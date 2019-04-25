package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/8 15:30
 * @description
 */
@Data
public class CatVO implements Serializable {
    private String catId;
    private String catName;
    private Boolean isActive;
}
