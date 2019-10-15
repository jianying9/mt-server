package com.xiezhutech.mt.org.entity;

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
        table = TableNames.ORG_JOIN
)
public class OrgJoinEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "申请id")
    private String joinId;

    @EsColumnConfig(desc = "组织编号")
    private String orgId;

    @EsColumnConfig(desc = "工程名称")
    private String projectName;

    @EsColumnConfig(desc = "公司名称")
    private String companyName;

    @EsColumnConfig(desc = "施工单位")
    private String constructionName;

    @EsColumnConfig(desc = "见证单位")
    private String witnessName;

    @EsColumnConfig(desc = "申请人id")
    private long applyId;

    @EsColumnConfig(desc = "申请人")
    private String applyName;

    @EsColumnConfig(desc = "申请人电话")
    private String applyMobile;

    @EsColumnConfig(desc = "申请时间")
    private long applyTime;

    public String getJoinId() {
        return joinId;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public String getWitnessName() {
        return witnessName;
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

}
