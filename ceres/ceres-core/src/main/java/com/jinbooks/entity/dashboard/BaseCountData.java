package com.jinbooks.entity.dashboard;

import lombok.*;

import java.util.List;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/06/29 13:20:42
 * {@code @version} 1.0
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseCountData<T> {
    /**
     * 标题
     */
    private String title;

    /**
     * 值
     */
    private T value;

    /**
     * 趋势
     */
    private double trend;

    /**
     * 数据
     */
    private List<BaseGroupVo<T>> data;
}
