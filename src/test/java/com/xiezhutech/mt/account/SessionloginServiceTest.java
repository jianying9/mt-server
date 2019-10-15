package com.xiezhutech.mt.account;

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
public class SessionloginServiceTest extends AbstractServerTest {

    public SessionloginServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
        paramMap.put("oldSid", "e250ddee3aa146a4badbd197e4fa37bc");
        Response response = testHandler.execute(RouteNames.SESSION_LOGIN_V1, paramMap);
    }
}
