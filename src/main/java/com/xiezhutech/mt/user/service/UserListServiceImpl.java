package com.xiezhutech.mt.user.service;

import com.wolf.framework.local.InjectLocalService;
import com.xiezhutech.mt.account.service.*;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.ResponseCode;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import com.xiezhutech.mt.user.local.UserRoles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.USER_LIST_V1,
        requestConfigs = {},
        responseConfigs = {
            @ResponseConfig(name = "userArray", dataType = ResponseDataType.OBJECT_ARRAY, desc = "用户集合",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "id"),
                        @SecondResponseConfig(name = "userName", dataType = ResponseDataType.STRING, desc = "名称"),
                        @SecondResponseConfig(name = "headUrl", dataType = ResponseDataType.STRING, desc = "头像"),
                        @SecondResponseConfig(name = "mobile", dataType = ResponseDataType.STRING, desc = "手机号"),
                        @SecondResponseConfig(name = "roleList", dataType = ResponseDataType.STRING_ARRAY, desc = "角色")
                    }),
            @ResponseConfig(name = "act", dataType = ResponseDataType.OBJECT, desc = "权限",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "canAddUser", dataType = ResponseDataType.BOOLEAN, desc = "是否可以新增用户")
                    })
        },
        responseCodes = {
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "用户列表查询(材料中心)",
        group = "用户")
public class UserListServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private UserLocal userLocal;

    private List<String> getRoleList(List<String> roleList) {
        List<String> resultList = new ArrayList(roleList.size());
        for (String role : roleList) {
            switch (role) {
                case UserRoles.ADMIN:
                    resultList.add("管理员");
                    break;
                case UserRoles.PICKED:
                    resultList.add("收件员");
                    break;
            }
        }
        return resultList;
    }

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer() == false) {
            List<UserEntity> userEntityList = this.userLocal.searchUser(false, 200);
            List<Map<String, Object>> userMapList = new ArrayList(userEntityList.size());
            Map<String, Object> userMap;
            List<String> roleList;
            for (UserEntity uEntity : userEntityList) {
                userMap = EntityUtils.getMap(uEntity);
                roleList = this.getRoleList(uEntity.getRoleList());
                userMap.put("roleList", roleList);
                userMapList.add(userMap);
            }
            //
            boolean canAddUser = false;
            if (userEntity.getRoleList().contains(UserRoles.ADMIN)) {
                canAddUser = true;
            }
            Map<String, Object> aclMap = new HashMap();
            aclMap.put("canAddUser", canAddUser);
            //

            Map<String, Object> responseMap = new HashMap();
            responseMap.put("userArray", userMapList);
            responseMap.put("act", aclMap);
            response.setDataMap(responseMap);
        } else {
            response.setCode("customer");
        }
    }
}
