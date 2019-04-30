package com.feihua.framework.statistic.mvc;

import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.statistic.api.ApiStatisticUserAreaCountPoService;
import com.feihua.framework.statistic.api.ApiStatisticUserClientCountPoService;
import com.feihua.framework.statistic.api.ApiStatisticUserCountPoService;
import com.feihua.framework.statistic.dto.StatisticUserAreaCountDto;
import com.feihua.framework.statistic.dto.StatisticUserClientCountDto;
import com.feihua.framework.statistic.dto.StatisticUserCountDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户相关统计
 * Created by yangwei
 * Created at 2019/4/24 16:23
 */
@RestController
@RequestMapping("/statistic")
public class UserStatisticController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(UserStatisticController.class);

    @Autowired
    private ApiStatisticUserCountPoService apiStatisticUserCountPoService;
    @Autowired
    private ApiStatisticUserAreaCountPoService apiStatisticUserAreaCountPoService;
    @Autowired
    private ApiStatisticUserClientCountPoService apiStatisticUserClientCountPoService;

    @RepeatFormValidator
    @RequiresPermissions("statistic:user:count")
    @RequestMapping(value = "/user/count",method = RequestMethod.GET)
    public ResponseEntity userCount(){
        ResponseJsonRender resultData=new ResponseJsonRender();
        List<StatisticUserCountDto> userCountDtos = apiStatisticUserCountPoService.selectAll(false);
        return returnList(userCountDtos,resultData);
    }
    @RepeatFormValidator
    @RequiresPermissions("statistic:user:area:count")
    @RequestMapping(value = "/user/area/count",method = RequestMethod.GET)
    public ResponseEntity userAreaCount(){
        ResponseJsonRender resultData=new ResponseJsonRender();
        List<StatisticUserAreaCountDto> userAreaCountDtos = apiStatisticUserAreaCountPoService.selectAll(false);
        return returnList(userAreaCountDtos,resultData);
    }
    @RepeatFormValidator
    @RequiresPermissions("statistic:user:client:count")
    @RequestMapping(value = "/user/client/count",method = RequestMethod.GET)
    public ResponseEntity userClientCount(){
        ResponseJsonRender resultData=new ResponseJsonRender();
        List<StatisticUserClientCountDto> userClientCountDtos = apiStatisticUserClientCountPoService.selectAll(false);
        return returnList(userClientCountDtos,resultData);
    }
}
