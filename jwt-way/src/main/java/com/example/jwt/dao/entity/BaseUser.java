package com.example.jwt.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户基础信息表
 * </p>
 *
 * @author moxiaofei
 * @since 2022-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("base_user")
public class BaseUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "userId", type = IdType.AUTO)
    private Long userId;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 性别（男:male,女:female）
     */
    @TableField("gender")
    private String gender;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 房主用户ID
     */
    @TableField("deviceId")
    private String deviceId;

    /**
     * 记录标识
     */
    @TableField("flag")
    private Integer flag;

    /**
     * 记录创建时间
     */
    @TableField("gmt_created")
    private LocalDateTime gmtCreated;

    /**
     * 记录更新时间
     */
    @TableField("gmt_modify")
    private LocalDateTime gmtModify;


}
