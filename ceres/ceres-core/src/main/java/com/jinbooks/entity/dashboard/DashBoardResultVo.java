package com.jinbooks.entity.dashboard;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/06/29 13:23:06
 * {@code @version} 1.0
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DashBoardResultVo {
    // 迷你图数据
    private List<BaseCountData<?>> countData;

    /**
     * 漏斗数据
     */
    private List<BaseGroupVo<Long>> funnelData;

    /**
     * 客户来源分组数据
     */
    private List<BaseGroupVo<Long>> customerFromData;

    /**
     * 年度合同金额月统计
     */
    private BaseCountData<BigDecimal> contractAmountData;

    /**
     * 年度新增客户月统计
     */
    private BaseCountData<Long> customerData;

    /**
     * 年度回款金额月统计
     */
    private BaseCountData<BigDecimal> receivePaymentData;

    /**
     * 执行中合同金额统计
     */
    private ContractAmountTotal contractAmountTotalData;

    /**
     * 年合同总金额
     */
    private BaseCountData<BigDecimal> yearAmountData;

}
