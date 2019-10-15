package com.xiezhutech.mt;

import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.dao.elasticsearch.EsAdminContextImpl;
import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.xiezhutech.mt.config.TableNames;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jianying9
 */
public class EsDeleteDevelopTest extends AbstractServerTest {

    public EsDeleteDevelopTest() {
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
        Set<String> safeSet = new HashSet();
        safeSet.add(TableNames.REPORT);
        safeSet.add(TableNames.REPORT_TYPE);
        EsAdminContextImpl ctx = EsAdminContextImpl.getInstance(ApplicationContext.CONTEXT);
        Map<String, EsEntityDao> cEntityDaomap = ctx.getEsEntityDao();
        for (EsEntityDao esEntityDao : cEntityDaomap.values()) {
            if (safeSet.contains(esEntityDao.getType())) {
                IndicesExistsResponse indicesExistsResponse = ctx.getTransportClient().admin().indices().prepareExists(esEntityDao.getIndex()).get();
                if (indicesExistsResponse.isExists()) {
                    ctx.getTransportClient().admin().indices().prepareDelete(esEntityDao.getIndex()).get();
                }
            }
        }
//
    }
}
