package com.xiezhutech.mt.account.entity;

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
        table = TableNames.ACCOUNT
)
public class AccountEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "account")
    private String account;

    @EsColumnConfig(desc = "用户id")
    private long userId;

    @EsColumnConfig(desc = "创建时间")
    private long createTime;

    public String getAccount() {
        return account;
    }

    public long getUserId() {
        return userId;
    }

    public long getCreateTime() {
        return createTime;
    }

}
