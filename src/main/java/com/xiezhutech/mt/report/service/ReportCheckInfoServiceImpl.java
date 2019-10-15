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
import com.wolf.framework.service.parameter.ThirdResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.wolf.framework.utils.MapUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.model.local.ModelHandler;
import com.xiezhutech.mt.model.local.ModelLocal;
import com.xiezhutech.mt.report.entity.ReportEntity;
import com.xiezhutech.mt.report.local.ReportLocal;
import com.xiezhutech.mt.report.local.ReportStates;
import com.xiezhutech.mt.report.local.RoleTypes;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import com.xiezhutech.mt.util.DateUtils;
import java.io.IOException;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.REPORT_CHECK_INFO_V1,
        requestConfigs = {
            @RequestConfig(name = "reportId", dataType = RequestDataType.LONG, desc = "检测id")
        },
        responseConfigs = {
            @ResponseConfig(name = "report", dataType = ResponseDataType.OBJECT, desc = "检测",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "reportId", dataType = ResponseDataType.LONG, desc = "id"),
                        @SecondResponseConfig(name = "type", dataType = ResponseDataType.STRING, desc = "类型"),
                        @SecondResponseConfig(name = "category", dataType = ResponseDataType.STRING, desc = "大类"),
                        @SecondResponseConfig(name = "orgId", dataType = ResponseDataType.STRING, desc = "工程号"),
                        @SecondResponseConfig(name = "applyUser", dataType = ResponseDataType.OBJECT, desc = "申请人",
                                thirdResponseConfigs = {
                                    @ThirdResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "id"),
                                    @ThirdResponseConfig(name = "userName", dataType = ResponseDataType.STRING, desc = "名称"),
                                    @ThirdResponseConfig(name = "headUrl", dataType = ResponseDataType.STRING, desc = "头像"),
                                    @ThirdResponseConfig(name = "mobile", dataType = ResponseDataType.STRING, desc = "手机号")
                                }),
                        @SecondResponseConfig(name = "state", dataType = ResponseDataType.STRING, desc = "状态"),
                        @SecondResponseConfig(name = "model", dataType = ResponseDataType.JSON_OBJECT, desc = "模型")
                    })
        },
        responseCodes = {
            @ResponseCode(code = "model_not_exist", desc = "数据异常,请刷新"),
            @ResponseCode(code = "state_error", desc = "该委托已经审核"),
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "查看检测委托",
        group = "检测")
public class ReportCheckInfoServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private ReportLocal reportLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @InjectLocalService
    private ModelLocal modelLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity currentUserEntity = this.userLocal.queryUser(userId);
        if (currentUserEntity.isCustomer() == false) {
            //读取模型信息
            long reportId = request.getLongValue("reportId");
            ReportEntity reportEntity = this.reportLocal.queryReport(reportId);
            if (reportEntity != null) {
                if (reportEntity.getState().equals(ReportStates.WAIT)) {
                    Map<String, Object> modelMap = null;
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        modelMap = mapper.readValue(reportEntity.getModel(), Map.class);
                    } catch (IOException e) {
                    }
                    if (modelMap == null) {
                        response.setCode("model_not_exist");
                    } else {
                        String type = MapUtils.getStringValue(modelMap, "type");
                        ModelHandler modelHandler = this.modelLocal.getModelHandler(type);
                        //根据角色过滤可操作字段
                        String role = RoleTypes.CHECK;
                        modelHandler.filterModel(role, modelMap);
                        Map<String, Object> reportMap = EntityUtils.getMap(reportEntity);
                        //
                        UserEntity applyUserEntity = this.userLocal.queryUser(reportEntity.getApplyId());
                        Map<String, Object> applyUserMap = this.userLocal.getUserSimpleInfoMap(applyUserEntity);
                        reportMap.put("applyUser", applyUserMap);
                        //
                        String applyTime = DateUtils.convertToYYYY_MM_DD_HHmmSS(reportEntity.getApplyTime());
                        reportMap.put("applyTime", applyTime);
                        //
                        reportMap.put("model", modelMap);
                        response.setData("report", reportMap);
                    }
                } else {
                    response.setCode("state_error");
                }
            } else {
                response.setCode("customer");
            }
        }
    }
}
