package com.stylefeng.guns.api.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/3 19:59
 * @description
 */
@Data
public class UserInfoModel implements Serializable {
    private Integer uuid;
    private String userName;
    private String nickName;
    private String email;
    private String phone;
    private Integer sex;
    private String birthday;
    private String lifeState;
    private String biography;
    private String address;
    private String headAddress;
    private Long createTime;
    private Long updateTime;
}
