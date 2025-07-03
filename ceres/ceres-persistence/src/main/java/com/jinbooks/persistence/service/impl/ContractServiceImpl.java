package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.contract.Contract;
import com.jinbooks.entity.contract.dto.ContractPageDto;
import com.jinbooks.entity.contract.vo.ContractVo;
import com.jinbooks.persistence.mapper.ContractMapper;
import com.jinbooks.persistence.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合同服务实现
 *
 * @author Wuyan
 */
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractMapper baseMapper;

    @Override
    public Page<ContractVo> pageList(ContractPageDto dto) {
        return baseMapper.contractPageList(dto.build(), dto);
    }

    @Override
    public ContractVo getById(String id,String workspaceId) {
        return baseMapper.getContractById(id,workspaceId);
    }

    @Override
    public Boolean save(Contract dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(Contract dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
