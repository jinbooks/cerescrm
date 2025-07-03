package com.jinbooks.entity.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同表 jbx_contract
 *
 * @author wuyan
 * {@code @date} 2025-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractVo extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 合同编码
     */
    private String code;
    
    /**
     * 关联客户ID
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同金额
     */
    private BigDecimal amount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 合同状态：草稿、待审批、审批通过、审批拒绝、履行中、已完成、已终止
     */
    private String status;

    /**
     * 签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate signingDate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate effectiveDate;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expirationDate;

    /**
     * 税率
     */
    private Float taxRate;

    /**
     * 合同内容
     */
    private String content;
    
    private BigDecimal payAmount;
    
    private BigDecimal invoiceAmount;
    
    private BigDecimal receiveAmount;
    
    String workspaceId;

}
