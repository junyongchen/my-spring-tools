package com.stylefeng.guns.api.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.AreaVO;
import com.stylefeng.guns.api.cinema.vo.BrandVO;
import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.CinemaQueryVO;
import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallTypeVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/9 19:41
 * @description
 */
public interface CinemaServiceAPI {

    /**
     *  查询影院列表
     * @param cinemaQueryVO
     * @return
     */
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);

    /**
     *  根据条件获取品牌列表
     * @param brandId
     * @return
     */
    List<BrandVO> getBrands(int brandId);

    /**
     *  根据条件获取行政区域列表
     * @param areaId
     * @return
     */
    List<AreaVO> getAreas(int areaId);

    /**
     *  获取影厅类型列表
     * @param hallType
     * @return
     */
    List<HallTypeVO> getHallType(int hallType);

    /**
     *  根据影院编号获取影院信息
     * @param cinemaId
     * @return
     */
    CinemaInfoVO getCinemaInfoById(int cinemaId);

    /**
     *  根据影院编号获取所有电影信息和对应的放映场次信息
     * @param cinemaId
     * @return
     */
    List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId);

    /**
     *  根据放映场次id获取放映信息
     * @param fieldId
     * @return
     */
    HallInfoVO getFilmFieldInfo(int fieldId);

    /**
     *  根据放映场次查询播放电影的编号，根据电影编号查询对应电影的信息
     * @param fieldId
     * @return
     */
    FilmInfoVO getFilmInfoByFieldId(int fieldId);

    /**
     *  订单模块所需信息
     * @param fieldId
     * @return
     */
    OrderQueryVO getOrderNeeds(int fieldId);
}
