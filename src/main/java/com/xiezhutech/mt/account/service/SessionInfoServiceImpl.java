package com.xiezhutech.mt.account.service;

import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.service.parameter.ThirdResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.SESSION_INFO_V1,
        requestConfigs = {},
        responseConfigs = {
            @ResponseConfig(name = "user", dataType = ResponseDataType.OBJECT, desc = "返回信息",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "用户id"),
                        @SecondResponseConfig(name = "nickName", dataType = ResponseDataType.STRING, desc = "用户昵称"),
                        @SecondResponseConfig(name = "mobile", dataType = ResponseDataType.CHINA_MOBILE, desc = "手机号"),
                        @SecondResponseConfig(name = "smallHeadUrl", dataType = ResponseDataType.STRING, desc = "小头像url"),
                        @SecondResponseConfig(name = "customer", dataType = ResponseDataType.BOOLEAN, desc = "是否是客户"),
                        @SecondResponseConfig(name = "roleList", dataType = ResponseDataType.STRING_ARRAY, desc = "角色集合"),
                        @SecondResponseConfig(name = "orgIdList", dataType = ResponseDataType.STRING_ARRAY, desc = "加入组织id集合"),
                        @SecondResponseConfig(name = "acl", dataType = ResponseDataType.OBJECT, desc = "操作权限",
                                thirdResponseConfigs = {
                                    @ThirdResponseConfig(name = "addProject", dataType = ResponseDataType.BOOLEAN, desc = "新增项目")
                                })
                    })
        },
        responseCodes = {},
        pushRoutes = {},
        validateSession = true,
        desc = "获取session信息",
        group = "账号")
public class SessionInfoServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        //
        Map<String, Object> dataMap = EntityUtils.getMap(userEntity);
        boolean customer = userEntity.isCustomer();
        dataMap.put("customer", customer);
        //
        Map<String, Object> aclMap = new HashMap();
        boolean addProject = false;
        if (userEntity.isCustomer() && userEntity.getOrgIdList().isEmpty()) {
            addProject = true;
        }
        aclMap.put("addProject", addProject);
        dataMap.put("acl", aclMap);
        response.setData("user", dataMap);
    }
}
