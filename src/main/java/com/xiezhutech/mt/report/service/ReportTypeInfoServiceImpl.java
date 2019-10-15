package com.xiezhutech.mt.report.service;

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
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.model.local.ModelHandler;
import com.xiezhutech.mt.model.local.ModelLocal;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
import com.xiezhutech.mt.report.entity.ReportTypeEntity;
import com.xiezhutech.mt.report.local.ReportLocal;
import com.xiezhutech.mt.report.local.RoleTypes;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.io.IOException;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.REPORT_TYPE_INFO_V1,
        requestConfigs = {
            @RequestConfig(name = "type", dataType = RequestDataType.STRING, desc = "协议类型"),
            @RequestConfig(name = "orgId", required = false, dataType = RequestDataType.STRING, desc = "工程备案号")
        },
        responseConfigs = {
            @ResponseConfig(name = "reportType", dataType = ResponseDataType.OBJECT, desc = "待审核集合",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "title", dataType = ResponseDataType.STRING, desc = "标题"),
                        @SecondResponseConfig(name = "type", dataType = ResponseDataType.STRING, desc = "类型"),
                        @SecondResponseConfig(name = "model", dataType = ResponseDataType.JSON_OBJECT, desc = "模型")
                    })
        },
        responseCodes = {
            @ResponseCode(code = "org_not_exist", desc = "工程不存在"),
            @ResponseCode(code = "model_not_exist", desc = "数据异常,请联系服务人员")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "检测协议类型详情",
        group = "检测")
public class ReportTypeInfoServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private ReportLocal reportLocal;

    @InjectLocalService
    private OrgLocal orgLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @InjectLocalService
    private ModelLocal modelLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        String type = request.getStringValue("type");
        String orgId = request.getStringValue("orgId");
        String role = RoleTypes.APPLY;
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer() == false) {
            role = RoleTypes.CHECK;
        }
        ReportTypeEntity reportTypeEntity = this.reportLocal.queryReportType(type);
        if (reportTypeEntity != null) {
            Map<String, Object> reportTypeMap = EntityUtils.getMap(reportTypeEntity);
            Map<String, Object> modelMap = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                modelMap = mapper.readValue(reportTypeEntity.getModel(), Map.class);
            } catch (IOException e) {
            }
            if (modelMap == null) {
                response.setCode("model_not_exist");
            } else {
                ModelHandler modelHandler = this.modelLocal.getModelHandler(type);
                OrgEntity orgEntity = this.orgLocal.queryOrg(orgId);
                if (orgEntity != null) {
                    modelHandler.fillModel(orgEntity, modelMap);
                }
                modelHandler.filterModel(role, modelMap);
                reportTypeMap.put("model", modelMap);
                response.setData("reportType", reportTypeMap);
            }
        }
    }
}
