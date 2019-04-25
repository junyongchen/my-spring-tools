package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/8 15:31
 * @description
 */
@Data
public class SourceVO implements Serializable {
    private String sourceId;
    private String sourceName;
    private Boolean isActive;
}
