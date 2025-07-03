package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.customer.People;
import com.jinbooks.entity.customer.dto.PeoplePageDto;
import com.jinbooks.entity.customer.vo.PeopleVo;
import com.jinbooks.persistence.mapper.PeopleMapper;
import com.jinbooks.persistence.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 联系人服务实现
 *
 * @author Wuyan
 */
@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {
    private final PeopleMapper baseMapper;

    @Override
    public Page<PeopleVo> pageList(PeoplePageDto dto) {
        return baseMapper.pageList(dto.build(), dto);
    }

    @Override
    public PeopleVo getById(String id,String workspaceId) {
        return baseMapper.getPeopleById(id,workspaceId);
    }

    @Override
    public Boolean save(People dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(People dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
