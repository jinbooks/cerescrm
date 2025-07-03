package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.payment.ReceivePayment;
import com.jinbooks.entity.payment.dto.ReceivePaymentPageDto;

import java.util.List;

/**
 * 财务收款服务接口
 *
 * @author Wuyan
 */
public interface ReceivePaymentService {
    /**
     * 分页查询列表
     */
    Page<ReceivePayment> pageList(ReceivePaymentPageDto dto);

    /**
     * 获取详情
     */
    ReceivePayment getById(String id);

    /**
     * 新增
     */
    Boolean save(ReceivePayment dto);

    /**
     * 更新
     */
    Boolean update(ReceivePayment dto);

    /**
     * 删除
     */
    Boolean deleteByIds(List<String> ids);
}
