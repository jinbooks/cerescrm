package com.jinbooks.entity.lead.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.lead.Lead;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/18 11:47
 */

@Data
public class LeadVo {

    private Page<Lead> leadPage;

    private Long totalLead;

    private Long intentionCount;

    private Long pendingFollowUpCount;

    private BigDecimal conversionRate;

}
