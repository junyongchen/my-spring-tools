package com.stylefeng.guns.api.order;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.order.vo.OrderVO;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/11 16:14
 * @description
 */
public interface OrderServiceAPI {

    /**
     *  验证售出的票是否有效
     * @param fieldId
     * @param seats
     * @return
     */
    boolean isTrueSeats(String fieldId, String seats);

    /**
     *  验证是否包含已售出的票（在订单中验证）
     * @param fieldId
     * @param seats
     * @return
     */
    boolean isNotSoldSeats(String fieldId, String seats);

    /**
     *  创建订单信息
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return
     */
    OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId);

    /**
     *  使用当前登录人获取历史订单
     * @param userId
     * @return
     */
    Page<OrderVO> getOrdersByUserId(Integer userId, Page<OrderVO> page);

    /**
     *  根据fieldId获取所有已经销售的座位编号
     * @param fieldId
     * @return
     */
    String getSoldSeatsByFieldId(Integer fieldId);

    /**
     *  根据订单编号查询订单信息
     * @param orderId
     * @return
     */
    OrderVO getOrderInfoById(String orderId);

    /**
     *  订单状态置为支付成功。不使用updatePayStatus(String orderId, Integer status)接口，对外隐藏敏感属性
     * @param orderId
     * @return
     */
    boolean paySuccess(String orderId);

    /**
     *  订单状态置为支付失败
     * @param orderId
     * @return
     */
    boolean payFail(String orderId);
}
