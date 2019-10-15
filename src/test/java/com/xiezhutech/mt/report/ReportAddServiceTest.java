package com.xiezhutech.mt.report;

import com.wolf.framework.reponse.Response;
import com.xiezhutech.mt.AbstractServerTest;
import com.xiezhutech.mt.MtTestConfig;
import com.xiezhutech.mt.config.RouteNames;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
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
public class ReportAddServiceTest extends AbstractServerTest {

    public ReportAddServiceTest() {
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
    public void test02UserList() throws IOException {
        File file = new File("/Users/jianying9/NetBeansProjects/mt-server/src/test/resources/test/newjson.json");
        Map<String, Object> paramMap = new HashMap();
        String model = FileUtils.readFileToString(file);
        model = model.replaceAll("\n", "");
        model = model.replaceAll(" ", "");
        Map<String, Object> modelMap = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            modelMap = mapper.readValue(model, Map.class);
        } catch (IOException e) {
        }
        paramMap.put("model", modelMap);
        Response response = testHandler.execute(RouteNames.REPORT_ADD_V1, paramMap);
    }

}
