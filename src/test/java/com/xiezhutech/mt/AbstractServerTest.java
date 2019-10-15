package com.xiezhutech.mt;

import com.wolf.framework.config.FrameworkConfig;
import com.wolf.framework.dao.elasticsearch.EsConfig;
import com.wolf.framework.test.TestHandler;
import com.xiezhutech.mt.config.MtConfigs;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class AbstractServerTest {

    protected static TestHandler testHandler;

    static {
        Map<String, String> parameterMap = new HashMap(8, 1);
        parameterMap.put(FrameworkConfig.BUILD_TIMESTAMP, Long.toString(System.currentTimeMillis()));
        parameterMap.put(FrameworkConfig.ANNOTATION_SCAN_PACKAGES, "com.xiezhutech.mt");
        //
        parameterMap.put(FrameworkConfig.COMPILE_MODEL, FrameworkConfig.SERVER);
        parameterMap.put(EsConfig.ELASTICSEARCH_HOST, "0.0.0.0");
        parameterMap.put(EsConfig.ELASTICSEARCH_DATABASE, "mt");
        //
        parameterMap.put(MtConfigs.CUSTOM_GCP_GATEWAY, "http://data.xiezhutech.com/microproject-server/http/api");
        //
        testHandler = new TestHandler(parameterMap);
    }
}
