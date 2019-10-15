package com.xiezhutech.mt.user;

import com.wolf.framework.reponse.Response;
import com.xiezhutech.mt.AbstractServerTest;
import com.xiezhutech.mt.config.RouteNames;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author jianying9
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAddServiceTest extends AbstractServerTest {

    public UserAddServiceTest() {
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
    public void test02UserList() {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("mobile", "18659199766");
        paramMap.put("userName", "刘XX2");
        Response response = testHandler.execute(RouteNames.USER_ADD_V1, paramMap);
    }

}
