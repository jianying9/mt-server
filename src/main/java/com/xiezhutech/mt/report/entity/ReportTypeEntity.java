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
        table = TableNames.REPORT_TYPE
)
public class ReportTypeEntity implements Entity {

    @EsColumnConfig(columnType = ColumnType.KEY, desc = "类型")
    private String type;

    @EsColumnConfig(desc = "标题")
    private String title;

    @EsColumnConfig(desc = "大类")
    private String category;

    @EsColumnConfig(desc = "检测内容及结果")
    private String model;

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getTitle() {
        return title;
    }

}
