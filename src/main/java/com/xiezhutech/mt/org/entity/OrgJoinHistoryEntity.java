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
        table = TableNames.ORG_JOIN_HISTORY
)
public class OrgJoinHistoryEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "申请id")
    private long historyId;

    @EsColumnConfig(desc = "组织编号")
    private String orgId;

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

    @EsColumnConfig(desc = "是否通过")
    private boolean pass;

    public long getHistoryId() {
        return historyId;
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

    public String getCheckName() {
        return checkName;
    }

    public long getCheckId() {
        return checkId;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public boolean isPass() {
        return pass;
    }

}
