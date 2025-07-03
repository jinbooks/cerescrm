package com.jinbooks.entity.dashboard;

import lombok.*;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/06/29 13:33:19
 * {@code @version} 1.0
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseGroupVo<T> {

    private String name;
    private T value;

}
