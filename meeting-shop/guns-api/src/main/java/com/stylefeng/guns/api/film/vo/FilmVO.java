package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/7 19:33
 * @description
 */
@Data
public class FilmVO implements Serializable {
    private Integer filmNum;
    private Integer nowPage;
    private int totalPage;
    private List<FilmInfoVO> filmInfo;
}
