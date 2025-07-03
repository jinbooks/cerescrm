package com.jinbooks.entity.opportunity.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 15:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OpportunityPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = 5310784214016667779L;

    String workspaceId;

    Integer status;

    Integer priority;

    String opportunityCode;

    String name;

    String customerName;

    String contactName;
}
