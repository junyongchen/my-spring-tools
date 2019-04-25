package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.UUIDUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/11 16:35
 * @description
 */
@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class)
public class DefaultOrderServiceImpl implements OrderServiceAPI {

    @Autowired
    private MoocOrderTMapper moocOrderTMapper;
    @Autowired
    private FTPUtil ftpUtil;
    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    /**
     * 验证售出的票是否有效
     *
     * @param fieldId
     * @param seats
     * @return
     */
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        // 根据fieldId获取对应位置的路径图
        String seatsPath = moocOrderTMapper.getSeatsByFieldId(fieldId);
        log.info("文件地址是：{}",seatsPath);
        String fileStrByAddress = ftpUtil.getFileStrByAddress(seatsPath);
        log.info(fileStrByAddress);
        // 将字符串转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);

        String ids = jsonObject.get("ids").toString();
        String[] seatArray = seats.split(",");
        String[] idArray = ids.split(",");
        // todo 校验算法似乎有问题：如果参数数组两个值相等并且可以匹配，那么数量是对的，但是不存在两个一样的座位
        int count = 0;
        for (String id : idArray) {
            for (String seat : seatArray) {
                if (StringUtils.equals(id, seat)) {
                    count++;
                }
            }
        }
        if (count == seatArray.length) {
            return true;
        }
        return false;
    }

    /**
     * 验证是否包含已售出的票（在订单中验证）
     *
     * @param fieldId
     * @param seats
     * @return
     */
    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id", fieldId);
        List<MoocOrderT> list = moocOrderTMapper.selectList(entityWrapper);
        String[] seatArray = seats.split(",");
        // 有匹配上则false
        for (MoocOrderT moocOrderT : list) {
            String[] ids = moocOrderT.getSeatsIds().split(",");
            for (String seat : seatArray) {
                for (String id : ids) {
                    if (StringUtils.equals(seat, id)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 创建订单信息
     *
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return
     */
    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        // 订单编号，一般不用自增，使用分布式
        String uuid= UUIDUtil.genUuid();
        // 影片信息
        FilmInfoVO filmInfo = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
        Integer filmId = Integer.parseInt(filmInfo.getFilmId());
        // 影院信息
        OrderQueryVO orderQueryVO= cinemaServiceAPI.getOrderNeeds(fieldId);
        Integer cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        Double filmPrice = Double.parseDouble(orderQueryVO.getFilmPrice());
        int soldNum = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(soldNum, filmPrice);
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(uuid);
        moocOrderT.setCinemaId(cinemaId);
        moocOrderT.setFieldId(fieldId);
        moocOrderT.setFilmId(filmId);
        moocOrderT.setFilmPrice(filmPrice);
        moocOrderT.setOrderPrice(totalPrice);
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setOrderUser(userId);
        moocOrderT.setSeatsName(seatsName);
        Integer insert = moocOrderTMapper.insert(moocOrderT);
        if (insert<1){
            log.error("订单信息插入失败：{}",moocOrderT.toString());
            return null;
        }
        OrderVO orderVO = moocOrderTMapper.getOrderInfoById(uuid);
        if (orderVO == null){
            log.error("订单信息查询失败，订单编号为：{}", uuid);
            return null;
        }
        return orderVO;
    }

    private double getTotalPrice(int solds, double price){
        BigDecimal soldDeci = new BigDecimal(solds);
        BigDecimal priceDeci = BigDecimal.valueOf(price);
        BigDecimal result = soldDeci.multiply(priceDeci);
        // 保留两位，四舍五入
        BigDecimal bigDecimal = result.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * 使用当前登录人获取历史订单
     *
     * @param userId
     * @return
     */
    @Override
    public Page<OrderVO> getOrdersByUserId(Integer userId, Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if (userId == null) {
            log.error("订单业务查询失败，用户编号未传入");
            return null;
        }
        List<OrderVO> orderList = moocOrderTMapper.getOrdersByUserId(userId, page);
        if (! orderList.isEmpty()){
            EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("order_user", userId);
            Integer count = moocOrderTMapper.selectCount(entityWrapper);
            result.setTotal(count);
            result.setRecords(orderList);
            return result;
        }
        result.setTotal(0);
        result.setRecords(Collections.EMPTY_LIST);
        return result;
    }

    /**
     * 根据fieldId获取所有已经销售的座位编号
     *
     * @param fieldId
     * @return
     */
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if (fieldId == null) {
            log.error("未传入任何场次编号");
            return "";
        }
        String soldSeatsByField = moocOrderTMapper.getSoldSeatsByField(fieldId);
        return soldSeatsByField;
    }

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderVO getOrderInfoById(String orderId) {
        // todo 需要在这里判断用户是否有权查看这份订单记录
        String userId = RpcContext.getContext().getAttachment("userId");
        OrderVO orderInfoById = moocOrderTMapper.getOrderInfoById(orderId);
        return orderInfoById;
    }

    /**
     * 订单状态置为支付成功
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean paySuccess(String orderId) {
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(orderId);
        // todo 要用枚举
        moocOrderT.setOrderStatus(1);
        Integer result = moocOrderTMapper.updateById(moocOrderT);
        return result>0;
    }

    /**
     * 订单状态置为支付失败
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean payFail(String orderId) {
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(orderId);
        // todo 要用枚举
        moocOrderT.setOrderStatus(2);
        Integer result = moocOrderTMapper.updateById(moocOrderT);
        return result>0;
    }
}
