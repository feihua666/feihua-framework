package com.feihua.framework.base.modules.calendar.api;

import com.feihua.framework.base.modules.calendar.dto.CalendarDto;
import com.feihua.framework.base.modules.calendar.dto.SearchCalendarConditionDto;
import com.feihua.utils.calendar.LunarDate;

import java.util.Date;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/9/20 10:42
 */
public interface ApiCalendarService {

    /**
     * 根据公历日期获取详细内容
     * @param date 公历日期
     * @return
     */
    public CalendarDto getCalendarByDate(Date date);

    /**
     * 根据农历日期获取详细内容
     * 如果给定的农历日期是闰月的话，则得到的公历日期是闰月第一个月对应的日期
     * @param lunarDate 农历日期
     * @return
     */
    public CalendarDto getCalendarByLunarDate(LunarDate lunarDate);

    /**
     * 根据公历日期获取一段时间的连续详细内容
     * @param start 公历开始日期
     * @param end 公历结束日期
     * @return 包括开始日期和结束日期,如果开始日期大于结束日期，返回null
     */
    public List<CalendarDto> getCalendarsByDate(Date start,Date end);

    /**
     * 根据农历日期获取一段时间的连接详细内容
     * 如果给定的农历日期是闰月的话，则得到的公历日期是闰月第一个月对应的日期
     * @param lunarStart 农历开始日期
     * @param lunarEnd 农历结束日期
     * @return 包括开始日期和结束日期,如果开始日期大于结束日期，返回null
     */
    public List<CalendarDto> getCalendarsByLunarDate(LunarDate lunarStart,LunarDate lunarEnd);

    /**
     * 搜索
     * @param dto
     * @return
     */
    public List<CalendarDto> searchCalendars(SearchCalendarConditionDto dto);
}
