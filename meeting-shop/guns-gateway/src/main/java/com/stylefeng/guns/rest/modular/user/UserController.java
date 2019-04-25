package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author junyong.chen
 * @date 2019/4/4 19:59
 * @description
 */
@RequestMapping("/user/")
@RestController
public class UserController {

    private static final Log logger = LogFactory.getLog(UserController.class);

    @Reference(interfaceClass = UserAPI.class, check = false)
    private UserAPI userAPI;

    @PostMapping("register")
    public ResponseVO register(UserModel userModel) {
        if (StringUtils.isBlank(userModel.getUserName())) {
            return ResponseVO.serviceFail("用户名不能为空");
        }
        if (StringUtils.isBlank(userModel.getPassword())) {
            return ResponseVO.serviceFail("密码不能为空");
        }

        boolean isSuccess = userAPI.register(userModel);
        if (isSuccess) {
            return ResponseVO.success("注册成功");
        } else {
            return ResponseVO.serviceFail("注册失败");
        }
    }

    @PostMapping("check")
    public ResponseVO check(String username) {
        if (StringUtils.isBlank(username)) {
            return ResponseVO.serviceFail("用户名不能为空");
        }
        boolean notExists = userAPI.checkUserName(username);
        if (notExists) {
            return ResponseVO.success("用户名不存在");
        } else {
            return ResponseVO.serviceFail("用户名已存在");
        }
    }

    @GetMapping("logout")
    public ResponseVO logout() {
        /*
        应用
        1.前端存储jwt 7天：jwt的刷新（用户修改密码需要刷新）
        2.服务端存储活动用户信息 30分钟
        3. jwt里的userId为key，查找活跃用户
        退出
        1.前端删除jwt
        2.后端服务器删除jwt缓存
        现状
        前端删除jwt
        */
        return ResponseVO.success("退出成功");
    }

    @GetMapping("getUserInfo")
    public ResponseVO getUserInfo() {
        String userId = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(userId)) {
            return ResponseVO.serviceFail("用户未登录");
        }
        int uuid = Integer.parseInt(userId);
        UserInfoModel userInfoModel = userAPI.getUserInfo(uuid);
        if (userInfoModel != null) {
            return ResponseVO.success(userInfoModel);
        }
        return ResponseVO.serviceFail("用户信息查询失败");
    }

    @PostMapping("updateUserInfo")
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel) {
        String userId = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(userId)) {
            return ResponseVO.serviceFail("用户未登录");
        }
        if (!StringUtils.equals(userId, userInfoModel.getUuid().toString())) {
            return ResponseVO.serviceFail("请修改您地个人信息");
        }
        UserInfoModel userInfo = userAPI.updateUserInfo(userInfoModel);
        if (userInfo != null) {
            return ResponseVO.success(userInfo);
        }
        return ResponseVO.serviceFail("用户信息修改失败");
    }
}
