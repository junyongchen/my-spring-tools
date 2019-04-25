package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.FilmInfoVO;
import com.stylefeng.guns.api.film.vo.FilmVO;
import lombok.Data;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/7 19:28
 * @description
 */
@Data
public class FilmIndexVO {
    private List<BannerVO> banners;
    private FilmVO hotFilms;
    private FilmVO soonFilms;
    private List<FilmInfoVO> boxRanking;
    private List<FilmInfoVO> expectRanking;
    private List<FilmInfoVO> top100;
}
