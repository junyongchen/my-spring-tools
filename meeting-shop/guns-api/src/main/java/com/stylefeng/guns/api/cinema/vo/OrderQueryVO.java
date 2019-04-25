package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/13 10:44
 * @description
 */
@Data
public class OrderQueryVO implements Serializable {
    private String cinemaId;
    private String filmPrice;
}
