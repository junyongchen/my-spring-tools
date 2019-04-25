package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/8 15:33
 * @description
 */
@Data
public class YearVO implements Serializable {
    private String yearId;
    private String yearName;
    private Boolean isActive;
}
