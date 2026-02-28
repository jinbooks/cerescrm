package com.jinbooks.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.contract.Contract;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.followup.dto.FollowUpChangeDto;
import com.jinbooks.entity.followup.dto.FollowUpPageDto;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.enums.FollowUpCategoryEnum;
import com.jinbooks.persistence.mapper.ContractMapper;
import com.jinbooks.persistence.mapper.CustomerMapper;
import com.jinbooks.persistence.mapper.FollowUpMapper;
import com.jinbooks.persistence.mapper.LeadMapper;
import com.jinbooks.persistence.mapper.OpportunityMapper;
import com.jinbooks.persistence.service.FollowUpService;
import com.jinbooks.web.WebContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 15:12
 */

@Service
@RequiredArgsConstructor
public class FollowUpServiceImpl extends ServiceImpl<FollowUpMapper, FollowUp> implements FollowUpService {

    private final FollowUpMapper followUpMapper;

    private final LeadMapper leadMapper;
    
    private final ContractMapper contractMapper;
    
    private final OpportunityMapper opportunityMapper;
    
    private final CustomerMapper customerMapper;

    @Override
    public Message<Page<FollowUp>> pageList(FollowUpPageDto dto) {
        Page<FollowUp> followUpPage = followUpMapper.pageList(dto.build(), dto);

        return Message.ok(followUpPage);
    }

    @Override
    @Transactional
    public Message<String> save(FollowUpChangeDto dto) {
        FollowUp followUp = BeanUtil.copyProperties(dto, FollowUp.class);

        if (FollowUpCategoryEnum.LEAD.getCode().equals(followUp.getCategory())) {
            Lead lead = leadMapper.selectById(followUp.getRelationId());
            followUp.setCustomerName(lead.getCompany());
            followUp.setRelationName(lead.getName());
            lead.setFollowUpTimes(Optional.ofNullable(lead.getFollowUpTimes()).orElse(0) + 1);
            leadMapper.updateById(lead);
        }else if (FollowUpCategoryEnum.OPPORTUNITY.getCode().equals(followUp.getCategory())) {
        	Opportunity opportunity = opportunityMapper.selectById(followUp.getRelationId());
        	followUp.setCustomerName(opportunity.getCompany());
            followUp.setRelationName(opportunity.getName());
        }else if (FollowUpCategoryEnum.CONTRACT.getCode().equals(followUp.getCategory())) {
        	Contract contract = contractMapper.selectById(followUp.getRelationId());
            followUp.setRelationName(contract.getContractName());
            Customer customer = customerMapper.selectById(contract.getCustomerId());
        	followUp.setCustomerName(customer.getCustomerName());
        }else if (FollowUpCategoryEnum.CUSTOMER.getCode().equals(followUp.getCategory())) {
        	Customer customer = customerMapper.selectById(followUp.getRelationId());
        	followUp.setCustomerName(customer.getCustomerName());
            followUp.setRelationName(customer.getCustomerName());
        }

        boolean save = super.save(followUp);


        return save ? Message.ok(WebContext.getI18nValue("common.add.success")) : Message.failed(WebContext.getI18nValue("common.add.fail"));
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? Message.ok(WebContext.getI18nValue("common.delete.success")) : Message.failed(WebContext.getI18nValue("common.delete.fail"));
    }
}
