package com.xiezhutech.mt.org.local;

import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.wolf.framework.dao.elasticsearch.annotation.InjectEsEntityDao;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.local.LocalServiceConfig;
import com.xiezhutech.mt.config.TableNames;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.entity.OrgJoinEntity;
import com.xiezhutech.mt.org.entity.OrgJoinHistoryEntity;
import com.xiezhutech.mt.sequence.local.SequenceLocal;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 *
 * @author jianying9
 */
@LocalServiceConfig
public class OrgLocalImpl implements OrgLocal {

    @InjectEsEntityDao
    private EsEntityDao<OrgEntity> orgEntityDao;

    @InjectEsEntityDao
    private EsEntityDao<OrgJoinEntity> orgJoinEntityDao;

    @InjectEsEntityDao
    private EsEntityDao<OrgJoinHistoryEntity> orgJoinHistoryEntityDao;

    @InjectLocalService
    private SequenceLocal sequenceLocal;

    @Override
    public void init() {
    }

    @Override
    public OrgEntity queryOrg(String orgId) {
        return this.orgEntityDao.inquireByKey(orgId);
    }

    @Override
    public OrgEntity addOrg(Map<String, Object> dataMap) {
        dataMap.put("enabled", false);
        dataMap.put("createTime", System.currentTimeMillis());
        return this.orgEntityDao.insertAndInquire(dataMap);
    }

    @Override
    public void updateOrg(Map<String, Object> dataMap) {
        this.orgEntityDao.update(dataMap);
    }

    @Override
    public List<OrgEntity> searchOrg(QueryBuilder queryBuilder, SortBuilder sortBuilder, int from, int size) {
        return this.orgEntityDao.search(queryBuilder, sortBuilder, from, size);
    }

    @Override
    public void addOrgJoin(Map<String, Object> dataMap) {
        dataMap.put("applyTime", System.currentTimeMillis());
        this.orgJoinEntityDao.insert(dataMap);
    }

    @Override
    public OrgJoinEntity queryOrgJoin(String joinId) {
        return this.orgJoinEntityDao.inquireByKey(joinId);
    }

    @Override
    public List<OrgJoinEntity> searchOrgJoin(int from, int size) {
        return this.orgJoinEntityDao.search(from, size);
    }

    @Override
    public void deleteOrgJoin(String joinId) {
        this.orgJoinEntityDao.delete(joinId);
    }

    @Override
    public void addOrgJoinHistory(Map<String, Object> dataMap) {
        long historyId = this.sequenceLocal.nextValue(TableNames.ORG_JOIN_HISTORY);
        dataMap.put("historyId", historyId);
        this.orgJoinHistoryEntityDao.insert(dataMap);
    }

    @Override
    public List<OrgJoinHistoryEntity> searchOrgJoinHistory(QueryBuilder queryBuilder, SortBuilder sortBuilder, int from, int size) {
        return this.orgJoinHistoryEntityDao.search(queryBuilder, sortBuilder, from, size);
    }

}
