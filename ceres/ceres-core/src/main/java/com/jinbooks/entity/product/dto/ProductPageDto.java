package com.jinbooks.entity.product.dto;

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
public class ProductPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -6789012345678901234L;

    String productCode;
    
    String productName;
    
    String productUnit;
	
	String version;
    
    int status;
    
    String categoryId;
    
    String workspaceId;

}
