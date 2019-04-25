package com.stylefeng.guns.api.alipay;

import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;

/**
 * @author junyong.chen
 * @date 2019/4/15 17:03
 * @description
 */
public interface AliPayServiceAPI {

    /**
     *  获取二维码
     * @param orderId
     * @return
     */
    AliPayInfoVO getQRCode(String orderId);

    /**
     *  获取订单支付结果
     * @param orderId
     * @return
     */
    AliPayResultVO getOrderStatus(String orderId);
}
