package com.jinbooks.entity.contract.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractProductPageDto extends PageQuery {


    /**
	 * 
	 */
	private static final long serialVersionUID = -6423752976058449476L;

	String contractId;
	
	String productCode;
    
    String productName;
    
    String workspaceId;
}
