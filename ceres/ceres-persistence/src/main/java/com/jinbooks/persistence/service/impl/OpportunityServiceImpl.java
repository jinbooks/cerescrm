package com.jinbooks.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.customer.People;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.opportunity.OppStage;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.OpportunityVo;
import com.jinbooks.entity.opportunity.dto.OppStageSetDto;
import com.jinbooks.entity.opportunity.dto.OpportunityChangeDto;
import com.jinbooks.entity.opportunity.dto.OpportunityPageDto;
import com.jinbooks.enums.FollowUpCategoryEnum;
import com.jinbooks.enums.LeadStatusEnum;
import com.jinbooks.exception.BusinessException;
import com.jinbooks.persistence.mapper.*;
import com.jinbooks.persistence.service.OpportunityService;
import com.jinbooks.persistence.service.UserInfoService;
import com.jinbooks.web.WebContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/19 17:39
 */

@Service
@RequiredArgsConstructor
public class OpportunityServiceImpl extends ServiceImpl<OpportunityMapper, Opportunity> implements OpportunityService {

    private final OppStageMapper oppStageMapper;

    private final LeadMapper leadMapper;

    private final OpportunityMapper opportunityMapper;

    private final UserInfoMapper userInfoMapper;

    private final PeopleMapper peopleMapper;

    private final CustomerMapper customerMapper;

    private final FollowUpMapper followUpMapper;

    @Override
    public Message<OpportunityVo> getById(String id) {
        Opportunity opportunity = super.getById(id);
        if (ObjectUtils.isNotEmpty(opportunity.getOwnerId())) {
            UserInfo userInfo = userInfoMapper.selectById(opportunity.getOwnerId());
            Optional.ofNullable(userInfo)
                    .map(UserInfo::getUsername)
                    .ifPresent(opportunity::setOwnerName);
        }
        if (ObjectUtils.isNotEmpty(opportunity.getLeadId())) {
            Lead lead = leadMapper.selectById(opportunity.getLeadId());
            Optional.ofNullable(lead)
                    .map(Lead::getLeadCode)
                    .ifPresent(opportunity::setLeadName);
        }
        if (ObjectUtils.isNotEmpty(opportunity.getContactId())) {
            People people = peopleMapper.selectById(opportunity.getContactId());
            Optional.ofNullable(people)
                    .map(People::getContactName)
                    .ifPresent(opportunity::setContactName);
        }
        if (ObjectUtils.isNotEmpty(opportunity.getCustomerId())) {
            Optional.ofNullable(customerMapper.selectById(opportunity.getCustomerId()))
                    .map(Customer::getCustomerName)
                    .ifPresent(opportunity::setCustomerName);
        }
        if (ObjectUtils.isNotEmpty(opportunity.getStageId())) {
            Optional.ofNullable(oppStageMapper.selectById(opportunity.getStageId()))
                    .map(OppStage::getName)
                    .ifPresent(opportunity::setStageName);
        }

        LambdaUpdateWrapper<FollowUp> followUpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        followUpLambdaUpdateWrapper.eq(FollowUp::getWorkspaceId, opportunity.getWorkspaceId());
        followUpLambdaUpdateWrapper.eq(FollowUp::getCategory, FollowUpCategoryEnum.OPPORTUNITY.getCode());
        followUpLambdaUpdateWrapper.eq(FollowUp::getRelationId, id);
        List<FollowUp> followUps = followUpMapper.selectList(followUpLambdaUpdateWrapper);
        OpportunityVo opportunityVo = new OpportunityVo(opportunity, followUps);

        return Message.ok(opportunityVo);
    }

    @Override
    public Message<Page<Opportunity>> pageList(OpportunityPageDto dto) {
        Page<Opportunity> page = opportunityMapper.pageList(dto.build(), dto);

        return Message.ok(page);
    }

    @Override
    @Transactional
    public Message<String> save(OpportunityChangeDto dto) {

        Opportunity opportunity = BeanUtil.copyProperties(dto, Opportunity.class);

        generateOppCode(opportunity);

        setAmountInfo(opportunity);

        setReasonAndInfo(opportunity, false);

        setNameAndCompany(opportunity);

        //获取默认阶段信息
        LambdaUpdateWrapper<OppStage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OppStage::getWorkspaceId, dto.getWorkspaceId());
        wrapper.eq(OppStage::getIsSystem, 1);
        wrapper.eq(OppStage::getIsActive, 0);
        wrapper.orderByAsc(OppStage::getSortOrder);
        Page<OppStage> page = new Page<>(1, 1); // 页码 1，每页 1 条
        IPage<OppStage> pageResult = oppStageMapper.selectPage(page, wrapper);
        OppStage firstStage = pageResult.getRecords().isEmpty() ? null : pageResult.getRecords().get(0);
        if (Objects.isNull(firstStage)) {
            throw new BusinessException(50001, "请先生成商机阶段信息");
        } else {
            opportunity.setStageId(firstStage.getId());
        }

        if (Boolean.TRUE.equals(dto.getIsLeadConvert())) {
            leadMapper.update(Wrappers.<Lead>lambdaUpdate()
                    .eq(Lead::getId, dto.getLeadId())
                    .set(Lead::getStatus, LeadStatusEnum.TRANSFERRED_CUSTOMERS.getCode())
                    .set(Lead::getConvertedAt, new Date()));
        }


        boolean result = super.save(opportunity);

        return result ? Message.ok(WebContext.getI18nValue("common.add.success")) : Message.failed("新增失败");
    }

    @Override
    public Message<String> update(OpportunityChangeDto dto) {
        Opportunity opportunity = BeanUtil.copyProperties(dto, Opportunity.class);

        setAmountInfo(opportunity);

        setReasonAndInfo(opportunity, true);

        setNameAndCompany(opportunity);

        boolean result = super.updateById(opportunity);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }

    private void setAmountInfo(Opportunity opportunity) {
        //加权金额
        BigDecimal weightedAmount = opportunity.getExpectedAmount()
                .multiply(BigDecimal.valueOf(opportunity.getProbability()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        opportunity.setWeightedAmount(weightedAmount);
        //预估利润
        opportunity.setProfit(opportunity.getExpectedAmount().subtract(opportunity.getCost()));
    }

    private void generateOppCode(Opportunity opportunity) {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        LambdaQueryWrapper<Opportunity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Opportunity::getWorkspaceId, opportunity.getWorkspaceId())
                .likeRight(Opportunity::getOpportunityCode, "OPP-" + datePrefix)
                .orderByDesc(Opportunity::getOpportunityCode)
                .last("LIMIT 1");
        // 只查一条最新的

        Opportunity lastOpp = super.getOne(wrapper);

        int nextNumber = 1;
        if (lastOpp != null && lastOpp.getOpportunityCode() != null) {
            String lastCode = lastOpp.getOpportunityCode();
            String numberStr = lastCode.substring(11);
            try {
                nextNumber = Integer.parseInt(numberStr) + 1;

                // ✅ 超出 9999 上限判断
                if (nextNumber > 9999) {
                    throw new BusinessException(50001, "今日商机编号已达上限（9999），无法继续生成！");
                }
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        String code = String.format("OPP-%s%04d", datePrefix, nextNumber);
        opportunity.setOpportunityCode(code);
    }

    private void setReasonAndInfo(Opportunity opportunity, boolean isEdit) {
        Date now = new Date();
        Integer status = opportunity.getStatus();
        String ownerId = opportunity.getOwnerId();

        Opportunity originOpp = null;
        if (isEdit) {
            originOpp = super.getById(opportunity.getId());

            // 设置成交日期（仅当状态从非2变成2时）
            if (status == 2 && !Objects.equals(status, originOpp.getStatus())) {
                opportunity.setActualCloseDate(now);
            }

            // 设置分配时间（仅当负责人变更时）
            if (StringUtils.isNotEmpty(ownerId) && !Objects.equals(ownerId, originOpp.getOwnerId())) {
                opportunity.setAssignedAt(now);
            }
        } else {
            if (status == 2) {
                opportunity.setActualCloseDate(now);
            }
            if (StringUtils.isNotEmpty(ownerId)) {
                opportunity.setAssignedAt(now);
            }
        }

        // 设置最后活动日期
        opportunity.setLastActivityDate(now);
    }

    @Override
    public Message<String> updateOpportunityStage(OppStageSetDto dto) {
        Opportunity opportunity = super.getById(dto.getOppId());
        if (dto.getStageId().equals(opportunity.getStageId())) {
            return new Message<>(Message.SUCCESS, "并未发生变化");
        }
        opportunity.setStageId(dto.getStageId());
       /* OppStage oppStage = oppStageMapper.selectById(dto.getStageId());
        if ("CLOSED_WON".equals(oppStage.getCode())) {
            opportunity.setActualCloseDate(new Date());
        }*/
        opportunity.setLastActivityDate(new Date());
        boolean update = super.updateById(opportunity);
        return update ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    /**
     * @Description: 设置线索的姓名和公司
     * @Param: [opportunity]
     * @return: void
     * @Author: xZen
     * @Date: 2025/6/27 16:53
     */
    private void setNameAndCompany(Opportunity opportunity) {
        if (ObjectUtils.isNotEmpty(opportunity.getLeadId())) {
            Lead lead = leadMapper.selectById(opportunity.getLeadId());
            if (lead != null) {
                opportunity.setPeopleName(lead.getName());
                opportunity.setCompany(lead.getCompany());
            }
        }
    }
}
