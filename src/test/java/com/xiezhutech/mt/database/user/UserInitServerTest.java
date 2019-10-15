package com.xiezhutech.mt.database.user;

import com.xiezhutech.mt.*;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.dao.elasticsearch.EsAdminContextImpl;
import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.xiezhutech.mt.user.entity.UserEntity;
import com.xiezhutech.mt.user.local.UserRoles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class UserInitServerTest extends AbstractServerTest {

    public UserInitServerTest() {
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
        EsEntityDao<UserEntity> userEntityDao = ctx.getEsEntityDao(UserEntity.class);
        //
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("userId", 10004);
        dataMap.put("customer", false);
        List<String> roleList = new ArrayList();
        roleList.add(UserRoles.ADMIN);
        roleList.add(UserRoles.PICKED);
        dataMap.put("roleList", roleList);
        userEntityDao.update(dataMap);
    }
}
