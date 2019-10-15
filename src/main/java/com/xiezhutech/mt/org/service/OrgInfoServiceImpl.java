package com.xiezhutech.mt.org.service;

import com.wolf.framework.local.InjectLocalService;
import com.xiezhutech.mt.account.service.*;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.RequestConfig;
import com.wolf.framework.service.parameter.RequestDataType;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.service.parameter.ThirdResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.ORG_INFO_V1,
        requestConfigs = {
            @RequestConfig(name = "orgId", dataType = RequestDataType.STRING, desc = "工程备案号")
        },
        responseConfigs = {
            @ResponseConfig(name = "org", dataType = ResponseDataType.OBJECT, desc = "项目信息",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "orgId", dataType = ResponseDataType.STRING, desc = "工程号"),
                        @SecondResponseConfig(name = "projectName", dataType = ResponseDataType.STRING, desc = "工程名称"),
                        @SecondResponseConfig(name = "companyName", dataType = ResponseDataType.STRING, desc = "公司名称"),
                        @SecondResponseConfig(name = "constructionName", dataType = ResponseDataType.STRING, desc = "施工单位"),
                        @SecondResponseConfig(name = "witnessName", dataType = ResponseDataType.STRING, desc = "见证单位"),
                        @SecondResponseConfig(name = "userArray", dataType = ResponseDataType.OBJECT_ARRAY, desc = "人员列表",
                                thirdResponseConfigs = {
                                    @ThirdResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "id"),
                                    @ThirdResponseConfig(name = "userName", dataType = ResponseDataType.STRING, desc = "名称"),
                                    @ThirdResponseConfig(name = "headUrl", dataType = ResponseDataType.STRING, desc = "头像")
                                })
                    })
        },
        responseCodes = {},
        pushRoutes = {},
        validateSession = true,
        desc = "组织详情查询",
        group = "组织")
public class OrgInfoServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private OrgLocal orgLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        String orgId = request.getStringValue("orgId");
        OrgEntity orgEntity = this.orgLocal.queryOrg(orgId);
        if (orgEntity != null) {
            Map<String, Object> dataMap = EntityUtils.getMap(orgEntity);
            //
            List<Long> userIdList = orgEntity.getUserIdList();
            UserEntity userEntity;
            Map<String, Object> userMap;
            List<Map<String, Object>> userMapList = new ArrayList(userIdList.size());
            for (long userId : userIdList) {
                userEntity = this.userLocal.queryUser(userId);
                if (userEntity != null) {
                    userMap = EntityUtils.getMap(userEntity);
                    userMapList.add(userMap);
                }
            }
            dataMap.put("userArray", userMapList);
            response.setData("org", dataMap);
        }
    }
}
