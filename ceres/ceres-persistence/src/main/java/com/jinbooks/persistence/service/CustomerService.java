package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.customer.dto.CustomerPageDto;
import com.jinbooks.entity.customer.vo.CustomerVo;

import java.util.List;

/**
 * 客户服务接口
 *
 * @author Wuyan
 */
public interface CustomerService {
    /**
     * 分页查询客户列表
     */
    Page<CustomerVo> pageList(CustomerPageDto dto);

    /**
     * 获取客户详情
     */
    CustomerVo getById(String id, String workspaceId);

    /**
     * 新增客户
     */
    Boolean save(Customer dto);

    /**
     * 更新客户
     */
    Boolean update(Customer dto);

    /**
     * 删除客户
     */
    Boolean deleteByIds(List<String> ids);
}
