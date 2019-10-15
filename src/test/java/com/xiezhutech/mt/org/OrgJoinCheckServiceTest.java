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
public class OrgJoinCheckServiceTest extends AbstractServerTest {

    public OrgJoinCheckServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        testHandler.setSessionId(MtTestConfig.SID);
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
        paramMap.put("joinId", "协筑大厦_10006");
        paramMap.put("pass", true);
        Response response = testHandler.execute(RouteNames.ORG_CHECK_V1, paramMap);
    }
}
