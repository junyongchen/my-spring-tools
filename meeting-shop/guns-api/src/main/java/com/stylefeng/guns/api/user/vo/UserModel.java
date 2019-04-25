package com.stylefeng.guns.api.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/3 19:59
 * @description
 */
@Data
public class UserModel implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String address;
}
