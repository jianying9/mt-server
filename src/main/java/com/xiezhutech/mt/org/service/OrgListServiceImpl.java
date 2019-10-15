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
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
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
        route = RouteNames.ORG_LIST_V1,
        requestConfigs = {
            @RequestConfig(name = "nextIndex", required = false, dataType = RequestDataType.LONG, desc = "分页查询-起始id"),
            @RequestConfig(name = "nextSize", required = false, dataType = RequestDataType.LONG, desc = "分页查询-返回记录数")
        },
        responseConfigs = {
            @ResponseConfig(name = "nextIndex", dataType = ResponseDataType.LONG, desc = "当前页最大nextIndex"),
            @ResponseConfig(name = "nextSize", dataType = ResponseDataType.LONG, desc = "当前页记录数"),
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
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "组织列表查询(材料中心)",
        group = "组织")
public class OrgListServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private OrgLocal orgLocal;

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
            boolQueryBuilder.must(QueryBuilders.termQuery("enabled", true));
            //
            SortBuilder sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
            List<OrgEntity> orgEntityList = this.orgLocal.searchOrg(boolQueryBuilder, sortBuilder, nextIndex.intValue(), nextSize.intValue());
            List<Map<String, Object>> orgMapList = new ArrayList(orgEntityList.size());
            Map<String, Object> orgMap;
            for (OrgEntity orgEntity : orgEntityList) {
                orgMap = EntityUtils.getMap(orgEntity);
                orgMapList.add(orgMap);
            }
            //
            long lastNextIndex = nextIndex + nextSize;
            Map<String, Object> responseMap = new HashMap();
            responseMap.put("orgArray", orgMapList);
            responseMap.put("nextIndex", lastNextIndex);
            responseMap.put("nextSize", nextSize);
            response.setDataMap(responseMap);
        } else {
            response.setCode("customer");
        }
    }
}
