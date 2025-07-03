package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.customer.People;
import com.jinbooks.entity.customer.dto.PeoplePageDto;
import com.jinbooks.entity.customer.vo.PeopleVo;

import java.util.List;

/**
 * 联系人服务接口
 *
 * @author Wuyan
 */
public interface PeopleService{
    /**
     * 分页查询联系人列表
     */
    Page<PeopleVo> pageList(PeoplePageDto dto);

    /**
     * 获取联系人详情
     */
    PeopleVo getById(String id,String workspaceId);

    /**
     * 新增联系人
     */
    Boolean save(People dto);

    /**
     * 更新联系人
     */
    Boolean update(People dto);

    /**
     * 删除联系人
     */
    Boolean deleteByIds(List<String> ids);
}
