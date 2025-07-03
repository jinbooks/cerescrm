package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jinbooks.entity.customer.CustomerSegment;
import com.jinbooks.entity.customer.dto.CustomerSegmentPageDto;
import com.jinbooks.entity.customer.vo.CustomerSegmentVo;

import java.util.List;

/**
 * 客户分层服务接口
 *
 * @author Wuyan
 */
public interface CustomerSegmentService {
    /**
     * 分页查询客户分层列表
     */
    IPage<CustomerSegmentVo> pageList(CustomerSegmentPageDto dto);

    /**
     * 获取客户分层详情
     */
    CustomerSegmentVo getById(String id);

    /**
     * 新增客户分层
     */
    Boolean save(CustomerSegment dto);

    /**
     * 更新客户分层
     */
    Boolean update(CustomerSegment dto);

    /**
     * 删除客户分层
     */
    Boolean deleteByIds(List<String> ids);
}
