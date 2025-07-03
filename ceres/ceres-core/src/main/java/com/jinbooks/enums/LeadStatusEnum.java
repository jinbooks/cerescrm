package com.jinbooks.enums;


import lombok.Getter;

@Getter
public enum LeadStatusEnum {

    NEW_CLUE(1, "新线索"),

    ALREADY_CONTACT(2, "已联系"),

    INTENTION_CLUE(3, "有意向"),

    TRANSFERRED_CUSTOMERS(4, "已转商机"),

    LOST_CLUE(5, "已流失");

    private final Integer code;

    private final String name;
    LeadStatusEnum(Integer code , String name) {
        this.code = code;
        this.name = name;
    }
}
