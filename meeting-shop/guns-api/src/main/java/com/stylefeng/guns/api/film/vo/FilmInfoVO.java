package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/7 19:39
 * @description
 */
@Data
public class FilmInfoVO implements Serializable {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
    private Integer expectNum;
    private String showTime;
    private Integer boxNum;
    private String score;
}
