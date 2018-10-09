package com.feihua.framework.base.modules.calendar.dto;

import com.feihua.utils.calendar.LunarDate;
import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Date;

/**
 * Created by yangwei
 * Created at 2018/1/9 16:37
 */
public class SearchCalendarConditionDto extends BaseConditionDto {

    private LunarDate lunarStart;
    private LunarDate lunarEnd;

    private Date start;
    private Date end;
    /**
     * 是否农历日期
     */
    private boolean lunar;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isLunar() {
        return lunar;
    }

    public void setLunar(boolean lunar) {
        this.lunar = lunar;
    }

    public LunarDate getLunarStart() {
        return lunarStart;
    }

    public void setLunarStart(LunarDate lunarStart) {
        this.lunarStart = lunarStart;
    }

    public LunarDate getLunarEnd() {
        return lunarEnd;
    }

    public void setLunarEnd(LunarDate lunarEnd) {
        this.lunarEnd = lunarEnd;
    }
}
