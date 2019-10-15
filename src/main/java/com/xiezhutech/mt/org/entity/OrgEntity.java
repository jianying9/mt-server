package com.xiezhutech.mt.org.entity;

import com.wolf.framework.dao.ColumnType;
import com.wolf.framework.dao.Entity;
import com.wolf.framework.dao.elasticsearch.annotation.EsColumnConfig;
import com.wolf.framework.dao.elasticsearch.annotation.EsEntityConfig;
import com.xiezhutech.mt.config.TableNames;
import java.util.List;

/**
 *
 * @author jianying9
 */
@EsEntityConfig(
        table = TableNames.ORG
)
public class OrgEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "组织编号")
    private String orgId;

    @EsColumnConfig(desc = "工程名称", analyzer = true)
    private String projectName;

    @EsColumnConfig(desc = "公司名称", analyzer = true)
    private String companyName;

    @EsColumnConfig(desc = "施工单位", analyzer = true)
    private String constructionName;

    @EsColumnConfig(desc = "见证单位", analyzer = true)
    private String witnessName;

    @EsColumnConfig(desc = "维度")
    private double latitude;

    @EsColumnConfig(desc = "经度")
    private double longitude;

    @EsColumnConfig(desc = "位置", analyzer = true)
    private String location;

    @EsColumnConfig(desc = "位置截图")
    private String locationSnapshot;

    @EsColumnConfig(desc = "县/区")
    private String county;

    @EsColumnConfig(desc = "市")
    private String city;

    @EsColumnConfig(desc = "省")
    private String province;

    @EsColumnConfig(desc = "用户id集合")
    private List<Long> userIdList;
    
    @EsColumnConfig(desc = "是否可用")
    private boolean enabled;

    @EsColumnConfig(desc = "创建时间")
    private long createTime;

    @EsColumnConfig(desc = "客户id")
    private long gcpOrgId;

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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public String getLocationSnapshot() {
        return locationSnapshot;
    }

    public String getCounty() {
        return county;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public long getCreateTime() {
        return createTime;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public long getGcpOrgId() {
        return gcpOrgId;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
