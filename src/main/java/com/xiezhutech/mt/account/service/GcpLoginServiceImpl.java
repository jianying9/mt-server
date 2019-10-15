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
import com.xiezhutech.mt.account.entity.AccountEntity;
import com.xiezhutech.mt.account.local.AccountLocal;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.thirdparty.local.GcpLocal;
import com.xiezhutech.mt.thirdparty.local.GcpUserInfo;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.GCP_LOGIN_V1,
        requestConfigs = {
            @RequestConfig(name = "authCode", dataType = RequestDataType.STRING, desc = "授权码")
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
                    })
        },
        responseCodes = {
            @ResponseCode(code = "auth_code_error", desc = "授权失败,请退出重新授权")
        },
        pushRoutes = {},
        validateSession = false,
        sessionHandleType = SessionHandleType.SAVE,
        desc = "通过工程派用户登录",
        group = "账号")
public class GcpLoginServiceImpl implements Service {

    @InjectLocalService
    private UserLocal userLocal;

    @InjectLocalService
    private AccountLocal accountLocal;

    @InjectLocalService
    private GcpLocal gcpLocal;

    @Override
    public void execute(Request request, Response response) {
        String authCode = request.getStringValue("authCode");
        GcpUserInfo gcpUserInfo = this.gcpLocal.getGcpUserInfo(authCode);
        if (gcpUserInfo != null) {
            UserEntity userEntity = null;
            //查询本地是否有关联账号
            String mobile = gcpUserInfo.getMobile();
            AccountEntity accountEntity = this.accountLocal.queryAccount(mobile);
            if (accountEntity != null) {
                //已经关联,直接登录
                userEntity = this.userLocal.queryUser(accountEntity.getUserId());
            }
            //如果不存在关联账号,则重新注册
            if (userEntity == null) {
                //注册新用户
                long gcpUserId = gcpUserInfo.getUserId();
                Map<String, Object> userMap = new HashMap();
                userMap.put("gcpUserId", gcpUserId);
                String userName = gcpUserInfo.getUserName();
                if (userName.isEmpty()) {
                    userName = gcpUserInfo.getNickName();
                }
                userMap.put("userName", userName);
                userMap.put("mobile", gcpUserInfo.getMobile());
                userMap.put("headUrl", gcpUserInfo.getHeadUrl());
                userMap.put("gcpUserId", gcpUserId);
                userEntity = this.userLocal.addCustomerUser(userMap);
                //注册账号
                Map<String, Object> accountMap = new HashMap();
                accountMap.put("account", userEntity.getMobile());
                accountMap.put("userId", userEntity.getUserId());
                accountMap.put("createTime", userEntity.getCreateTime());
                this.accountLocal.addAccount(accountMap);
            } else {
                //绑定gcp账号
                long gcpUserId = gcpUserInfo.getUserId();
                Map<String, Object> userMap = new HashMap();
                userMap.put("gcpUserId", gcpUserId);
                userMap.put("userId", userEntity.getUserId());
                this.userLocal.updateUser(userMap);
            }
            //登录
            String newSid = this.accountLocal.addMobileSession(userEntity.getUserId());
            this.userLocal.updateUserMobileSid(userEntity.getUserId(), newSid);
            response.setNewSessionId(newSid);
            //
            Map<String, Object> dataMap = EntityUtils.getMap(userEntity);
            boolean customer = userEntity.isCustomer();
            dataMap.put("customer", customer);
            dataMap.put("newSid", newSid);
            response.setData("user", dataMap);
        } else {
            response.setCode("auth_code_error");
        }
    }
}
