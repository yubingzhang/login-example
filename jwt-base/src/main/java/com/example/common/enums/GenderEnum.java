package com.example.common.enums;

import lombok.Getter;

/**
 * 性别枚举
 *
 * @author zhangubing
 */
public enum GenderEnum {
    /**
     * 女性
     */
    FEMALE("female"),

    /**
     * 男性
     */
    MALE("male");

    @Getter
    private final String name;


    GenderEnum(String name) {
        this.name = name;
    }

}