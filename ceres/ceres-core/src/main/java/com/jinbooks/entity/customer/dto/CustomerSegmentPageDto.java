package com.jinbooks.entity.customer.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 客户分层分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerSegmentPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -1122334455667788990L;
    
    String workspaceId;
}
