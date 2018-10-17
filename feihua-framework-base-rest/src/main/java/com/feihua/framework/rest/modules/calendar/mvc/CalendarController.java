package com.feihua.framework.rest.modules.calendar.mvc;

import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.area.dto.SearchAreasConditionDto;
import com.feihua.framework.base.modules.area.po.BaseAreaPo;
import com.feihua.framework.base.modules.calendar.api.ApiCalendarService;
import com.feihua.framework.base.modules.calendar.dto.CalendarDto;
import com.feihua.framework.base.modules.calendar.dto.SearchCalendarConditionDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.area.dto.AddAreaFormDto;
import com.feihua.framework.rest.modules.area.dto.UpdateAreaFormDto;
import com.feihua.framework.rest.modules.calendar.dto.CalendarVo;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 区域接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base")
public class CalendarController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @Autowired
    private ApiCalendarService apiCalendarService;

    /**
     * 单资源，获取日期详情
     * @param date 公历日期
     * @return
     */
    @RequiresPermissions("base:calendar:getByDate")
    @RequestMapping(value = "/calendar/{date}",method = RequestMethod.GET)
    public ResponseEntity getCalendarByDate(@PathVariable Date date){
        ResponseJsonRender resultData=new ResponseJsonRender();
        CalendarDto calendarDto = apiCalendarService.getCalendarByDate(date);
        return super.returnDto(new CalendarVo(calendarDto),resultData);
    }
    /**
     * 单资源，获取日期详情
     * @param lunarDate 农历日期
     * @return
     */
    @RequiresPermissions("base:calendar:getByDate")
    @RequestMapping(value = "/calendar/lunar/{lunarDate}",method = RequestMethod.GET)
    public ResponseEntity getCalendarByLunarDate(@PathVariable String lunarDate){
        ResponseJsonRender resultData=new ResponseJsonRender();
        CalendarDto calendarDto = apiCalendarService.getCalendarByLunarDate(LunarCalendarUtils.stringTolunarDate(lunarDate));
        return super.returnDto(new CalendarVo(calendarDto),resultData);
    }

    /**
     * 获取一整月的数据
     * @param monthDate 公历日期
     * @return
     */
    @RequiresPermissions("base:calendar:getByMonthDate")
    @RequestMapping(value = "/calendar/{monthDate}/dates",method = RequestMethod.GET)
    public ResponseEntity getCalendarsByMonthDate(@PathVariable Date monthDate){
        ResponseJsonRender resultData=new ResponseJsonRender();
        SearchCalendarConditionDto dto = new SearchCalendarConditionDto();
        Date start = CalendarUtils.getFirstDayOfMonth(monthDate);
        Date end = CalendarUtils.getEndDayOfMonth(monthDate);
        dto.setStart(start);
        dto.setEnd(end);

        return super.returnList(doSearch(dto),resultData);
    }

    /**
     * 获取一整月的数据
     * @param lunarMonthDate 农历日期
     * @return
     */
    @RequiresPermissions("base:calendar:getByLunarMonthDate")
    @RequestMapping(value = "/calendar/lunar/{lunarMonthDate}/dates",method = RequestMethod.GET)
    public ResponseEntity getCalendarsByLunarMonthDate(@PathVariable String lunarMonthDate){
        ResponseJsonRender resultData=new ResponseJsonRender();
        SearchCalendarConditionDto dto = new SearchCalendarConditionDto();
        Date monthDate = LunarCalendarUtils.LunarToCalendar(LunarCalendarUtils.stringTolunarDate(lunarMonthDate));
        Date start = CalendarUtils.getFirstDayOfMonth(monthDate);
        Date end = CalendarUtils.getEndDayOfMonth(monthDate);
        dto.setStart(start);
        dto.setEnd(end);

        return super.returnList(doSearch(dto),resultData);
    }
    /**
     * 复数资源，搜索区域
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:calendars:search")
    @RequestMapping(value = "/calendars",method = RequestMethod.GET)
    public ResponseEntity searchAreas(SearchCalendarConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();

        return super.returnList(doSearch(dto),resultData);
    }

    private List<CalendarVo> doSearch(SearchCalendarConditionDto dto){
        List<CalendarDto> list = apiCalendarService.searchCalendars(dto);
        List<CalendarVo> rList = null;
        if(list != null && !list.isEmpty()){
            rList = new ArrayList<>(list.size());
            for (CalendarDto calendarDto : list) {
                rList.add(new CalendarVo(calendarDto));
            }
        }
        return rList;
    }
}
