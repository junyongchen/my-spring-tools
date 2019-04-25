package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/9 10:44
 * @description
 */
@Data
public class FilmDescVO implements Serializable {
    private String biography;
    private String filmId;
}
