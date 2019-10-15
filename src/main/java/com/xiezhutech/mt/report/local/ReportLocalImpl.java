package com.xiezhutech.mt.report.local;

import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.wolf.framework.dao.elasticsearch.annotation.InjectEsEntityDao;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.local.LocalServiceConfig;
import com.xiezhutech.mt.config.TableNames;
import com.xiezhutech.mt.report.entity.ReportEntity;
import com.xiezhutech.mt.report.entity.ReportTypeEntity;
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
public class ReportLocalImpl implements ReportLocal {

    @InjectEsEntityDao
    private EsEntityDao<ReportTypeEntity> reportTypeEntityDao;

    @InjectEsEntityDao
    private EsEntityDao<ReportEntity> reportEntityDao;

    @InjectLocalService
    private SequenceLocal sequenceLocal;

    @Override
    public void init() {
    }

    @Override
    public List<ReportTypeEntity> queryReprotTypeList() {
        return this.reportTypeEntityDao.search(0, 200);
    }

    @Override
    public ReportTypeEntity queryReportType(String type) {
        return this.reportTypeEntityDao.inquireByKey(type);
    }

    @Override
    public long addReport(Map<String, Object> dataMap) {
        long reportId = this.sequenceLocal.nextValue(TableNames.REPORT);
        dataMap.put("reportId", reportId);
        dataMap.put("state", ReportStates.WAIT);
        dataMap.put("checkId", 0);
        this.reportEntityDao.insert(dataMap);
        return reportId;
    }

    @Override
    public void updateReport(Map<String, Object> dataMap) {
        this.reportEntityDao.update(dataMap);
    }

    @Override
    public ReportEntity queryReport(long reportId) {
        return this.reportEntityDao.inquireByKey(reportId);
    }

    @Override
    public List<ReportEntity> searchReport(QueryBuilder queyrBuilder, SortBuilder sortBuilder, int from, int size) {
        return this.reportEntityDao.search(queyrBuilder, sortBuilder, from, size);
    }

}
