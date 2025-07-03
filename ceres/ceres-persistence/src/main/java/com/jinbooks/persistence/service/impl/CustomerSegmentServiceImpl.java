package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jinbooks.entity.customer.CustomerSegment;
import com.jinbooks.entity.customer.dto.CustomerSegmentPageDto;
import com.jinbooks.entity.customer.vo.CustomerSegmentVo;
import com.jinbooks.persistence.mapper.CustomerSegmentMapper;
import com.jinbooks.persistence.service.CustomerSegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户分层服务实现
 *
 * @author Wuyan
 */
@Service
@RequiredArgsConstructor
public class CustomerSegmentServiceImpl implements CustomerSegmentService {
    private final CustomerSegmentMapper baseMapper;

    @Override
    public IPage<CustomerSegmentVo> pageList(CustomerSegmentPageDto dto) {
        LambdaQueryWrapper<CustomerSegment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CustomerSegment::getWorkspaceId, dto.getWorkspaceId());
        return baseMapper.selectVoPage(dto.build(), lqw);
    }

    @Override
    public CustomerSegmentVo getById(String id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean save(CustomerSegment dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(CustomerSegment dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
