package com.xiezhutech.mt.org.service;

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
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.ORG_JOIN_V1,
        requestConfigs = {
            @RequestConfig(name = "projectName", dataType = RequestDataType.STRING, desc = "工程名称"),
            @RequestConfig(name = "companyName", dataType = RequestDataType.STRING, desc = "公司名称"),
            @RequestConfig(name = "constructionName", dataType = RequestDataType.STRING, desc = "施工单位"),
            @RequestConfig(name = "witnessName", dataType = RequestDataType.STRING, desc = "见证单位")
        },
        responseConfigs = {
            @ResponseConfig(name = "joinId", dataType = ResponseDataType.STRING, desc = "申请id")
        },
        responseCodes = {
            @ResponseCode(code = "no_customer", desc = "当前用户不是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "申请加入组织",
        group = "组织")
public class OrgJoinServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private OrgLocal orgLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer()) {
            String projectName = request.getStringValue("projectName");
            String companyName = request.getStringValue("companyName");
            String constructionName = request.getStringValue("constructionName");
            String witnessName = request.getStringValue("witnessName");
            String orgId = projectName;
            OrgEntity orgEntity = this.orgLocal.queryOrg(orgId);
            if (orgEntity == null) {
                //保存临时组织
                Map<String, Object> dataMap = new HashMap();
                dataMap.put("projectName", projectName);
                dataMap.put("companyName", companyName);
                dataMap.put("constructionName", constructionName);
                dataMap.put("witnessName", witnessName);
                dataMap.put("orgId", orgId);
                orgEntity = this.orgLocal.addOrg(dataMap);
            }
            //提交申请
            Map<String, Object> dataMap = new HashMap();
            String joinId = orgEntity.getOrgId() + "_" + Long.toString(userId);
            dataMap.put("joinId", joinId);
            dataMap.put("projectName", orgEntity.getProjectName());
            dataMap.put("companyName", orgEntity.getCompanyName());
            dataMap.put("constructionName", orgEntity.getConstructionName());
            dataMap.put("witnessName", orgEntity.getWitnessName());
            dataMap.put("orgId", orgId);
            dataMap.put("applyId", userId);
            dataMap.put("applyName", userEntity.getUserName());
            dataMap.put("applyMobile", userEntity.getMobile());
            this.orgLocal.addOrgJoin(dataMap);
            //
            response.setData("joinId", joinId);
        } else {
            //不是客户账号,无法提交申请
            response.setCode("no_customer");
        }
    }
}
