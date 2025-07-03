package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.customer.People;
import com.jinbooks.entity.customer.dto.PeoplePageDto;
import com.jinbooks.entity.customer.vo.PeopleVo;
import com.jinbooks.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * 联系人 数据层
 *
 * @author Wuyan
 */
public interface PeopleMapper extends BaseMapperPlus<PeopleMapper, People, PeopleVo> {
    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param dto  查询参数
     * @return 查询结果
     */
    Page<PeopleVo> pageList(Page<Customer> page, @Param("dto") PeoplePageDto dto);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 查询结果
     */
    PeopleVo getPeopleById(@Param("id") String id, @Param("workspaceId") String workspaceId);
}
