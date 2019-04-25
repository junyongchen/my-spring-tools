package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;

public interface UserAPI {

    /**
     * 登录
     * @param userName
     * @param password
     * @return 返回用户id，用于在服务端中缓存判断用户是否为登录状态
     */
    int login(String userName, String password);

    /**
     * 注册
     * @param userModel
     * @return
     */
    boolean register(UserModel userModel);

    /**
     *  查询用户是否存在
     * @param userName
     * @return
     */
    boolean checkUserName(String userName);

    UserInfoModel getUserInfo(int uuid);

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
