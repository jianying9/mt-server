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
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.account.local.AccountLocal;
import com.xiezhutech.mt.account.local.SessionTypes;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.SESSION_LOGIN_V1,
        requestConfigs = {
            @RequestConfig(name = "oldSid", dataType = RequestDataType.STRING, desc = "sid")
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
            @ResponseCode(code = "sid_not_exist", desc = "登录失败,会话id不存在"),
            @ResponseCode(code = "sid_timeout", desc = "登录失败,会话id超期")
        },
        pushRoutes = {},
        validateSession = false,
        sessionHandleType = SessionHandleType.SAVE,
        desc = "通过sid登录",
        group = "账号")
public class SessionLoginServiceImpl implements Service {

    @InjectLocalService
    private UserLocal userLocal;

    @InjectLocalService
    private AccountLocal accountLocal;

    private final long mobileTimeLimit = 2592000000L;

    private final long otherTimeLimit = 86400000L;

    @Override
    public void execute(Request request, Response response) {
        String oldSid = request.getStringValue("oldSid");
        SessionEntity sessionEntity = this.accountLocal.querySession(oldSid);
        if (sessionEntity != null) {
            //session存在,判断是否超期
            long thisTime = System.currentTimeMillis();
            boolean sidTimeout = false;
            long time = thisTime - sessionEntity.getLastTime();
            if (sessionEntity.getType().equals(SessionTypes.ACCOUNT_TYPE_MOBILE)) {
                if (time > this.mobileTimeLimit) {
                    sidTimeout = true;
                }
            } else if (time > this.otherTimeLimit) {
                sidTimeout = true;
            }
            if (sidTimeout == false) {
                //未超期
                long userId = sessionEntity.getUserId();
                UserEntity userEntity = this.userLocal.queryUser(userId);
                //更新session时间
                this.accountLocal.updateSessionLastTime(oldSid, thisTime);
                response.setNewSessionId(oldSid);
                //
                Map<String, Object> dataMap = EntityUtils.getMap(userEntity);
                boolean customer = userEntity.isCustomer();
                dataMap.put("customer", customer);
                dataMap.put("newSid", oldSid);
                response.setData("user", dataMap);
            } else {
                response.setCode("sid_timeout");
            }
        } else {
            response.setCode("sid_not_exist");
        }
    }
}
