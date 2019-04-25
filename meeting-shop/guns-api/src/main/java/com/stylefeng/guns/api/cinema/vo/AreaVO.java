package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/10 9:19
 * @description
 */
@Data
public class AreaVO implements Serializable {
    private String areaId;
    private String areaName;
    private boolean isActive;
}
