package com.jinbooks.entity.lead.dto;

import com.jinbooks.enums.LeadStatusEnum;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/18 18:10
 */

@Data
public class LeadStatisticsDto {
    private String workspaceId;

    private Integer intention = LeadStatusEnum.INTENTION_CLUE.getCode();

    private Integer transferred = LeadStatusEnum.TRANSFERRED_CUSTOMERS.getCode();
}
