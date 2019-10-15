package com.xiezhutech.mt.account.local;

import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.wolf.framework.dao.elasticsearch.annotation.InjectEsEntityDao;
import com.wolf.framework.local.LocalServiceConfig;
import com.xiezhutech.mt.account.entity.AccountEntity;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.account.entity.TokenEntity;
import com.xiezhutech.mt.util.MtUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@LocalServiceConfig
public class AccountLocalImpl implements AccountLocal {

    @InjectEsEntityDao
    private EsEntityDao<AccountEntity> accountEntityDao;

    @InjectEsEntityDao
    private EsEntityDao<SessionEntity> sessionEntityDao;

    @InjectEsEntityDao
    private EsEntityDao<TokenEntity> tokenEntityDao;

    @Override
    public void init() {
    }

    @Override
    public void addAccount(Map<String, Object> dataMap) {
        this.accountEntityDao.insert(dataMap);
    }

    @Override
    public AccountEntity queryAccount(String account) {
        return this.accountEntityDao.inquireByKey(account);
    }

    @Override
    public String addMobileSession(long userId) {
        String sid = MtUtils.createUid();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("sid", sid);
        dataMap.put("userId", userId);
        dataMap.put("type", SessionTypes.ACCOUNT_TYPE_MOBILE);
        dataMap.put("lastTime", System.currentTimeMillis());
        this.sessionEntityDao.insert(dataMap);
        return sid;
    }

    @Override
    public String addOtherSession(long userId) {
        String sid = MtUtils.createUid();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("sid", sid);
        dataMap.put("userId", userId);
        dataMap.put("type", SessionTypes.ACCOUNT_TYPE_OTHER);
        dataMap.put("lastTime", System.currentTimeMillis());
        this.sessionEntityDao.insert(dataMap);
        return sid;
    }

    @Override
    public SessionEntity querySession(String sid) {
        return this.sessionEntityDao.inquireByKey(sid);
    }

    @Override
    public void updateSessionLastTime(String sid, long lastTime) {
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("sid", sid);
        dataMap.put("lastTime", lastTime);
        this.sessionEntityDao.update(dataMap);
    }

    @Override
    public TokenEntity addToken() {
        String token = MtUtils.createUid();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", token);
        long expiredTime = System.currentTimeMillis() + 180000;
        dataMap.put("expiredTime", expiredTime);
        return this.tokenEntityDao.insertAndInquire(dataMap);
    }

    @Override
    public TokenEntity queryToken(String token) {
        return this.tokenEntityDao.inquireByKey(token);
    }

    @Override
    public void updateToken(String token, long userId) {
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", token);
        dataMap.put("userId", userId);
        long expiredTime = System.currentTimeMillis() + 180000;
        dataMap.put("expiredTime", expiredTime);
        this.tokenEntityDao.update(dataMap);
    }

}
