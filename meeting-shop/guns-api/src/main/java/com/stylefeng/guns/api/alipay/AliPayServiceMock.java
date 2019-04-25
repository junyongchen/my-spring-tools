package com.stylefeng.guns.api.alipay;

import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;

/**
 * @author junyong.chen
 * @date 2019/4/16 15:41
 * @description 本地伪装，业务降级方法，用于服务降级。出现RpcException时会被执行，例如远程调用超时
 */
public class AliPayServiceMock implements AliPayServiceAPI {
    /**
     * 获取二维码
     *
     * @param orderId
     * @return
     */
    @Override
    public AliPayInfoVO getQRCode(String orderId) {
        return null;
    }

    /**
     * 获取订单支付结果
     *
     * @param orderId
     * @return
     */
    @Override
    public AliPayResultVO getOrderStatus(String orderId) {
        AliPayResultVO aliPayResultVO = new AliPayResultVO();
        aliPayResultVO.setOrderId(orderId);
        // todo 用枚举
        aliPayResultVO.setOrderStatus(0);
        aliPayResultVO.setOrderMsg("尚未支付成功");
        return aliPayResultVO;
    }
}
