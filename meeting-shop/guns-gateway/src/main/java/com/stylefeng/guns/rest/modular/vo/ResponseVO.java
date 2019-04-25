package com.stylefeng.guns.rest.modular.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author junyong.chen
 * @date 2019/4/3 20:24
 * @description 统一返回报文
 */
@Getter
@Setter
@ToString
public class ResponseVO<T> {
    // 返回状态 [ 0-成功，1-业务异常，999-系统异常]
    private int status;
    // 返回信息
    private String msg;
    // 返回数据实体
    private T data;
    // 图片前缀
    private String imgPre;
    // 分页参数
    private int nowPage;
    private int totalPage;

    private ResponseVO(){}

    /**
     *  成功
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ResponseVO success(T data){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(data);
        return responseVO;
    }

    public static<T> ResponseVO success(String imgPre,T data){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(data);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }

    public static<T> ResponseVO success(int nowPage, int totalPage, String imgPre,T data){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(data);
        responseVO.setImgPre(imgPre);
        responseVO.setNowPage(nowPage);
        responseVO.setTotalPage(totalPage);
        return responseVO;
    }

    /**
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResponseVO success(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return responseVO;
    }

    /**
     *  业务异常
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResponseVO serviceFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

    /**
     *  系统异常
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResponseVO appFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setMsg(msg);
        return responseVO;
    }
}
