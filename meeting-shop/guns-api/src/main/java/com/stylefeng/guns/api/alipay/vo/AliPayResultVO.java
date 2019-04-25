package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/15 17:07
 * @description
 */
@Data
public class AliPayResultVO implements Serializable {
    private String orderId;
    private Integer orderStatus;
    private String orderMsg;
}
