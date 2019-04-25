package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
// todo 获取外部资源加需要捕获异常
@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MoocUserTMapper moocUserTMapper;

    @Override
    public int login(String userName, String password) {
        // todo 参数校验
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userName);
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);
        if (result.getUserPwd().equals(MD5Util.encrypt(password))) {
            return result.getUuid();
        }
        return 0;
    }

    /**
     *  用户注册
     * @param userModel
     * @return
     */
    @Override
    public boolean register(UserModel userModel) {
        // todo 参数校验
        // 将数据信息实体转为数据实体
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUserName());
        // 业务中密码不能明文存储，需要加密  【MD5混淆加密+盐值   -> shiro加密】
        moocUserT.setUserPwd(MD5Util.encrypt(userModel.getPassword()));
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setUserPhone(userModel.getPhone());
        moocUserT.setAddress(userModel.getAddress());
        // 将数据实体存入数据库
        Integer integer = moocUserTMapper.insert(moocUserT);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param userName
     * @return true表示用户名可用
     */
    @Override
    public boolean checkUserName(String userName) {
        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name", userName);
        Integer result = moocUserTMapper.selectCount(entityWrapper);
        if (result != null && result > 0) {
            return false;
        }
        return true;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        // todo 校验
        MoocUserT moocUserT = moocUserTMapper.selectById(uuid);
        UserInfoModel userInfoModel = do2UserInfo(moocUserT);
        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setNickName(userInfoModel.getNickName());
        moocUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setBeginTime(null);
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setUpdateTime(null);

        // 更新数据库
        Integer isSuccess = moocUserTMapper.updateById(moocUserT);
        if (isSuccess>0){
            // 根据id查询最新的用户信息
            UserInfoModel userInfo = getUserInfo(moocUserT.getUuid());
            return userInfo;
        }
        return userInfoModel;
    }

    private UserInfoModel do2UserInfo(MoocUserT moocUserT){
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setUserName(moocUserT.getUserName());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime().getTime());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setNickName(moocUserT.getNickName());
        userInfoModel.setLifeState(moocUserT.getLifeState()+"");
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setCreateTime(moocUserT.getBeginTime().getTime());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setBiography(moocUserT.getBiography());
        userInfoModel.setAddress(moocUserT.getAddress());
        return userInfoModel;
    }

    private Date time2Date(long time){
        Date date = new Date(time);
        return date;
    }
}
