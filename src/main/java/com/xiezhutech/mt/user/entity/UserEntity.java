package com.xiezhutech.mt.user.entity;

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
        table = TableNames.USER
)
public class UserEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "用户id")
    private long userId;

    @EsColumnConfig(desc = "用户名称")
    private String userName;

    @EsColumnConfig(desc = "手机号")
    private String mobile;

    @EsColumnConfig(desc = "用户头像")
    private String headUrl;

    @EsColumnConfig(desc = "角色")
    private List<String> roleList;

    @EsColumnConfig(desc = "组织id集合")
    private List<String> orgIdList;

    @EsColumnConfig(desc = "创建时间")
    private long createTime;

    @EsColumnConfig(desc = "gcp用户id")
    private long gcpUserId;

    @EsColumnConfig(desc = "是否是客户")
    private boolean customer;

    @EsColumnConfig(desc = "移动端sid")
    private String mobileSid;
    //
    @EsColumnConfig(desc = "其它端sid")
    private String otherSid;

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public long getCreateTime() {
        return createTime;
    }

    public List<String> getOrgIdList() {
        return orgIdList;
    }

    public boolean isCustomer() {
        return this.customer;
    }

    public String getMobileSid() {
        return mobileSid;
    }

    public String getOtherSid() {
        return otherSid;
    }

    public long getGcpUserId() {
        return gcpUserId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

}
