package com.jinbooks.entity.contract;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合同产品表 jbx_contract_product
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_contract_product")
public class ContractProduct extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8190605993747127827L;

	/**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 产品信息
     */
	String productId;
	
	String productCode;
    
    String productName;
	
	String productVersion;
	
	String productUnit;
	
	BigDecimal price;
	/**
	 * 购买数量
	 */
	int  quantity;
	
	BigDecimal originalPrice;
	/**
	 * 折扣率
	 */
	BigDecimal discount;
    /**
     * 折扣后金额
     */
    BigDecimal amount;


    String workspaceId;
    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
