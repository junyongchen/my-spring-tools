package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import lombok.Data;

/**
 * @author junyong.chen
 * @date 2019/4/10 20:22
 * @description
 */
@Data
public class CinemaFieldResponseVO {
    private FilmInfoVO filmInfo;
    private CinemaInfoVO cinemaInfo;
    private HallInfoVO hallInfo;
}
