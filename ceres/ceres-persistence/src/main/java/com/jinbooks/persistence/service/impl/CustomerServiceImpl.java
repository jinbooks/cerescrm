package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.customer.dto.CustomerPageDto;
import com.jinbooks.entity.customer.vo.CustomerVo;
import com.jinbooks.persistence.mapper.CustomerMapper;
import com.jinbooks.persistence.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户服务实现
 *
 * @author Wuyan
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper baseMapper;

    @Override
    public Page<CustomerVo> pageList(CustomerPageDto dto) {
        return baseMapper.customerPageList(dto.build(), dto);
    }

    @Override
    public CustomerVo getById(String id, String workspaceId) {
        return baseMapper.getCustomerById(id, workspaceId);
    }

    @Override
    public Boolean save(Customer dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(Customer dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
