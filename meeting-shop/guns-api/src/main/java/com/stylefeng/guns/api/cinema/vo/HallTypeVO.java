package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/10 9:21
 * @description
 */
@Data
public class HallTypeVO implements Serializable {
    private String halltypeId;
    private String halltypeName;
    private boolean isActive;
}
