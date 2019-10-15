package com.xiezhutech.mt.report.service;

import com.wolf.framework.local.InjectLocalService;
import com.xiezhutech.mt.account.service.*;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.report.entity.ReportTypeEntity;
import com.xiezhutech.mt.report.local.ReportLocal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.REPORT_TYPE_LIST_V1,
        requestConfigs = {},
        responseConfigs = {
            @ResponseConfig(name = "reportTypeArray", dataType = ResponseDataType.OBJECT_ARRAY, desc = "待审核集合",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "title", dataType = ResponseDataType.STRING, desc = "标题"),
                        @SecondResponseConfig(name = "type", dataType = ResponseDataType.STRING, desc = "类型")
                    })
        },
        responseCodes = {},
        pushRoutes = {},
        validateSession = true,
        desc = "检测协议类型列表",
        group = "检测")
public class ReportTypeListServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private ReportLocal reportLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        List<ReportTypeEntity> reportTypeEntityList = this.reportLocal.queryReprotTypeList();
        List<Map<String, Object>> reportTypeMapList = new ArrayList(reportTypeEntityList.size());
        Map<String, Object> reportTypeMap;
        for (ReportTypeEntity reportTypeEntity : reportTypeEntityList) {
            reportTypeMap = EntityUtils.getMap(reportTypeEntity);
            reportTypeMapList.add(reportTypeMap);
        }
        //
        Map<String, Object> responseMap = new HashMap();
        responseMap.put("reportTypeArray", reportTypeMapList);
        response.setDataMap(responseMap);
    }
}
