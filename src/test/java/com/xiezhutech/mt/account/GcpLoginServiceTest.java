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
public class GcpLoginServiceTest extends AbstractServerTest {

    public GcpLoginServiceTest() {
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
//        paramMap.put("authCode", "77568e8b-a215-4b2a-a9c3-4685f4ae8cfb");
//40ccdc4fdc784b5c89b82798ec96b413
        paramMap.put("authCode", "729949fc-2855-4bfe-b9ae-fca2631b5233");
        //bd6b8e73be294adcb152250ec0283842
        Response response = testHandler.execute(RouteNames.GCP_LOGIN_V1, paramMap);
    }
}
