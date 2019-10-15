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
import com.wolf.framework.service.parameter.SecondResponseConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.org.entity.OrgJoinHistoryEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
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
        route = RouteNames.ORG_JOIN_HISTORY_V1,
        requestConfigs = {
            @RequestConfig(name = "nextIndex", required = false, dataType = RequestDataType.LONG, desc = "分页查询-起始id"),
            @RequestConfig(name = "nextSize", required = false, dataType = RequestDataType.LONG, desc = "分页查询-返回记录数")
        },
        responseConfigs = {
            @ResponseConfig(name = "nextIndex", dataType = ResponseDataType.LONG, desc = "当前页最大nextIndex"),
            @ResponseConfig(name = "nextSize", dataType = ResponseDataType.LONG, desc = "当前页记录数"),
            @ResponseConfig(name = "orgJoinHistoryArray", dataType = ResponseDataType.OBJECT_ARRAY, desc = "待审核集合",
                    secondResponseConfigs = {
                        @SecondResponseConfig(name = "historyId", dataType = ResponseDataType.LONG, desc = "申请id"),
                        @SecondResponseConfig(name = "orgId", dataType = ResponseDataType.STRING, desc = "工程号"),
                        @SecondResponseConfig(name = "projectName", dataType = ResponseDataType.STRING, desc = "工程名称"),
                        @SecondResponseConfig(name = "companyName", dataType = ResponseDataType.STRING, desc = "公司名称"),
                        @SecondResponseConfig(name = "constructionName", dataType = ResponseDataType.STRING, desc = "施工单位"),
                        @SecondResponseConfig(name = "witnessName", dataType = ResponseDataType.STRING, desc = "见证单位"),
                        @SecondResponseConfig(name = "applyId", dataType = ResponseDataType.LONG, desc = "申请人id"),
                        @SecondResponseConfig(name = "applyName", dataType = ResponseDataType.STRING, desc = "申请人"),
                        @SecondResponseConfig(name = "applyMobile", dataType = ResponseDataType.STRING, desc = "申请电话"),
                        @SecondResponseConfig(name = "applyTime", dataType = ResponseDataType.DATE_TIME, desc = "申请时间"),
                        @SecondResponseConfig(name = "checkId", dataType = ResponseDataType.LONG, desc = "审核人id"),
                        @SecondResponseConfig(name = "checkName", dataType = ResponseDataType.STRING, desc = "审核人"),
                        @SecondResponseConfig(name = "checkTime", dataType = ResponseDataType.DATE_TIME, desc = "审核时间"),
                        @SecondResponseConfig(name = "pass", dataType = ResponseDataType.BOOLEAN, desc = "审核结果"),
                        @SecondResponseConfig(name = "result", dataType = ResponseDataType.STRING, desc = "审核结果")
                    })
        },
        responseCodes = {
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "审核历史记录列表",
        group = "组织")
public class OrgJoinHistoryListServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private OrgLocal orgLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
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
        //如果是客户,限制能看客户的审核记录
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer()) {
            boolQueryBuilder.must(QueryBuilders.termQuery("applyId", userEntity.getUserId()));
        }
        SortBuilder sortBuilder = SortBuilders.fieldSort("checkTime").order(SortOrder.DESC);
        List<OrgJoinHistoryEntity> orgJoinHistoryEntityList = this.orgLocal.searchOrgJoinHistory(boolQueryBuilder, sortBuilder, nextIndex.intValue(), nextSize.intValue());
        List<Map<String, Object>> orgJoinHistoryMapList = new ArrayList(orgJoinHistoryEntityList.size());
        Map<String, Object> orgJoinHistoryMap;
        String applyTime;
        String checkTime;
        String result;
        for (OrgJoinHistoryEntity orgJoinHistoryEntity : orgJoinHistoryEntityList) {
            orgJoinHistoryMap = EntityUtils.getMap(orgJoinHistoryEntity);
            applyTime = DateUtils.convertToYYYY_MM_DD_HHmmSS(orgJoinHistoryEntity.getApplyTime());
            orgJoinHistoryMap.put("applyTime", applyTime);
            //
            checkTime = DateUtils.convertToYYYY_MM_DD_HHmmSS(orgJoinHistoryEntity.getCheckTime());
            orgJoinHistoryMap.put("checkTime", checkTime);
            //
            if (orgJoinHistoryEntity.isPass()) {
                result = "审核通过";
            } else {
                result = "审核未通过";
            }
            orgJoinHistoryMap.put("result", result);
            orgJoinHistoryMapList.add(orgJoinHistoryMap);
        }
        //
        long lastNextIndex = nextIndex + nextSize;
        Map<String, Object> responseMap = new HashMap();
        responseMap.put("orgJoinHistoryArray", orgJoinHistoryMapList);
        responseMap.put("nextIndex", lastNextIndex);
        responseMap.put("nextSize", nextSize);
        response.setDataMap(responseMap);
    }
}
