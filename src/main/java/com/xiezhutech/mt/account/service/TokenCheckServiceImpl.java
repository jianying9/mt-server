package com.xiezhutech.mt.account.service;

import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.ResponseCode;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.RequestConfig;
import com.wolf.framework.service.parameter.RequestDataType;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.account.entity.TokenEntity;
import com.xiezhutech.mt.config.RouteNames;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.TOKEN_CHECK_V1,
        requestConfigs = {
            @RequestConfig(name = "token", dataType = RequestDataType.STRING, desc = "token")
        },
        responseConfigs = {},
        responseCodes = {
            @ResponseCode(code = "token_error", desc = "已过期,请刷新页面在登录")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "手机验证当前用户其它环境登录请求",
        group = "账号")
public class TokenCheckServiceImpl extends AbstractSessionService implements Service {

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        String token = request.getStringValue("token");
        TokenEntity tokenEntity = this.accountLocal.queryToken(token);
        if (tokenEntity != null && tokenEntity.getExpiredTime() >= System.currentTimeMillis()) {
            //token有效,为token绑定用户信息
            long userId = sessionEntity.getUserId();
            this.accountLocal.updateToken(token, userId);
        } else {
            response.setCode("token_error");
        }
    }
}
