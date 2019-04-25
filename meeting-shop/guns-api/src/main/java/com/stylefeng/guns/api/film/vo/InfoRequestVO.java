package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/9 16:05
 * @description
 */
@Data
public class InfoRequestVO implements Serializable {
    private String biography;
    private ActorRequestVO actors;
    private ImgVO imgs;
    private String filmId;
}
