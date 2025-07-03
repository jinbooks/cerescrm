package com.jinbooks.persistence.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.Product;
import com.jinbooks.entity.product.dto.ProductPageDto;
import com.jinbooks.mapper.BaseMapperPlus;

/**
 * 
 */
public interface ProductMapper extends BaseMapperPlus<ProductMapper, Product, Product> {
    
    Page<Product> pageList(Page<Product> page, @Param("dto") ProductPageDto dto);

}
