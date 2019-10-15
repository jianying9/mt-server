package com.xiezhutech.mt.account.service;

import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.ResponseCode;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.SessionHandleType;
import com.wolf.framework.service.parameter.RequestConfig;
import com.wolf.framework.service.parameter.RequestDataType;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.TokenEntity;
import com.xiezhutech.mt.account.local.AccountLocal;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.TOKEN_LOGIN_V1,
        requestConfigs = {
            @RequestConfig(name = "token", dataType = RequestDataType.STRING, desc = "token")
        },
        responseConfigs = {
            @ResponseConfig(name = "user", dataType = ResponseDataType.OBJECT, desc = "返回信息",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "用户id"),
                        @SecondResponseConfig(name = "nickName", dataType = ResponseDataType.STRING, desc = "用户昵称"),
                        @SecondResponseConfig(name = "mobile", dataType = ResponseDataType.CHINA_MOBILE, desc = "手机号"),
                        @SecondResponseConfig(name = "smallHeadUrl", dataType = ResponseDataType.STRING, desc = "小头像url"),
                        @SecondResponseConfig(name = "customer", dataType = ResponseDataType.BOOLEAN, desc = "是否是客户"),
                        @SecondResponseConfig(name = "newSid", dataType = ResponseDataType.STRING, desc = "新的会话id")
                    })},
        responseCodes = {
            @ResponseCode(code = "token_error", desc = "已过期,请刷新页面重试"),
            @ResponseCode(code = "token_checking", desc = "请登录手机客户端验证")
        },
        pushRoutes = {},
        validateSession = false,
        sessionHandleType = SessionHandleType.SAVE,
        desc = "使用token登录",
        group = "账号")
public class TokenLoginServiceImpl implements Service {

    @InjectLocalService
    private UserLocal userLocal;

    @InjectLocalService
    private AccountLocal accountLocal;

    @Override
    public void execute(Request request, Response response) {
        String token = request.getStringValue("token");
        TokenEntity tokenEntity = this.accountLocal.queryToken(token);
        if (tokenEntity != null && tokenEntity.getExpiredTime() >= System.currentTimeMillis()) {
            //token存在且未过期,判断token是否绑定用户信息
            long userId = tokenEntity.getUserId();
            if (userId == 0) {
                //未绑定用户,验证中
                response.setCode("token_checking");
            } else {
                UserEntity userEntity = this.userLocal.queryUser(userId);
                if (userEntity != null) {
                    String newSid = this.accountLocal.addOtherSession(userId);
                    this.userLocal.updateUserOtherSid(userId, newSid);
                    response.setNewSessionId(newSid);
                    //
                    Map<String, Object> dataMap = EntityUtils.getMap(userEntity);
                    boolean customer = userEntity.isCustomer();
                    dataMap.put("customer", customer);
                    dataMap.put("newSid", newSid);
                    response.setData("user", dataMap);
                } else {
                    response.setCode("token_error");
                }
            }
        } else {
            response.setCode("token_error");
        }
    }
}
