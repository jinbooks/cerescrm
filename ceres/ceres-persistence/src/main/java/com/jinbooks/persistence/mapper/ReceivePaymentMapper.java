package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinbooks.entity.dashboard.BaseGroupVo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.payment.ReceivePayment;
import com.jinbooks.entity.payment.dto.ReceivePaymentPageDto;
import com.jinbooks.mapper.BaseMapperPlus;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收款 数据层
 *
 * @author Wuyan
 */
public interface ReceivePaymentMapper extends BaseMapperPlus<ReceivePaymentMapper, ReceivePayment, ReceivePayment> {

    Page<ReceivePayment> pageList(Page<ReceivePayment> page, @Param("dto") ReceivePaymentPageDto dto);

    List<BaseGroupVo<BigDecimal>> countRecentReceivePayment(String workspaceId);

    BigDecimal selectAllAccount(String workspaceId);

    BigDecimal selectYearReceivePayment(@Param("year") Integer year, @Param("workspaceId") String workspaceId);

    BigDecimal selectYearAccount(String workspaceId);

    List<BaseGroupVo<BigDecimal>> groupYearReceivePayment(@Param("year") Integer year, @Param("workspaceId") String workspaceId);
}
