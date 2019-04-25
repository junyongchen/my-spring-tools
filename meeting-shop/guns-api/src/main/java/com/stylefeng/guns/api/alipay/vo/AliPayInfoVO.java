package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junyong.chen
 * @date 2019/4/15 17:04
 * @description
 */
@Data
public class AliPayInfoVO implements Serializable {
    private String orderId;
    private String QRCodeAddress;
}
