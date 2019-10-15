package com.xiezhutech.mt.database.report;

import com.xiezhutech.mt.*;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.dao.elasticsearch.EsAdminContextImpl;
import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.wolf.framework.utils.MapUtils;
import com.xiezhutech.mt.report.entity.ReportTypeEntity;
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
import org.junit.Test;

/**
 *
 * @author jianying9
 */
public class ReportTypeInitTest extends AbstractServerTest {

    public ReportTypeInitTest() {
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
    public void check() throws IOException {
        EsAdminContextImpl ctx = EsAdminContextImpl.getInstance(ApplicationContext.CONTEXT);
        EsEntityDao<ReportTypeEntity> reportTypeEntityDao = ctx.getEsEntityDao(ReportTypeEntity.class);
        //
        File dir = new File("/Users/jianying9/NetBeansProjects/mt-server/src/test/resources/agreement/");
        String text;
        String type;
        String category;
        String title;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> modelMap;
        Map<String, Object> dataMap;
        if (dir.isDirectory()) {
            File[] fileArray = dir.listFiles();
            for (File file : fileArray) {
                text = FileUtils.readFileToString(file);
                text = text.replaceAll("\n", "");
                text = text.replaceAll(" ", "");
                modelMap = mapper.readValue(text, Map.class);
                type = MapUtils.getStringValue(modelMap, "type");
                category = MapUtils.getStringValue(modelMap, "category");
                title = MapUtils.getStringValue(modelMap, "title");
                //
                dataMap = new HashMap();
                dataMap.put("type", type);
                dataMap.put("category", category);
                dataMap.put("title", title);
                dataMap.put("model", text);
                reportTypeEntityDao.insert(dataMap);
            }
        }
    }
}
