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
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.config.RouteNames;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.entity.OrgJoinEntity;
import com.xiezhutech.mt.org.local.OrgLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserLocal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = RouteNames.ORG_CHECK_V1,
        requestConfigs = {
            @RequestConfig(name = "joinId", dataType = RequestDataType.STRING, desc = "joinId"),
            @RequestConfig(name = "pass", dataType = RequestDataType.BOOLEAN, desc = "是否通过")
        },
        responseConfigs = {},
        responseCodes = {
            @ResponseCode(code = "customer", desc = "当前用户是客户账号,无权限操作")
        },
        pushRoutes = {},
        validateSession = true,
        desc = "审核加入组织的申请",
        group = "组织")
public class OrgCheckServiceImpl extends AbstractSessionService implements Service {

    @InjectLocalService
    private OrgLocal orgLocal;

    @InjectLocalService
    private UserLocal userLocal;

    @Override
    protected void sessionExecute(Request request, Response response, SessionEntity sessionEntity) {
        long userId = sessionEntity.getUserId();
        UserEntity userEntity = this.userLocal.queryUser(userId);
        if (userEntity.isCustomer() == false) {
            String joinId = request.getStringValue("joinId");
            OrgJoinEntity orgJoinEntity = this.orgLocal.queryOrgJoin(joinId);
            if (orgJoinEntity != null) {
                boolean pass = request.getBooleanValue("pass");
                String orgId = orgJoinEntity.getOrgId();
                if (pass) {
                    synchronized (this) {
                        //更新组织为启用,关联账号
                        OrgEntity orgEntity = this.orgLocal.queryOrg(orgId);
                        if (orgEntity != null) {
                            Map<String, Object> orgMap = new HashMap();
                            orgMap.put("orgId", orgJoinEntity.getOrgId());
                            orgMap.put("enabled", true);
                            if (orgEntity.getUserIdList().contains(orgJoinEntity.getApplyId()) == false) {
                                List<Long> userIdList = new ArrayList();
                                userIdList.addAll(orgEntity.getUserIdList());
                                userIdList.add(orgJoinEntity.getApplyId());
                                orgMap.put("userIdList", userIdList);
                            }
                            this.orgLocal.updateOrg(orgMap);
                            //更新用户信息
                            UserEntity applyUerEntity = this.userLocal.queryUser(orgJoinEntity.getApplyId());
                            if (applyUerEntity != null && applyUerEntity.getOrgIdList().contains(orgId) == false) {
                                List<String> orgIdList = new ArrayList();
                                orgIdList.addAll(applyUerEntity.getOrgIdList());
                                orgIdList.add(orgJoinEntity.getOrgId());
                                Map<String, Object> userMap = new HashMap();
                                userMap.put("userId", orgJoinEntity.getApplyId());
                                userMap.put("orgIdList", orgIdList);
                                this.userLocal.updateUser(userMap);
                            }
                        }
                    }
                }
                //保存审批记录
                Map<String, Object> historyMap = EntityUtils.getMap(orgJoinEntity);
                historyMap.put("checkName", userEntity.getUserName());
                historyMap.put("checkId", userEntity.getUserId());
                historyMap.put("checkTime", System.currentTimeMillis());
                historyMap.put("pass", pass);
                this.orgLocal.addOrgJoinHistory(historyMap);
                //
                this.orgLocal.deleteOrgJoin(joinId);
            }
        } else {
            //不是客户账号,无法提交申请
            response.setCode("customer");
        }
    }
}
