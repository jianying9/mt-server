package com.xiezhutech.mt.org.local;

import com.wolf.framework.local.Local;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.org.entity.OrgJoinEntity;
import com.xiezhutech.mt.org.entity.OrgJoinHistoryEntity;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 *
 * @author jianying9
 */
public interface OrgLocal extends Local {

    public OrgEntity queryOrg(String orgId);

    public OrgEntity addOrg(Map<String, Object> dataMap);

    public void updateOrg(Map<String, Object> dataMap);

    public List<OrgEntity> searchOrg(QueryBuilder queryBuilder, SortBuilder sortBuilder, int from, int size);

    public void addOrgJoin(Map<String, Object> dataMap);

    public OrgJoinEntity queryOrgJoin(String joinId);

    public List<OrgJoinEntity> searchOrgJoin(int from, int size);

    public void deleteOrgJoin(String joinId);

    public void addOrgJoinHistory(Map<String, Object> dataMap);

    public List<OrgJoinHistoryEntity> searchOrgJoinHistory(QueryBuilder queryBuilder, SortBuilder sortBuilder, int from, int size);

}
