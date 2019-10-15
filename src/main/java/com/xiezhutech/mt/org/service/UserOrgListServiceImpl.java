package com.xiezhutech.mt.org.service;

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
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.USER_ORG_LIST_V1,
        requestConfigs = {},
        responseConfigs = {
            @ResponseConfig(name = "orgArray", dataType = ResponseDataType.OBJECT_ARRAY, desc = "组织集合",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "orgId", dataType = ResponseDataType.STRING, desc = "工程号"),
                        @SecondResponseConfig(name = "projectName", dataType = ResponseDataType.STRING, desc = "工程名称"),
                        @SecondResponseConfig(name = "companyName", dataType = ResponseDataType.STRING, desc = "公司名称"),
                        @SecondResponseConfig(name = "constructionName", dataType = ResponseDataType.STRING, desc = "施工单位"),
                        @SecondResponseConfig(name = "witnessName", dataType = ResponseDataType.STRING, desc = "见证单位")
                    })
        },
        responseCodes = {
            @ResponseCode(code = "no_customer", desc = "当前用户不是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "我的组织列表查询(客户)",
        group = "组织")
public class UserOrgListServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private OrgLocal orgLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer()) {
            //查询列表
            List<String> orgIdList = userEntity.getOrgIdList();
            List<Map<String, Object>> orgMapList = new ArrayList(orgIdList.size());
            Map<String, Object> orgMap;
            OrgEntity orgEntity;
            for (String orgId : orgIdList) {
                orgEntity = this.orgLocal.queryOrg(orgId);
                if (orgEntity != null && orgEntity.isEnabled()) {
                    orgMap = EntityUtils.getMap(orgEntity);
                    orgMapList.add(orgMap);
                }
            }
            //
            Map<String, Object> responseMap = new HashMap();
            responseMap.put("orgArray", orgMapList);
            response.setDataMap(responseMap);
        } else {
            response.setCode("no_customer");
        }
    }
}
