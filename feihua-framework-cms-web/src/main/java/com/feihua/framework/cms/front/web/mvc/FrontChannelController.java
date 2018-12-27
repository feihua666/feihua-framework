package com.feihua.framework.cms.front.web.mvc;

import com.feihua.framework.cms.api.ApiCmsChannelPageViewPoService;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.po.CmsChannelPageViewPo;
import com.feihua.framework.cms.po.CmsChannelPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * cms栏目前端页面访问入口
 * Created by yangwei
 */
@Controller
@RequestMapping("/cms/front")
public class FrontChannelController extends FrontBaseController {

    private static Logger logger = LoggerFactory.getLogger(FrontChannelController.class);

    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    @Autowired
    private ApiCmsChannelPageViewPoService apiCmsChannelPageViewPoService;

    /**
     * 单资源，更新访问数量
     * @param id
     * @return
     */
    @RequestMapping(value = "/channel/{id}/count",method = RequestMethod.PUT)
    public ResponseEntity channelCount(@PathVariable String id, HttpServletRequest request){
        logger.info("更新栏目访问量开始");
        logger.info("栏目id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        CmsChannelPo cmsChannelPo = apiCmsChannelPoService.selectByPrimaryKeySimple(id,false);
        if (cmsChannelPo == null) {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新栏目访问量结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }

        Date today = new Date();
        String host = RequestUtils.getRemoteAddr(request);
        int existHost = apiCmsChannelPageViewPoService.existHost(host,today);
        Cookie cookie = RequestUtils.getCookieByName(self_session_id_name,request);

        int existCookie = 0;
        if (cookie != null) {
            String cookieValue = cookie.getValue();
            existCookie = apiCmsChannelPageViewPoService.existCookie(cookieValue,today);
        }


        //添加访问记录
        CmsChannelPageViewPo cmsChannelPageViewPo = new CmsChannelPageViewPo();
        cmsChannelPageViewPo.setChannelId(id);
        cmsChannelPageViewPo.setSiteId(cmsChannelPo.getSiteId());
        cmsChannelPageViewPo.setHost(RequestUtils.getRemoteAddr(request));
        if (cookie != null) {
            cmsChannelPageViewPo.setCookie(cookie.getValue());
        }
        String userId = getLoginUserId();
        if(StringUtils.isEmpty(userId)){
            userId = BasePo.DEFAULT_USER_ID;
        }
        cmsChannelPageViewPo = apiCmsChannelPageViewPoService.preInsert(cmsChannelPageViewPo,userId);
        apiCmsChannelPageViewPoService.insert(cmsChannelPageViewPo);

        // 表单值设置
        CmsChannelPo basePo = new CmsChannelPo();
        // id
        basePo.setId(id);

        basePo.setPv(cmsChannelPo.getPv().intValue() + 1);
        if(existHost == 0){
            basePo.setUv(cmsChannelPo.getIv().intValue() + 1);
        }
        if(existCookie == 0){
            basePo.setUv(cmsChannelPo.getUv().intValue() + 1);
        }

        int r = apiCmsChannelPoService.updateByPrimaryKeySelective(basePo);
        // 更新成功，已被成功创建
        logger.info("更新的栏目id:{}",id);
        logger.info("更新栏目访问量结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
}
