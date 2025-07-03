package com.jinbooks.persistence.service.impl;

import com.jinbooks.entity.dashboard.BaseCountData;
import com.jinbooks.entity.dashboard.BaseGroupVo;
import com.jinbooks.entity.dashboard.ContractAmountTotal;
import com.jinbooks.entity.dashboard.DashBoardResultVo;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.mapper.*;
import com.jinbooks.persistence.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/06/29 13:26:52
 * {@code @version} 1.0
 */

@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {
    private final LeadMapper leadMapper;
    private final OpportunityMapper opportunityMapper;
    private final CustomerMapper customerMapper;
    private final ContractMapper contractMapper;
    private final ReceivePaymentMapper receivePaymentMapper;
    private final ProductGrantMapper productGrantMapper;

    @Override
    public DashBoardResultVo dashboard(Integer year, UserInfo currentUser) {
        List<BaseCountData<?>> countData = new ArrayList<>();
        List<BaseGroupVo<Long>> funnelData = new ArrayList<>();

        // 线索统计
        List<BaseGroupVo<Long>> leadGroupVos = leadMapper.countRecentLeads(currentUser.getWorkspaceId());
        Long leadCount = leadMapper.countCurrentYear(currentUser.getWorkspaceId());
        funnelData.add(BaseGroupVo.<Long>builder()
                .name("线索")
                .value(leadCount)
                .build());
        countData.add(BaseCountData.<Long>builder()
                .title("线索总数")
                .value(leadCount)
                .trend(calculateTrend(leadGroupVos))
                .data(fillMissingMonths(leadGroupVos, Long.class))
                .build());
        // 商机统计
        List<BaseGroupVo<Long>> opportunityGroupVos = opportunityMapper.countRecentOpportunity(currentUser.getWorkspaceId());
        Long opportunityCount = opportunityMapper.countCurrentYear(currentUser.getWorkspaceId());
        funnelData.add(BaseGroupVo.<Long>builder()
                .name("商机")
                .value(opportunityCount)
                .build());
        countData.add(BaseCountData.<Long>builder()
                .title("商机总数")
                .value(opportunityCount)
                .trend(calculateTrend(opportunityGroupVos))
                .data(fillMissingMonths(opportunityGroupVos, Long.class))
                .build());
        // 客户统计
        List<BaseGroupVo<Long>> customerGroupVos = customerMapper.countRecentCustomer(currentUser.getWorkspaceId());
        Long customerCount = customerMapper.countCurrentYear(currentUser.getWorkspaceId());
        funnelData.add(BaseGroupVo.<Long>builder()
                .name("客户")
                .value(customerCount)
                .build());
        countData.add(BaseCountData.<Long>builder()
                .title("客户总数")
                .value(customerCount)
                .trend(calculateTrend(customerGroupVos))
                .data(fillMissingMonths(customerGroupVos, Long.class))
                .build());
        // 合同统计
        List<BaseGroupVo<Long>> contractGroupVos = contractMapper.countRecentContract(currentUser.getWorkspaceId());
        Long contractCount = contractMapper.countCurrentYear(currentUser.getWorkspaceId());
        funnelData.add(BaseGroupVo.<Long>builder()
                .name("合同")
                .value(contractCount)
                .build());
        countData.add(BaseCountData.<Long>builder()
                .title("合同总数")
                .value(contractCount)
                .trend(calculateTrend(contractGroupVos))
                .data(fillMissingMonths(contractGroupVos, Long.class))
                .build());
        // 回款统计
        List<BaseGroupVo<BigDecimal>> receivePaymentGroupVos = receivePaymentMapper.countRecentReceivePayment(currentUser.getWorkspaceId());
        BigDecimal receivePaymentCount = receivePaymentMapper.selectYearAccount(currentUser.getWorkspaceId());
        countData.add(BaseCountData.<BigDecimal>builder()
                .title("回款金额")
                .value(receivePaymentCount)
                .trend(calculateTrend(receivePaymentGroupVos))
                .data(fillMissingMonths(receivePaymentGroupVos, BigDecimal.class))
                .build());
        // 产品销量统计
        List<BaseGroupVo<Long>> productGrantGroupVos = productGrantMapper.countRecentProductGrant(currentUser.getWorkspaceId());
        Long productGrantCount = productGrantMapper.countCurrentYear(currentUser.getWorkspaceId());
        countData.add(BaseCountData.<Long>builder()
                .title("产品销量")
                .value(productGrantCount)
                .trend(calculateTrend(productGrantGroupVos))
                .data(fillMissingMonths(productGrantGroupVos, Long.class))
                .build());

        List<BaseGroupVo<Long>> customerFromData = customerMapper.countGroupFromCustomer(currentUser.getWorkspaceId());

        // 年度合同金额月统计
        List<BaseGroupVo<BigDecimal>> contractAmountGroupVos = contractMapper.groupYearContractAmount(year, currentUser.getWorkspaceId());
        BaseCountData<BigDecimal> contractAmountData = BaseCountData.<BigDecimal>builder()
                .title("年合同金额")
                .value(BigDecimal.ZERO)
                .trend(calculateTrend(contractAmountGroupVos))
                .data(fillMissingMonths(contractAmountGroupVos, BigDecimal.class, getEffectiveMonthList(year)))
                .build();
        // 年度新增客户月统计
        List<BaseGroupVo<Long>> customerYearGroupVos = customerMapper.groupYearCustomer(year, currentUser.getWorkspaceId());
        BaseCountData<Long> customerData = BaseCountData.<Long>builder()
                .title("年新增客户")
                .value(0L)
                .trend(calculateTrend(customerYearGroupVos))
                .data(fillMissingMonths(customerYearGroupVos, Long.class, getEffectiveMonthList(year)))
                .build();
        // 年度回款金额月统计
        List<BaseGroupVo<BigDecimal>> receivePaymentYearGroupVos = receivePaymentMapper.groupYearReceivePayment(year, currentUser.getWorkspaceId());
        BaseCountData<BigDecimal> receivePaymentData = BaseCountData.<BigDecimal>builder()
                .title("年新增客户")
                .value(BigDecimal.ZERO)
                .trend(calculateTrend(receivePaymentYearGroupVos))
                .data(fillMissingMonths(receivePaymentYearGroupVos, BigDecimal.class, getEffectiveMonthList(year)))
                .build();

        // 执行中合同金额统计
        ContractAmountTotal contractAmountTotalData = contractMapper.groupContractAmountTotal(currentUser.getWorkspaceId());

        // 年合同总金额
        BigDecimal yearContractAmount = contractMapper.selectYearContractAmount(year, currentUser.getWorkspaceId());
        BigDecimal yearReceivePaymentCount = receivePaymentMapper.selectYearReceivePayment(year, currentUser.getWorkspaceId());
        List<BaseGroupVo<BigDecimal>> yearAmountGroupVos = new ArrayList<>();
        yearAmountGroupVos.add(BaseGroupVo.<BigDecimal>builder()
                .name("本年已回款")
                .value(yearReceivePaymentCount)
                .build());
        yearAmountGroupVos.add(BaseGroupVo.<BigDecimal>builder()
                .name("本年未回款")
                .value(yearContractAmount.subtract(yearReceivePaymentCount))
                .build());
        BaseCountData<BigDecimal> yearAmountData = BaseCountData.<BigDecimal>builder()
                .title("年金额总计")
                .value(yearContractAmount)
                .trend(0)
                .data(yearAmountGroupVos)
                .build();

        return DashBoardResultVo.builder()
                .countData(countData)
                .funnelData(funnelData)
                .customerFromData(customerFromData)
                .contractAmountData(contractAmountData)
                .yearAmountData(yearAmountData)
                .customerData(customerData)
                .receivePaymentData(receivePaymentData)
                .contractAmountTotalData(contractAmountTotalData)
                .build();
    }


    /**
     * 计算最近两个月的趋势百分比
     *
     * @param data List<BaseGroupVo<Integer>>，月份顺序从旧到新
     * @return 趋势百分比（例如 25.0 表示增长25%，-10.0 表示下降10%）
     */
    public <T extends Number> double calculateTrend(List<BaseGroupVo<T>> data) {
        if (data == null || data.size() < 2) {
            return 0.0;
        }

        int size = data.size();
        T lastMonthVal = data.get(size - 2).getValue();
        T thisMonthVal = data.get(size - 1).getValue();

        double last = lastMonthVal != null ? lastMonthVal.doubleValue() : 0.0;
        double current = thisMonthVal != null ? thisMonthVal.doubleValue() : 0.0;

        if (last == 0.0) {
            return current == 0.0 ? 0.0 : 100.0;
        }

        return ((current - last) * 100.0) / last;
    }


    /**
     * 补全最近 6 个月的月份（从当前月往前推）缺失的数据项，value 默认为 0
     *
     * @param original List<BaseGroupVo<Long>> 已存在数据，按 name 升序（yyyy-MM）
     * @return List<BaseGroupVo < Long>> 补全后的完整数据
     */
    public <T extends Number> List<BaseGroupVo<T>> fillMissingMonths(List<BaseGroupVo<T>> original, Class<T> clazz) {
        return fillMissingMonths(original, clazz, 6);
    }

    /**
     * 补全最近 N 个月的月份（从当前月往前推）缺失的数据项，value 默认为 0
     *
     * @param original List<BaseGroupVo<Long>> 已存在数据，按 name 升序（yyyy-MM）
     * @return List<BaseGroupVo < Long>> 补全后的完整数据
     */
    public <T extends Number> List<BaseGroupVo<T>> fillMissingMonths(List<BaseGroupVo<T>> original, Class<T> clazz, int months) {
        Map<String, T> existingMap = new HashMap<>();
        for (BaseGroupVo<T> item : original) {
            existingMap.put(item.getName(), item.getValue());
        }

        List<BaseGroupVo<T>> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        LocalDate now = LocalDate.now().withDayOfMonth(1);
        for (int i = months - 1; i >= 0; i--) {
            String month = now.minusMonths(i).format(formatter);
            T value = existingMap.getOrDefault(month, defaultZero(clazz));
            result.add(new BaseGroupVo<>(month, value));
        }

        return result;
    }

    /**
     * 按指定月份列表补全 BaseGroupVo 数据项，缺失的 value 使用默认 0 值
     *
     * @param original 原始数据，name 为 yyyy-MM 格式
     * @param clazz    数值类型（如 Long.class, BigDecimal.class）
     * @param months   指定月份列表（yyyy-MM），升序排列
     * @return 补全后的 BaseGroupVo 列表
     */
    public <T extends Number> List<BaseGroupVo<T>> fillMissingMonths(List<BaseGroupVo<T>> original,
                                                                     Class<T> clazz,
                                                                     List<String> months) {
        Map<String, T> existingMap = new HashMap<>();
        for (BaseGroupVo<T> item : original) {
            existingMap.put(item.getName(), item.getValue());
        }

        List<BaseGroupVo<T>> result = new ArrayList<>();
        for (String month : months) {
            T value = existingMap.getOrDefault(month, defaultZero(clazz));
            result.add(new BaseGroupVo<>(month, value));
        }

        return result;
    }

    /**
     * 返回对应类型的 0 值，支持 Integer、Long、BigDecimal
     */
    @SuppressWarnings("unchecked")
    private <T extends Number> T defaultZero(Class<T> clazz) {
        if (clazz == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(0L);
        } else if (clazz == BigDecimal.class) {
            return (T) BigDecimal.ZERO;
        } else {
            throw new IllegalArgumentException("不支持的类型: " + clazz);
        }
    }

    /**
     * 获取指定年份中应统计的月份列表，格式为 yyyy-MM
     *
     * @param year 指定年份
     * @return 月份字符串列表（如 ["2024-01", "2024-02", ..., "2024-07"]）
     */
    public static List<String> getEffectiveMonthList(int year) {
        int currentYear = LocalDate.now().getYear();
        int endMonth;

        if (year < currentYear) {
            endMonth = 12;
        } else if (year == currentYear) {
            endMonth = LocalDate.now().getMonthValue();
        } else {
            return new ArrayList<>(); // 未来年份，无数据
        }

        List<String> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int month = 1; month <= endMonth; month++) {
            LocalDate date = LocalDate.of(year, month, 1);
            result.add(date.format(formatter));
        }

        return result;
    }
}
