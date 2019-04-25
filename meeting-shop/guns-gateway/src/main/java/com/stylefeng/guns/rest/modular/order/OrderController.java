package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.alipay.AliPayServiceAPI;
import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/11 15:37
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    @Reference(interfaceClass = OrderServiceAPI.class, check = false)
    private OrderServiceAPI orderServiceAPI;
    @Reference(interfaceClass = AliPayServiceAPI.class, check = false)
    private AliPayServiceAPI aliPayServiceAPI;

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @PostMapping("buyTickets")
    public ResponseVO buyTickets(Integer fieldId, String soldSeats, String seatsName) {
        try {
            // 验证售出的票是否有效   todo 参数类型，很多使用了String，是否可以改为其他基本类型？ 在Service中使用字符串类型参数可以省略其他类型的转换？二进制啥的
            boolean isTrue = orderServiceAPI.isTrueSeats(fieldId + "", soldSeats);
            // 验证是否包含已售出的票（在订单中验证）
            boolean isNorSold = orderServiceAPI.isNotSoldSeats(fieldId + "", soldSeats);

            if (isTrue && isNorSold) {
                // 创建订单信息(需要获取登录人)
                String userId = CurrentUser.getCurrentUser();
                if (StringUtils.isBlank(userId)) {
                    return ResponseVO.serviceFail("用户未登录");
                }
                OrderVO orderVO = orderServiceAPI.saveOrderInfo(fieldId, soldSeats, seatsName, Integer.parseInt(userId));
                if (orderVO == null) {
                    log.error("购票业务异常");
                    return ResponseVO.serviceFail("购票业务异常");
                }
                return ResponseVO.success(orderVO);
            }
            return ResponseVO.serviceFail("订单中的座位编号有问题");
        } catch (Exception e) {
            log.error("购票业务异常", e);
            return ResponseVO.serviceFail("购票业务异常");
        }
    }

    @PostMapping("getOrderInfo")
    public ResponseVO getOrderInfo(@RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize) {
        // 获取当前登录人的信息
        String userId = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(userId)) {
            return ResponseVO.serviceFail("用户未登录");
        }
        // 使用当前登录人获取历史订单
        Page<OrderVO> page = new Page<>(nowPage, pageSize);
        Page<OrderVO> result = orderServiceAPI.getOrdersByUserId(Integer.parseInt(userId), page);
        return ResponseVO.success(nowPage, (int) result.getTotal(), "", result.getRecords());
    }

    @PostMapping("getPayInfo")
    public ResponseVO getPayInfo(@RequestParam("orderId") String orderId) {
        String userId = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(userId)) {
            return ResponseVO.serviceFail("用户未登录");
        }
        AliPayInfoVO aliPayInfo = aliPayServiceAPI.getQRCode(orderId);
        return ResponseVO.success(IMG_PRE, aliPayInfo);
    }

    /**
     *
     * @param orderId 订单编号
     * @param tryNums 重试次数，默认1，4超时
     * @return
     */
    @PostMapping("getPayResult")
    public ResponseVO getPayResult(@RequestParam("orderId") String orderId,
                                   @RequestParam(name = "tryNums", required = false, defaultValue = "1") Integer tryNums) {
        String userId = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(userId)) {
            return ResponseVO.serviceFail("用户未登录");
        }
        if (tryNums >= 4) {
            return ResponseVO.serviceFail("订单支付失败，请稍后重试");
        }
        // todo 加熔断器？
        // todo 需要判断用户查询的订单是否属于自己
        // 将当前登录人信息传给后端
        RpcContext.getContext().setAttachment("userId", userId);
        AliPayResultVO aliPayResult = aliPayServiceAPI.getOrderStatus(orderId);
        if(aliPayResult == null || StringUtils.isBlank(aliPayResult.getOrderId())) {
            AliPayResultVO aliPayResultVO = new AliPayResultVO();
            aliPayResultVO.setOrderId(orderId);
            aliPayResultVO.setOrderStatus(0);
            aliPayResultVO.setOrderMsg("支付不成功");
            return ResponseVO.success(aliPayResultVO);
        }
        return ResponseVO.success(aliPayResult);
    }
}
