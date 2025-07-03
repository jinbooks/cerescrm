package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.ProductGrant;
import com.jinbooks.entity.product.dto.ProductGrantPageDto;
import com.jinbooks.persistence.mapper.ProductGrantMapper;
import com.jinbooks.persistence.service.ProductGrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class ProductGrantServiceImpl implements ProductGrantService {
	
    private final ProductGrantMapper baseMapper;

    @Override
    public Page<ProductGrant> pageList(ProductGrantPageDto dto) {
        return baseMapper.pageList(dto.build(), dto);
    }

    @Override
    public ProductGrant getById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Boolean save(ProductGrant dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(ProductGrant dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

	@Override
	public ProductGrant getByLicense(String license) {
		LambdaQueryWrapper<ProductGrant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductGrant::getLicense, license);
		return baseMapper.selectOne(wrapper);
	}
}
