package com.jinbooks.entity.lead.vo;

import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/18 18:06
 */

@Data
public class LeadStatisticsVo {
    //总线索数
    private Long leadNums = 0L;

    // 有意向线索数量
    private Long intentionCount = 0L;

    // 待跟进线索数量
    private Long pendingCount = 0L;

    // 已转化线索数量
    private Long convertedCount = 0L;
}
