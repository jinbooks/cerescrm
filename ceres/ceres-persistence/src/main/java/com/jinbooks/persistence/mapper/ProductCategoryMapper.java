package com.jinbooks.persistence.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.ProductCategory;
import com.jinbooks.entity.product.dto.ProductCategoryPageDto;
import com.jinbooks.mapper.BaseMapperPlus;

/**
 * 
 */
public interface ProductCategoryMapper extends BaseMapperPlus<ProductCategoryMapper, ProductCategory, ProductCategory> {
    
    Page<ProductCategory> pageList(Page<ProductCategory> page, @Param("dto") ProductCategoryPageDto dto);

}
