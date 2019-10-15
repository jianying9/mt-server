package com.xiezhutech.mt.report.entity;

import com.wolf.framework.dao.ColumnType;
import com.wolf.framework.dao.Entity;
import com.wolf.framework.dao.elasticsearch.annotation.EsColumnConfig;
import com.wolf.framework.dao.elasticsearch.annotation.EsEntityConfig;
import com.xiezhutech.mt.config.TableNames;

/**
 *
 * @author jianying9
 */
@EsEntityConfig(
        table = TableNames.REPORT
)
public class ReportEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "id")
    private long reportId;

    @EsColumnConfig(desc = "组织id")
    private String orgId;

    @EsColumnConfig(desc = "大类")
    private String category;

    @EsColumnConfig(desc = "类型")
    private String type;

    @EsColumnConfig(desc = "检测内容及结果")
    private String model;

    @EsColumnConfig(desc = "申请人id")
    private long applyId;

    @EsColumnConfig(desc = "申请人")
    private String applyName;

    @EsColumnConfig(desc = "申请人电话")
    private String applyMobile;

    @EsColumnConfig(desc = "申请时间")
    private long applyTime;

    @EsColumnConfig(desc = "核对人")
    private String checkName;

    @EsColumnConfig(desc = "核对人id")
    private long checkId;

    @EsColumnConfig(desc = "审核时间")
    private long checkTime;

    @EsColumnConfig(desc = "状态")
    private String state;

    public long getReportId() {
        return reportId;
    }

    public String getOrgId() {
        return orgId;
    }

    public long getApplyId() {
        return applyId;
    }

    public String getApplyName() {
        return applyName;
    }

    public String getApplyMobile() {
        return applyMobile;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getCheckName() {
        return checkName;
    }

    public long getCheckId() {
        return checkId;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public String getState() {
        return state;
    }

}
