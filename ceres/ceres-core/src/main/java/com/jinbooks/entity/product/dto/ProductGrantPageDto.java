package com.jinbooks.entity.product.dto;

import com.jinbooks.entity.PageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductGrantPageDto extends PageQuery {


    /**
	 * 
	 */
	private static final long serialVersionUID = 2728458099614098063L;

	String license;
	
	String customerId;
	
	String customerName;
	
	String productId;
	
	String productCode;
    
    String productName;
	
	String version;
    
    int status;
    
    String workspaceId;

}
