package com.canapolis.solucaosaude.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtil {

    private static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forID("America/Bahia");

    public static Date newDate() {
        return DateTime.now().withZone(DATE_TIME_ZONE).toDate();
    }

    public static String formataData(Date dataParam, String formatParam) {
        if (ObjectUtils.isEmpty(dataParam)) {
            return null;
        }
        String df = new SimpleDateFormat(formatParam).format(dataParam);
        return df;
    }
}
