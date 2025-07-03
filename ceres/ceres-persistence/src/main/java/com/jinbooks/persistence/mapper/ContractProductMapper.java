package com.jinbooks.persistence.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.contract.ContractProduct;
import com.jinbooks.entity.contract.dto.ContractProductPageDto;
import com.jinbooks.mapper.BaseMapperPlus;

/**
 * 数据层
 *
 */
public interface ContractProductMapper extends BaseMapperPlus<ContractProductMapper, ContractProduct, ContractProduct> {
    
    Page<ContractProduct> pageList(Page<ContractProduct> page, @Param("dto") ContractProductPageDto dto);

}
