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
public class UserOrgListServiceTest extends AbstractServerTest {

    public UserOrgListServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        testHandler.setSessionId("9b5db1561d0b4b9d8488840eada052b7");
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
        Response response = testHandler.execute(RouteNames.USER_ORG_LIST_V1, paramMap);
    }
}
