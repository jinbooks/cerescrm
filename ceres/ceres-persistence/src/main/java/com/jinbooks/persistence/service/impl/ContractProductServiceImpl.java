package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.contract.ContractProduct;
import com.jinbooks.entity.contract.dto.ContractProductPageDto;
import com.jinbooks.persistence.mapper.ContractProductMapper;
import com.jinbooks.persistence.service.ContractProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现

 */
@Service
@RequiredArgsConstructor
public class ContractProductServiceImpl implements ContractProductService {
	
    private final ContractProductMapper baseMapper;

    @Override
    public Page<ContractProduct> pageList(ContractProductPageDto dto) {
        return baseMapper.pageList(dto.build(), dto);
    }

    @Override
    public ContractProduct getById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Boolean save(ContractProduct dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(ContractProduct dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
