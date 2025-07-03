package com.jinbooks.entity.product;

import java.io.Serializable;
import java.util.Date;

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
@TableName("jbx_product_grant")
public class ProductGrant  extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1005326220906584848L;

	@TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    String id;
	
	String license;
	
	String customerId;
	
	String customerName;
	
	String productId;
	
	String productCode;
    
    String productName;
	
	String productVersion;
	
	String grantType;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date grantDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date expirationDate;
    
	String content;
	
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
