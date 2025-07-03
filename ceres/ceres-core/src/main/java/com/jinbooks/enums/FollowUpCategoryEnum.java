package com.jinbooks.enums;

import lombok.Getter;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 16:41
 */

@Getter
public enum FollowUpCategoryEnum {
    LEAD(1, "线索"),

    CUSTOMER(2, "客户"),

    OPPORTUNITY(3, "商机"),

    CONTRACT(4, "合同"),

    AFTER_SALES(5, "售后");

    private final Integer code;

    private final String name;
    FollowUpCategoryEnum(Integer code , String name) {
        this.code = code;
        this.name = name;
    }
}
