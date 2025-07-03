package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.Product;
import com.jinbooks.entity.product.dto.ProductPageDto;

import java.util.List;

/**
 * 
 *
 */
public interface ProductService {
    /**
     * 分页查询列表
     */
    Page<Product> pageList(ProductPageDto dto);

    /**
     * 获取详情
     */
    Product getById(String id);

    /**
     * 新增
     */
    Boolean save(Product dto);

    /**
     * 更新
     */
    Boolean update(Product dto);

    /**
     * 删除
     */
    Boolean deleteByIds(List<String> ids);
}
