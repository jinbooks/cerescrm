package com.jinbooks.entity.product;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("jbx_product")
public class Product  extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6175144126412944963L;
	@TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    String id;
	
	String productCode;
	
	String productName;
	
	String productUnit;
	
	String version;
	
	private BigDecimal price;
	
	int stock;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date releaseDate;
	
	int lifeCycle;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date endOfLifeDate;
	
	String content;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	int status;
	
	String categoryId;
	
	String workspaceId;
	
    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

}
