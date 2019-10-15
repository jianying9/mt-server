package com.xiezhutech.mt.user.service;

import com.wolf.framework.local.InjectLocalService;
import com.xiezhutech.mt.account.service.*;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.ResponseCode;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.RequestConfig;
import com.wolf.framework.service.parameter.RequestDataType;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.xiezhutech.mt.account.entity.AccountEntity;
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
        route = RouteNames.USER_ADD_V1,
        requestConfigs = {
            @RequestConfig(name = "userName", dataType = RequestDataType.STRING, desc = "姓名"),
            @RequestConfig(name = "mobile", dataType = RequestDataType.STRING, desc = "手机号"),
            @RequestConfig(name = "roleList", required = false, dataType = RequestDataType.STRING_ARRAY, desc = "角色")
        },
        responseConfigs = {
            @ResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "userId")
        },
        responseCodes = {
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作"),
            @ResponseCode(code = "not_admin", desc = "当前用户是不是管理员,无权限操作"),
            @ResponseCode(code = "mobile_exist", desc = "该手机号已经被使用")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "用户列表查询(材料中心)",
        group = "用户")
public class UserAddServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private UserLocal userLocal;

    private List<String> getRoleList(List<String> roleList) {
        List<String> resultList = new ArrayList(roleList.size());
        for (String role : roleList) {
            switch (role) {
                case "管理员":
                    resultList.add(UserRoles.ADMIN);
                    break;
                case "收件员":
                    resultList.add(UserRoles.PICKED);
                    break;
            }
        }
        return resultList;
    }

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity currentUserEntity = this.userLocal.queryUser(userId);
        if (currentUserEntity.isCustomer() == false) {
            if (currentUserEntity.getRoleList().contains(UserRoles.ADMIN)) {
                String userName = request.getStringValue("userName");
                String mobile = request.getStringValue("mobile");
                List<String> roleList = request.getStringListValue("roleList");
                if (roleList == null) {
                    roleList = new ArrayList(1);
                    roleList.add(UserRoles.PICKED);
                } else {
                    roleList = this.getRoleList(roleList);
                }
                //判断账号是否存在
                AccountEntity accountEntity = this.accountLocal.queryAccount(mobile);
                if (accountEntity == null) {
                    //可以添加账号
                    Map<String, Object> userMap = new HashMap();
                    userMap.put("userName", userName);
                    userMap.put("mobile", mobile);
                    userMap.put("roleList", roleList);
                    UserEntity userEntity = this.userLocal.addUser(userMap);
                    //注册账号
                    Map<String, Object> accountMap = new HashMap();
                    accountMap.put("account", userEntity.getMobile());
                    accountMap.put("userId", userEntity.getUserId());
                    accountMap.put("createTime", userEntity.getCreateTime());
                    this.accountLocal.addAccount(accountMap);
                    response.setData("userId", userEntity.getUserId());
                } else {
                    response.setCode("mobile_exist");
                }
            } else {
                response.setCode("not_admin");
            }
        } else {
            response.setCode("customer");
        }
    }
}
