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
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.report.entity.ReportEntity;
import com.xiezhutech.mt.report.local.ReportLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import com.xiezhutech.mt.util.DateUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.REPORT_LIST_V1,
        requestConfigs = {
            @RequestConfig(name = "nextIndex", required = false, dataType = RequestDataType.LONG, desc = "分页查询-起始id"),
            @RequestConfig(name = "nextSize", required = false, dataType = RequestDataType.LONG, desc = "分页查询-返回记录数"),
            @RequestConfig(name = "orgId", required = false, dataType = RequestDataType.STRING, desc = "工程号"),
            @RequestConfig(name = "state", required = false, dataType = RequestDataType.ENUM, text = "审核中,通过,退回", desc = "状态"),
            @RequestConfig(name = "mine", required = false, dataType = RequestDataType.BOOLEAN, desc = "我审核")
        },
        responseConfigs = {
            @ResponseConfig(name = "nextIndex", dataType = ResponseDataType.LONG, desc = "当前页最大nextIndex"),
            @ResponseConfig(name = "nextSize", dataType = ResponseDataType.LONG, desc = "当前页记录数"),
            @ResponseConfig(name = "reportArray", dataType = ResponseDataType.OBJECT_ARRAY, desc = "检测集合",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "reportId", dataType = ResponseDataType.LONG, desc = "id"),
                        @SecondResponseConfig(name = "type", dataType = ResponseDataType.STRING, desc = "类型"),
                        @SecondResponseConfig(name = "orgId", dataType = ResponseDataType.STRING, desc = "工程号"),
                        @SecondResponseConfig(name = "applyUser", dataType = ResponseDataType.OBJECT, desc = "申请人",
                                thirdResponseConfigs = {
                                    @ThirdResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "id"),
                                    @ThirdResponseConfig(name = "userName", dataType = ResponseDataType.STRING, desc = "名称"),
                                    @ThirdResponseConfig(name = "headUrl", dataType = ResponseDataType.STRING, desc = "头像"),
                                    @ThirdResponseConfig(name = "mobile", dataType = ResponseDataType.STRING, desc = "手机号")
                                }),
                        @SecondResponseConfig(name = "checkUser", dataType = ResponseDataType.OBJECT, desc = "申请人",
                                thirdResponseConfigs = {
                                    @ThirdResponseConfig(name = "userId", dataType = ResponseDataType.LONG, desc = "id"),
                                    @ThirdResponseConfig(name = "userName", dataType = ResponseDataType.STRING, desc = "名称"),
                                    @ThirdResponseConfig(name = "headUrl", dataType = ResponseDataType.STRING, desc = "头像")
                                }),
                        @SecondResponseConfig(name = "applyTime", dataType = ResponseDataType.DATE_TIME, desc = "申请时间"),
                        @SecondResponseConfig(name = "checkTime", dataType = ResponseDataType.DATE_TIME, desc = "审核时间"),
                        @SecondResponseConfig(name = "state", dataType = ResponseDataType.STRING, desc = "状态")
                    })
        },
        responseCodes = {
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "检测列表查询(检测中心)",
        group = "检测")
public class ReportListServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private ReportLocal reportLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer() == false) {
            //查询列表
            Long nextIndex = request.getLongValue("nextIndex");
            if (nextIndex == null) {
                nextIndex = 0l;
            }
            Long nextSize = request.getLongValue("nextSize");
            if (nextSize == null) {
                nextSize = 20l;
            }
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            String orgId = request.getStringValue("orgId");
            if (orgId != null) {
                boolQueryBuilder.must(QueryBuilders.termQuery("orgId", orgId));
            }
            String state = request.getStringValue("state");
            if (state != null && state.isEmpty() == false) {
                boolQueryBuilder.must(QueryBuilders.termQuery("state", state));
            }
            //
            Boolean mine = request.getBooleanValue("mine");
            if (mine != null && mine) {
                boolQueryBuilder.must(QueryBuilders.termQuery("checkId", userId));
            }
            SortBuilder sortBuilder = SortBuilders.fieldSort("applyTime").order(SortOrder.DESC);
            List<ReportEntity> reportEntityList = this.reportLocal.searchReport(boolQueryBuilder, sortBuilder, nextIndex.intValue(), nextSize.intValue());
            List<Map<String, Object>> reportMapList = new ArrayList(reportEntityList.size());
            Map<String, Object> reportMap;
            String applyTime;
            String checkTime;
            Map<String, Object> checkUserMap;
            Map<String, Object> applyUserMap;
            UserEntity applyUserEntity;
            UserEntity checkUserEntity;
            Map<Long, UserEntity> userEntityMap = new HashMap();
            for (ReportEntity reportEntity : reportEntityList) {
                reportMap = EntityUtils.getMap(reportEntity);
                //
                applyUserEntity = this.userLocal.queryUser(reportEntity.getApplyId(), userEntityMap);
                applyUserMap = this.userLocal.getUserSimpleInfoMap(applyUserEntity);
                //
                reportMap.put("applyUser", applyUserMap);
                applyTime = DateUtils.convertToYYYY_MM_DD_HHmmSS(reportEntity.getApplyTime());
                reportMap.put("applyTime", applyTime);
                //
                if (reportEntity.getCheckId() > 0) {
                    checkUserEntity = this.userLocal.queryUser(reportEntity.getCheckId(), userEntityMap);
                    checkUserMap = this.userLocal.getUserSimpleInfoMap(checkUserEntity);
                    reportMap.put("checkUser", checkUserMap);
                }
                //
                if (reportEntity.getCheckTime() > 0) {
                    checkTime = DateUtils.convertToYYYY_MM_DD_HHmmSS(reportEntity.getCheckTime());
                    reportMap.put("checkTime", checkTime);
                } else {
                    reportMap.put("checkTime", "");
                }
                reportMapList.add(reportMap);
            }
            //
            long lastNextIndex = nextIndex + nextSize;
            Map<String, Object> responseMap = new HashMap();
            responseMap.put("reportArray", reportMapList);
            responseMap.put("nextIndex", lastNextIndex);
            responseMap.put("nextSize", nextSize);
            response.setDataMap(responseMap);
        } else {
            //是客户账号
            response.setCode("customer");
        }
    }
}
