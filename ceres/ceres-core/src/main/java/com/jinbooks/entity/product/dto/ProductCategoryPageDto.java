package com.jinbooks.entity.product.dto;

import com.jinbooks.entity.PageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询参数
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductCategoryPageDto extends PageQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2693451565769738084L;

	String code;
	
	String name;
	
	String parentId;
	
    String workspaceId;

}
