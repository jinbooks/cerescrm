package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.payment.ReceivePayment;
import com.jinbooks.entity.payment.dto.ReceivePaymentPageDto;
import com.jinbooks.persistence.mapper.ReceivePaymentMapper;
import com.jinbooks.persistence.service.ReceivePaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 财务收款服务实现
 *
 * @author Wuyan
 */
@Service
@RequiredArgsConstructor
public class ReceivePaymentServiceImpl implements ReceivePaymentService {
	
    private final ReceivePaymentMapper baseMapper;

    @Override
    public Page<ReceivePayment> pageList(ReceivePaymentPageDto dto) {
        return baseMapper.pageList(dto.build(), dto);
    }

    @Override
    public ReceivePayment getById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Boolean save(ReceivePayment dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(ReceivePayment dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
