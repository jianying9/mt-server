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
        table = TableNames.TOKEN
)
public class TokenEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "token")
    private String token;

    @EsColumnConfig(desc = "失效时间")
    private long expiredTime;

    @EsColumnConfig(desc = "userId")
    private long userId;

    public String getToken() {
        return token;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public long getUserId() {
        return userId;
    }

}
