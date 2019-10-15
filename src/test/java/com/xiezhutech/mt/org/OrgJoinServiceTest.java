package com.xiezhutech.mt.org;

import com.xiezhutech.mt.*;
import com.wolf.framework.reponse.Response;
import com.xiezhutech.mt.config.RouteNames;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jianying9
 */
public class OrgJoinServiceTest extends AbstractServerTest {

    public OrgJoinServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        testHandler.setSessionId(MtTestConfig.CUSTOMER_SID);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    //
    @Test
    public void check() {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("projectName", "协筑大厦");
        paramMap.put("companyName", "协筑集团");
        paramMap.put("constructionName", "协筑建设");
        paramMap.put("witnessName", "协筑监理");
        Response response = testHandler.execute(RouteNames.ORG_JOIN_V1, paramMap);
    }
}
