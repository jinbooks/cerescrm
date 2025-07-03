package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.Product;
import com.jinbooks.entity.product.dto.ProductPageDto;
import com.jinbooks.persistence.mapper.ProductMapper;
import com.jinbooks.persistence.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
    private final ProductMapper baseMapper;

    @Override
    public Page<Product> pageList(ProductPageDto dto) {
        return baseMapper.pageList(dto.build(), dto);
    }

    @Override
    public Product getById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Boolean save(Product dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(Product dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
