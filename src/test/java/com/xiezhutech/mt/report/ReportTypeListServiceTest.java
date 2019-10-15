package com.xiezhutech.mt.report;

import com.wolf.framework.reponse.Response;
import com.xiezhutech.mt.AbstractServerTest;
import com.xiezhutech.mt.MtTestConfig;
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
public class ReportTypeListServiceTest extends AbstractServerTest {

    public ReportTypeListServiceTest() {
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
    public void test02UserList() {
        Map<String, Object> paramMap = new HashMap();
        Response response = testHandler.execute(RouteNames.REPORT_TYPE_LIST_V1, paramMap);
    }

}
