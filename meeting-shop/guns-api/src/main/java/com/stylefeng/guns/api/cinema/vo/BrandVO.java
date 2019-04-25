package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/10 9:17
 * @description
 */
@Data
public class BrandVO implements Serializable {
    private String brandId;
    private String brandName;
    private boolean isActive;
}
