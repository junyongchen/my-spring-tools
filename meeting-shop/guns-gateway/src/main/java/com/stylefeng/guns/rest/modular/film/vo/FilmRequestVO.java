package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

/**
 * @author junyong.chen
 * @date 2019/4/8 19:50
 * @description
 */
@Data
public class FilmRequestVO {
    /**
     * 1-正在热映，2-即将上映，3-经典影片
     */
    private Integer showType=1;
    /**
     * 1-按热门搜索，2-按时间搜索，3-按评价搜索
     */
    private Integer sortId=1;
    /**
     *  类型编号
     */
    private Integer catId=99;
    /**
     *  区域编号
     */
    private Integer sourceId=99;
    /**
     *  年代编号
     */
    private Integer yearId=99;
    /**
     *  当前页
     */
    private Integer nowPage=1;
    /**
     *  每页显示条数
     */
    private Integer pageSize=18;
}
