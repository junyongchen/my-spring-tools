package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/9 19:42
 * @description
 */
@Data
public class CinemaQueryVO implements Serializable {
    /**
     * 影院编号
     */
    private Integer brandId;
    /**
     * 影厅类型
     */
    private Integer hallType;
    /**
     *  行政区编号
     */
    private Integer districtId;
    /**
     *  每页条数
     */
    private Integer pageSize;
    /**
     *  当前页数
     */
    private Integer nowPage;
}