package com.feihua.framework.rest;

import com.feihua.utils.calendar.CalendarUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 将页面的时间统一转换为date
 * Created by yw on 2016/1/28.
 */
public class DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        return CalendarUtils.StringToDate(source, CalendarUtils.getDateStyle(source));
    }

}
