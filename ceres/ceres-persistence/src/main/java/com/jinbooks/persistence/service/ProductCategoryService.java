package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.ProductCategory;
import com.jinbooks.entity.product.dto.ProductCategoryPageDto;
import java.util.List;

import org.dromara.hutool.core.tree.MapTree;

/**
 * 
 *
 */
public interface ProductCategoryService {
    /**
     * 分页查询列表
     */
    Page<ProductCategory> pageList(ProductCategoryPageDto dto);

    /**
     * 获取详情
     */
    ProductCategory getById(String id);

    /**
     * 新增
     */
    Boolean save(ProductCategory dto);

    /**
     * 更新
     */
    Boolean update(ProductCategory dto);

    /**
     * 删除
     */
    Boolean deleteByIds(List<String> ids);
    
    public List<MapTree<String>> tree(String workspaceId) ;
}
