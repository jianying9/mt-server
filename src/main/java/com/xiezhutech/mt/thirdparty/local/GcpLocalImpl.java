package com.xiezhutech.mt.thirdparty.local;

import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.framework.utils.MapUtils;
import com.wolf.thirdparty.http.HttpLocal;
import com.xiezhutech.mt.config.MtConfigs;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jianying9
 */
@LocalServiceConfig
public class GcpLocalImpl implements GcpLocal {

    @InjectLocalService
    private HttpLocal httpLocal;

    private String gcpGateway = "";

    private final String appId = "2019042500000001";

    private final String oauthUserInfo = "/oauth/user/info/v1";

    @Override
    public void init() {
        String gateway = ApplicationContext.CONTEXT.getParameter(MtConfigs.CUSTOM_GCP_GATEWAY);
        if (gateway != null) {
            this.gcpGateway = gateway;
        }
    }

    @Override
    public GcpUserInfo getGcpUserInfo(String authCode) {
        Map<String, String> parameterMap = new HashMap(2, 1);
        parameterMap.put("authCode", authCode);
        parameterMap.put("appId", this.appId);
        String result = this.httpLocal.doGet(this.gcpGateway, this.oauthUserInfo, parameterMap);
        //解析json
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = null;
        try {
            resultMap = mapper.readValue(result, Map.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        //
        GcpUserInfo gcpUserInfo = null;
        if (resultMap != null) {
            String code = MapUtils.getStringValue(resultMap, "code");
            if (code.equals("success")) {
                Map<String, Object> dataMap = MapUtils.getObjectValue(resultMap, "data");
                Map<String, Object> userMap = MapUtils.getObjectValue(dataMap, "user");
                long userId = MapUtils.getLongValue(userMap, "userId");
                String nickName = MapUtils.getStringValue(userMap, "nickName");
                String userName = MapUtils.getStringValue(userMap, "userName");
                if (userName == null) {
                    userName = "";
                }
                String headUrl = MapUtils.getStringValue(userMap, "smallHeadUrl");
                String mobile = MapUtils.getStringValue(userMap, "mobile");
                gcpUserInfo = new GcpUserInfo(userId, nickName, userName, mobile, headUrl);
            }
        }
        return gcpUserInfo;
    }

}
