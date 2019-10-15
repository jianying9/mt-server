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
import com.wolf.framework.utils.EntityUtils;
import com.wolf.framework.utils.MapUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.model.local.ModelHandler;
import com.xiezhutech.mt.model.local.ModelLocal;
import com.xiezhutech.mt.report.entity.ReportEntity;
import com.xiezhutech.mt.report.local.ReportLocal;
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
        route = RouteNames.REPORT_ADD_V1,
        requestConfigs = {
            @RequestConfig(name = "model", dataType = RequestDataType.JSON_OBJECT, desc = "协议类型")
        },
        responseConfigs = {
            @ResponseConfig(name = "reportId", dataType = ResponseDataType.LONG, desc = "检测id")
        },
        responseCodes = {
            @ResponseCode(code = "no_customer", desc = "当前用户不是客户账号,无权限操作"),
            @ResponseCode(code = "model_error", desc = "提交数据模型解析错误"),
            @ResponseCode(code = "model_type_error", desc = "模型类型不存在")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "新增检测委托",
        group = "检测")
public class ReportAddServiceImpl extends AbstractSessionService implements Service {

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
        if (userEntity.isCustomer()) {
            //读取模型信息
            Map<String, Object> modelMap = request.getObjectValue("model");
            String type = MapUtils.getStringValue(modelMap, "type");
            String category = MapUtils.getStringValue(modelMap, "category");
            String orgId = MapUtils.getStringValue(modelMap, "orgId");
            if (type != null && category != null && orgId != null) {
                ModelHandler modelHandler = this.modelLocal.getModelHandler(type);
                if (modelHandler != null) {
                    //添加模型自动计算
                    modelHandler.fillModel(modelMap);
                    ObjectMapper mapper = new ObjectMapper();
                    String model = "";
                    try {
                        model = mapper.writeValueAsString(modelMap);
                    } catch (IOException ex) {
                    }
                    //
                    Map<String, Object> dataMap = EntityUtils.getTempMap(ReportEntity.class);
                    dataMap.put("orgId", orgId);
                    //
                    dataMap.put("applyId", userId);
                    dataMap.put("applyName", userEntity.getUserName());
                    dataMap.put("applyMobile", userEntity.getMobile());
                    dataMap.put("applyTime", System.currentTimeMillis());
                    //
                    dataMap.put("type", type);
                    dataMap.put("category", category);
                    dataMap.put("model", model);
                    long reportId = this.reportLocal.addReport(dataMap);
                    //
                    response.setData("reportId", reportId);
                } else {
                    response.setCode("model_type_error");
                }
            } else {
                response.setCode("model_error");
            }
        } else {
            //不是客户账号,无法提交申请
            response.setCode("no_customer");
        }
    }
}
