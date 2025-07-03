package com.jinbooks.entity.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinbooks.entity.BaseEntity;
import com.jinbooks.validate.EditGroup;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_receive_payment")
public class ReceivePayment extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -6405003224401751405L;

	/**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    private String id;
    
    String contractId;
    
    String contractCode;
    
    String contractName;
    
    String customerId;
    
    String customerName;
    /**
     * 开票信息
     * 
     */
    
    private String taxNumber;
    
    private String taxContact;
    
    private String taxBank;
    
    private String taxBankAccount;
    
    private String taxBankAccountNumber;
    
    /**
     * 税率
     */
    Float taxRate;
    
    int percentage;
    
    BigDecimal amount;
    
    BigDecimal taxAmount;
    
    BigDecimal afterTaxAmount;
    
    String currency;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    LocalDate  invoiceDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    LocalDate  receiveDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    int status;
    
    String workspaceId;
    
    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
