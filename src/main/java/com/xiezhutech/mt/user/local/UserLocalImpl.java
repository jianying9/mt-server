package com.xiezhutech.mt.user.local;

import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.wolf.framework.dao.elasticsearch.annotation.InjectEsEntityDao;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.framework.utils.EntityUtils;
import com.xiezhutech.mt.config.TableNames;
import com.xiezhutech.mt.sequence.local.SequenceLocal;
import com.xiezhutech.mt.user.entity.UserEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
 *
 * @author jianying9
 */
@LocalServiceConfig
public class UserLocalImpl implements UserLocal {

    @InjectEsEntityDao
    private EsEntityDao<UserEntity> userEntityDao;

    @InjectLocalService
    private SequenceLocal sequenceLocal;

    @Override
    public void init() {
    }

    @Override
    public UserEntity queryUser(long userId) {
        return this.userEntityDao.inquireByKey(userId);
    }

    @Override
    public UserEntity addCustomerUser(Map<String, Object> dataMap) {
        long userId = this.sequenceLocal.nextValue(TableNames.USER);
        dataMap.put("userId", userId);
        dataMap.put("createTime", System.currentTimeMillis());
        dataMap.put("customer", true);
        return this.userEntityDao.insertAndInquire(dataMap);
    }

    @Override
    public UserEntity addUser(Map<String, Object> dataMap) {
        long userId = this.sequenceLocal.nextValue(TableNames.USER);
        dataMap.put("userId", userId);
        dataMap.put("createTime", System.currentTimeMillis());
        dataMap.put("customer", false);
        return this.userEntityDao.insertAndInquire(dataMap);
    }

    @Override
    public void updateUser(Map<String, Object> dataMap) {
        this.userEntityDao.update(dataMap);
    }

    @Override
    public List<UserEntity> searchUser(boolean customer, int size) {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("customer", customer);
        return this.userEntityDao.search(termQueryBuilder, 0, size);
    }

    @Override
    public void updateUserMobileSid(long userId, String sid) {
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("userId", userId);
        dataMap.put("mobileSid", sid);
        this.userEntityDao.update(dataMap);
    }

    @Override
    public void updateUserOtherSid(long userId, String sid) {
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("userId", userId);
        dataMap.put("otherSid", sid);
        this.userEntityDao.update(dataMap);
    }

    @Override
    public UserEntity queryUser(long userId, Map<Long, UserEntity> userEntityMap) {
        UserEntity userEntity = userEntityMap.get(userId);
        if (userEntity == null) {
            userEntity = this.queryUser(userId);
            if (userEntity != null) {
                userEntityMap.put(userId, userEntity);
            }
        }
        return userEntity;
    }

    @Override
    public Map<String, Object> getUserSimpleInfoMap(UserEntity userEntity) {
        Map<String, Object> dataMap = EntityUtils.getMap(userEntity);
        if (userEntity.getHeadUrl().isEmpty()) {
            dataMap.put("hearUrl", "http://r.xiezhutech.com/config/provider/mt.png");
        }
        return dataMap;
    }

}
