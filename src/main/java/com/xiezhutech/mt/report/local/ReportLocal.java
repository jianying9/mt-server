package com.xiezhutech.mt.report.local;

import com.wolf.framework.local.Local;
import com.xiezhutech.mt.report.entity.ReportEntity;
import com.xiezhutech.mt.report.entity.ReportTypeEntity;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 *
 * @author jianying9
 */
public interface ReportLocal extends Local {

    public List<ReportTypeEntity> queryReprotTypeList();

    public ReportTypeEntity queryReportType(String type);

    public long addReport(Map<String, Object> dataMap);

    public void updateReport(Map<String, Object> dataMap);

    public ReportEntity queryReport(long reportId);

    public List<ReportEntity> searchReport(QueryBuilder queyrBuilder, SortBuilder sortBuilder, int from, int size);

}
