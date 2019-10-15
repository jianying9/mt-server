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
public class OrgJoinHistoryListServiceTest extends AbstractServerTest {

    public OrgJoinHistoryListServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        testHandler.setSessionId("89f436abfc1a42c8ba71f859541092f1");
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
        Response response = testHandler.execute(RouteNames.ORG_JOIN_HISTORY_V1, paramMap);
    }
}
