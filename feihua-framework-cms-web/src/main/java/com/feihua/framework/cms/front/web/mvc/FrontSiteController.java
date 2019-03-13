package com.feihua.framework.cms.front.web.mvc;

import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.api.ApiCmsSiteIndexPageViewPoService;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsSiteDto;
import com.feihua.framework.cms.po.CmsSiteIndexPageViewPo;
import com.feihua.framework.cms.po.CmsSitePo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
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
 * cms站点前端页面访问入口
 * Created by yangwei
 */
@Controller
@RequestMapping("/cms/front")
public class FrontSiteController extends FrontBaseController {

    private static Logger logger = LoggerFactory.getLogger(FrontSiteController.class);
    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsSiteIndexPageViewPoService apiCmsSiteIndexPageViewPoService;


    /**
     * 单资源，更新访问数量
     * @param id
     * @return
     */
    @RequestMapping(value = "/site/{id}/count",method = RequestMethod.PUT)
    public ResponseEntity contentCount(@PathVariable String id, HttpServletRequest request){
        logger.info("更新站点访问量开始");
        logger.info("站点id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        CmsSitePo cmsSitePo = apiCmsSitePoService.selectByPrimaryKeySimple(id,false);
        if (cmsSitePo == null) {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新站点访问量结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }

        Date today = new Date();
        String host = RequestUtils.getRemoteAddr(request);
        int existHost = apiCmsSiteIndexPageViewPoService.existHost(host,today);
        Cookie cookie = RequestUtils.getCookieByName(self_session_id_name,request);

        int existCookie = 0;
        if (cookie != null) {
            String cookieValue = cookie.getValue();
            existCookie = apiCmsSiteIndexPageViewPoService.existCookie(cookieValue,today);
        }


        //添加访问记录
        CmsSiteIndexPageViewPo cmsContentPageViewPo = new CmsSiteIndexPageViewPo();

        cmsContentPageViewPo.setSiteId(id);
        cmsContentPageViewPo.setHost(RequestUtils.getRemoteAddr(request));
        if (cookie != null) {
            cmsContentPageViewPo.setCookie(cookie.getValue());
        }
        String userId = getLoginUserId();
        if(StringUtils.isEmpty(userId)){
            userId = BasePo.DEFAULT_USER_ID;
        }
        cmsContentPageViewPo = apiCmsSiteIndexPageViewPoService.preInsert(cmsContentPageViewPo,userId);
        apiCmsSiteIndexPageViewPoService.insert(cmsContentPageViewPo);

        // 表单值设置
        CmsSitePo basePo = new CmsSitePo();
        // id
        basePo.setId(id);

        basePo.setPv(cmsSitePo.getPv().intValue() + 1);
        if(existHost == 0){
            basePo.setIv(cmsSitePo.getIv().intValue() + 1);
        }
        if(existCookie == 0){
            basePo.setIv(cmsSitePo.getUv().intValue() + 1);
        }

        int r = apiCmsSitePoService.updateByPrimaryKeySelective(basePo);
        // 更新成功，已被成功创建
        logger.info("更新的站点id:{}",id);
        logger.info("更新站点访问量结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }

    /**
     * 站点查询
     * @param siteId
     * @param isMain
     * @return
     */
    @RequestMapping(value = "/site/{siteId}",method = RequestMethod.GET)
    public ResponseEntity sitePageList(@PathVariable String siteId, String isMain){
        ResponseJsonRender resultData=new ResponseJsonRender();

        CmsSitePo cmsSitePoConditionPo = new CmsSitePo();
        cmsSitePoConditionPo.setDelFlag(BasePo.YesNo.N.name());
        cmsSitePoConditionPo.setId(siteId);
        cmsSitePoConditionPo.setIsMain(isMain);

        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

        PageResultDto<CmsSiteDto> list = apiCmsSitePoService.selectList(cmsSitePoConditionPo,pageAndOrderbyParamDto);

        return returnPageResultDto(list,resultData);
    }
}
