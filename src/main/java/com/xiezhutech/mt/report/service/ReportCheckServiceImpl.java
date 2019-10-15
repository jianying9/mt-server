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
import com.wolf.framework.utils.MapUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.model.local.ModelHandler;
import com.xiezhutech.mt.model.local.ModelLocal;
import com.xiezhutech.mt.report.entity.ReportEntity;
import com.xiezhutech.mt.report.local.ReportLocal;
import com.xiezhutech.mt.report.local.ReportStates;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.REPORT_CHECK_V1,
        requestConfigs = {
            @RequestConfig(name = "model", dataType = RequestDataType.JSON_OBJECT, desc = "协议类型"),
            @RequestConfig(name = "reportId", dataType = RequestDataType.LONG, desc = "检测id"),
            @RequestConfig(name = "pass", dataType = RequestDataType.BOOLEAN, desc = "是否通过")
        },
        responseConfigs = {},
        responseCodes = {
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作"),
            @ResponseCode(code = "report_not_exist", desc = "检测委托不存在"),
            @ResponseCode(code = "model_error", desc = "提交数据模型解析错误"),
            @ResponseCode(code = "model_type_error", desc = "模型类型不存在")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "审核检测委托",
        group = "检测")
public class ReportCheckServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private ReportLocal reportLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @InjectLocalService
    private ModelLocal modelLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer() == false) {
            //读取模型信息
            long reportId = request.getLongValue("reportId");
            ReportEntity reportEntity = this.reportLocal.queryReport(reportId);
            if (reportEntity != null && reportEntity.getCheckId() == 0) {
                //
                Map<String, Object> modelMap = request.getObjectValue("model");
                String type = MapUtils.getStringValue(modelMap, "type");
                ModelHandler modelHandler = this.modelLocal.getModelHandler(type);
                if (modelHandler != null) {
                    //添加模型自动计算
                    modelHandler.fillModel(modelMap);
                    //未审核
                    String state = ReportStates.RETURN;
                    boolean pass = request.getBooleanValue("pass");
                    if (pass) {
                        state = ReportStates.PASS;
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    String model = "";
                    try {
                        model = mapper.writeValueAsString(modelMap);
                    } catch (IOException ex) {
                    }
                    Map<String, Object> dataMap = new HashMap();
                    dataMap.put("reportId", reportId);
                    if (model.isEmpty() == false) {
                        dataMap.put("model", model);
                    }
                    dataMap.put("state", state);
                    dataMap.put("checkId", userId);
                    dataMap.put("checkName", userEntity.getUserName());
                    dataMap.put("checkTime", System.currentTimeMillis());
                    this.reportLocal.updateReport(dataMap);
                } else {
                    response.setCode("model_type_error");
                }
            } else {
                response.setCode("report_not_exist");
            }
        } else {
            //不是客户账号,无法提交申请
            response.setCode("customer");
        }
    }
}
