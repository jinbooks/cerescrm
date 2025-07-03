package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.customer.dto.CustomerPageDto;
import com.jinbooks.entity.customer.vo.CustomerVo;
import com.jinbooks.entity.dashboard.BaseGroupVo;
import com.jinbooks.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户 数据层
 *
 * @author Wuyan
 */
public interface CustomerMapper extends BaseMapperPlus<CustomerMapper, Customer, CustomerVo> {
    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param dto  查询参数
     * @return 查询结果
     */
    Page<CustomerVo> customerPageList(Page<Customer> page, @Param("dto") CustomerPageDto dto);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 查询结果
     */
    CustomerVo getCustomerById(@Param("id") String id, @Param("workspaceId") String workspaceId);

    List<BaseGroupVo<Long>> countRecentCustomer(String workspaceId);

    List<BaseGroupVo<Long>> countGroupFromCustomer(String workspaceId);

    Long countCurrentYear(String workspaceId);

    List<BaseGroupVo<Long>> groupYearCustomer(@Param("year") Integer year, @Param("workspaceId") String workspaceId);
}
