package com.xiezhutech.mt.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理辅助类,用于Date类型和String类型时间转换
 *
 * @author aladdin
 */
public final class DateUtils {

    /**
     * 日志对象
     */
    public final static SimpleDateFormat FM_YYMM = new SimpleDateFormat("yyyyMM");
    public final static SimpleDateFormat FM_YYMMDD = new SimpleDateFormat("yyyyMMdd");
    public final static SimpleDateFormat FM_YY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat FM_YY_MM = new SimpleDateFormat("yyyy-MM");
    public final static SimpleDateFormat FM_YY_MM_DD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat FM_YY_MM_DD_HHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public final static SimpleDateFormat FM_HHMM = new SimpleDateFormat("HH:mm");
    public final static SimpleDateFormat FM_HH = new SimpleDateFormat("HH");

    private DateUtils() {
    }

    public static String convertToYYYY_MM_DD_HHmmSS(long milliseconds) {
        Date date = new Date(milliseconds);
        return DateUtils.FM_YY_MM_DD_HHMMSS.format(date);
    }

}
