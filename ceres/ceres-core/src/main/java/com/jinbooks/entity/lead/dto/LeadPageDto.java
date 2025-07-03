package com.jinbooks.entity.lead.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/13 14:08
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class LeadPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -2509686036476372194L;

    String workspaceId;

    String name;
    
    String phone;

    String company;
    
    Integer priority;

    Integer status;
}
