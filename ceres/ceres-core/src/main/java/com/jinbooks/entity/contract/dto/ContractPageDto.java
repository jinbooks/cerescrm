package com.jinbooks.entity.contract.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 合同分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -5544332211009988776L;


    /**
     * 关联客户ID
     */
    private String customerId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 签订日期范围
     */
    private String startSigningDate;

    private String endSigningDate;
    
    String workspaceId;
}
