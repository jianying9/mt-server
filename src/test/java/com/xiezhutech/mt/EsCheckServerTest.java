package com.xiezhutech.mt;

import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.dao.elasticsearch.EsAdminContextImpl;
import com.wolf.framework.dao.elasticsearch.EsEntityDao;
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
public class EsCheckServerTest extends AbstractServerTest {

    public EsCheckServerTest() {
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
        EsAdminContextImpl ctx = EsAdminContextImpl.getInstance(ApplicationContext.CONTEXT);
        Map<String, EsEntityDao> cEntityDaomap = ctx.getEsEntityDao();
        for (EsEntityDao esEntityDao : cEntityDaomap.values()) {
            esEntityDao.check();
        }
//
    }
}
