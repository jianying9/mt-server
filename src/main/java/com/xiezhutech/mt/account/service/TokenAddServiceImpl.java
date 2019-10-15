package com.xiezhutech.mt.account.service;

import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.TokenEntity;
import com.xiezhutech.mt.account.local.AccountLocal;
import com.xiezhutech.mt.config.RouteNames;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.TOKEN_ADD_V1,
        requestConfigs = {},
        responseConfigs = {
            @ResponseConfig(name = "token", dataType = ResponseDataType.STRING, desc = "返回随机token"),
            @ResponseConfig(name = "expiredTime", dataType = ResponseDataType.LONG, desc = "过期时间")
        },
        responseCodes = {},
        pushRoutes = {},
        validateSession = false,
        desc = "获取一个临时的token",
        group = "账号")
public class TokenAddServiceImpl implements Service {

    @InjectLocalService
    private AccountLocal accountLocal;

    @Override
    public void execute(Request request, Response response) {
        TokenEntity tokenEntity = this.accountLocal.addToken();
        Map<String, Object> dataMap = EntityUtils.getMap(tokenEntity);
        response.setDataMap(dataMap);
    }
}
