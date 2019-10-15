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
        table = TableNames.SESSION
)
public class SessionEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "sid")
    private String sid;

    @EsColumnConfig(desc = "用户id")
    private long userId;

    @EsColumnConfig(desc = "session类型:mobile,other")
    private String type;
    //
    @EsColumnConfig(desc = "最后使用时间")
    private long lastTime;

    public String getSid() {
        return sid;
    }

    public long getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public long getLastTime() {
        return lastTime;
    }

}
