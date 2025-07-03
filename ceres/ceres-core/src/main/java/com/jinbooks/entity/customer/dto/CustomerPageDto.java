package com.jinbooks.entity.customer.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 客户分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -6789012345678901234L;


    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户类型：个人、企业
     */
    private String customerType;

    /**
     * 客户分组ID
     */
    private String segmentId;

    /**
     * 客户负责人
     */
    private String customerHead;
    
    String workspaceId;

}
