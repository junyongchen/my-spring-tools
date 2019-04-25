package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import lombok.Data;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/10 19:50
 * @description
 */
@Data
public class CinemaResponseVO {
    private List<CinemaVO> cinemas;
}
