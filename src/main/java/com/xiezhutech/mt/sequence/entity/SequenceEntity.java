package com.xiezhutech.mt.sequence.entity;

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
        table = TableNames.SEQUENCE
)
public class SequenceEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "用户id")
    private String name;

    @EsColumnConfig(desc = "用户名称")
    private long value;

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

}
