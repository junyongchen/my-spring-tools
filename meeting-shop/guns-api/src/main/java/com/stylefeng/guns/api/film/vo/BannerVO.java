package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/7 19:31
 * @description
 */
@Data
public class BannerVO implements Serializable {
    private String bannerId;
    private String bannerAddress;
    private String bannerUrl;
}
