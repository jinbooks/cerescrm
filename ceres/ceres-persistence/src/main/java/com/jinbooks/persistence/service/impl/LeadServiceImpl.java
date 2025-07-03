package com.jinbooks.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadChangeDto;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.dto.LeadStatisticsDto;
import com.jinbooks.entity.lead.vo.LeadFollowUpVo;
import com.jinbooks.entity.lead.vo.LeadStatisticsVo;
import com.jinbooks.entity.lead.vo.LeadVo;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.OpportunityVo;
import com.jinbooks.enums.FollowUpCategoryEnum;
import com.jinbooks.enums.LeadStatusEnum;
import com.jinbooks.exception.BusinessException;
import com.jinbooks.persistence.mapper.FollowUpMapper;
import com.jinbooks.persistence.mapper.LeadMapper;
import com.jinbooks.persistence.mapper.OpportunityMapper;
import com.jinbooks.persistence.mapper.UserInfoMapper;
import com.jinbooks.persistence.service.LeadService;
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
 * @time: 2025/6/13 14:02
 */

@Service
@RequiredArgsConstructor
public class LeadServiceImpl extends ServiceImpl<LeadMapper, Lead> implements LeadService {

    private final LeadMapper leadMapper;

    private final OpportunityMapper opportunityMapper;

    private final UserInfoMapper userInfoMapper;

    private final FollowUpMapper followUpMapper;

    @Override
    public Message<LeadFollowUpVo> getById(String id) {
        Lead lead = super.getById(id);
        if (ObjectUtils.isNotEmpty(lead.getOwnerId())) {
            UserInfo userInfo = userInfoMapper.selectById(lead.getOwnerId());
            lead.setOwnerName(userInfo.getUsername());
        }

        LambdaUpdateWrapper<FollowUp> followUpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        followUpLambdaUpdateWrapper.eq(FollowUp::getWorkspaceId, lead.getWorkspaceId());
        followUpLambdaUpdateWrapper.eq(FollowUp::getCategory, FollowUpCategoryEnum.LEAD.getCode());
        followUpLambdaUpdateWrapper.eq(FollowUp::getRelationId, id);
        List<FollowUp> followUps = followUpMapper.selectList(followUpLambdaUpdateWrapper);
        LeadFollowUpVo leadFollowUpVo = new LeadFollowUpVo(lead, followUps);
        return Message.ok(leadFollowUpVo);
    }

    @Override
    public Message<LeadVo> pageList(LeadPageDto dto) {
        Page<Lead> leadPage = leadMapper.pageList(dto.build(), dto);


        // 2. 批量统计各种状态的线索数量（一次查询完成）
        LeadStatisticsVo statistics = getLeadStatistics(dto.getWorkspaceId());

        // 3. 构建返回对象
        LeadVo leadVo = new LeadVo();
        leadVo.setLeadPage(leadPage);
        leadVo.setTotalLead(statistics.getLeadNums());
        leadVo.setIntentionCount(statistics.getIntentionCount());
        leadVo.setPendingFollowUpCount(statistics.getPendingCount());

        // 计算转化率（避免除零错误）
        BigDecimal conversionRate;
        if (statistics.getLeadNums() > 0) {
            conversionRate = BigDecimal.valueOf(statistics.getConvertedCount())
                    .divide(BigDecimal.valueOf(statistics.getLeadNums()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } else {
            conversionRate = BigDecimal.ZERO;
        }

        leadVo.setConversionRate(conversionRate);

        return Message.ok(leadVo);
    }

    @Override
    public Message<String> save(LeadChangeDto dto) {
        Lead lead = BeanUtil.copyProperties(dto, Lead.class);

        generateLeadCode(lead);

        setConvertedAndAssignedTime(lead, false);

        boolean save = super.save(lead);

        return save ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<String> update(LeadChangeDto dto) {
        Lead lead = BeanUtil.copyProperties(dto, Lead.class);

        setConvertedAndAssignedTime(lead, true);

        boolean result = super.updateById(lead);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }

    private void generateLeadCode(Lead lead) {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        LambdaQueryWrapper<Lead> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Lead::getWorkspaceId, lead.getWorkspaceId())
                .likeRight(Lead::getLeadCode, "Lead-" + datePrefix)
                .orderByDesc(Lead::getLeadCode)
                .last("LIMIT 1");
                // 只查一条最新的

        Lead lastLead = super.getOne(wrapper);

        int nextNumber = 1;
        if (lastLead != null && lastLead.getLeadCode() != null) {
            String lastCode = lastLead.getLeadCode(); // e.g., Lead-2406180012
            String numberStr = lastCode.substring(11); // 取最后四位
            try {
                nextNumber = Integer.parseInt(numberStr) + 1;

                // ✅ 超出 9999 上限判断
                if (nextNumber > 9999) {
                    throw new BusinessException(50001, "今日线索编号已达上限（9999），无法继续生成！");
                }
            } catch (NumberFormatException e) {
                nextNumber = 1; // fallback
            }
        }

        String code = String.format("Lead-%s%04d", datePrefix, nextNumber);
        lead.setLeadCode(code);
    }

    /**
     * 统计数据
     */
    private LeadStatisticsVo getLeadStatistics(String workspaceId) {
        LeadStatisticsDto dto = new LeadStatisticsDto();
        dto.setWorkspaceId(workspaceId);

        return Optional.ofNullable(leadMapper.getLeadStatisticsBySQL(dto))
                .orElseGet(LeadStatisticsVo::new);
    }

    private void setConvertedAndAssignedTime(Lead lead, boolean isEdit) {
        Date now = new Date();
        Integer transferredCode = LeadStatusEnum.TRANSFERRED_CUSTOMERS.getCode();
        String ownerId = lead.getOwnerId();
        Integer status = lead.getStatus();

        if (isEdit) {
            Lead originLead = super.getById(lead.getId());
            Integer oldStatus = originLead.getStatus();

            if (transferredCode.equals(status) && !Objects.equals(oldStatus, transferredCode)) {
                throw new BusinessException(50001, "设置已转商机状态请使用操作栏更多里的“转化商机”功能");
            }

            // 设置转化时间（仅状态从非转化变为转化时）
            if (Objects.equals(oldStatus, transferredCode) && !Objects.equals(status, transferredCode)) {
                Long l = opportunityMapper.selectCount(Wrappers.<Opportunity>lambdaQuery()
                        .eq(Opportunity::getWorkspaceId, originLead.getWorkspaceId())
                        .eq(Opportunity::getLeadId, originLead.getId()));
                if (l > 0) {
                    throw new BusinessException(50001, "该线索已经转化成商机，请先删除对应商机后再修改线索状态");
                }
            }

            // 设置分配时间（仅当负责人变更时）
            if (StringUtils.isNotEmpty(ownerId) && !Objects.equals(ownerId, originLead.getOwnerId())) {
                lead.setAssignedAt(now);
            }
        } else {
            if (transferredCode.equals(status)) {
                throw new BusinessException(50001, "设置已转商机状态请使用操作栏更多里的“转化商机”功能");
            }
        /*    // 新增：状态为转化时设置转化时间
            if (Objects.equals(lead.getStatus(), transferredCode)) {
                lead.setConvertedAt(now);
            }*/

            // 新增：负责人不为空时设置分配时间
            if (StringUtils.isNotEmpty(ownerId)) {
                lead.setAssignedAt(now);
            }
        }
    }

}
