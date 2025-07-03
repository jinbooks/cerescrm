package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.ProductGrant;
import com.jinbooks.entity.product.dto.ProductGrantPageDto;
import java.util.List;

/**
 * 
 *
 */
public interface ProductGrantService {
    /**
     * 分页查询列表
     */
    Page<ProductGrant> pageList(ProductGrantPageDto dto);

    /**
     * 获取详情
     */
    ProductGrant getById(String id);

    /**
     * 获取详情
     */
    ProductGrant getByLicense(String license);
    
    /**
     * 新增
     */
    Boolean save(ProductGrant dto);

    /**
     * 更新
     */
    Boolean update(ProductGrant dto);

    /**
     * 删除
     */
    Boolean deleteByIds(List<String> ids);
}
