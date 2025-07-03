package com.jinbooks.entity.payment.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReceivePaymentPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -6789012345678901234L;


    String  contractId;
    
    private String contractCode;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 
     */
    private String contractName;

    /**
     * 客户负责人
     */
    private String customerHead;
    
    int status;
    
    String workspaceId;

}
