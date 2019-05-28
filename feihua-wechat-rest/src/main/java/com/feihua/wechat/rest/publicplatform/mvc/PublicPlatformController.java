package com.feihua.wechat.rest.publicplatform.mvc;


import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.properties.PropertiesUtils;
import com.feihua.wechat.CommonConstants;
import com.feihua.wechat.common.api.ApiWeixinUserListener;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.dto.WeixinUserDto;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.feihua.wechat.publicplatform.PublicConstants;
import com.feihua.wechat.publicplatform.PublicUtils;
import com.feihua.wechat.publicplatform.api.ApiPublicPlatformService;
import com.feihua.wechat.publicplatform.dto.AuthorizeAccessToken;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/4/28 14:51
 */
@Controller
@RequestMapping("/publicplatform")
public class PublicPlatformController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(PublicPlatformController.class);


    @Autowired
    private ApiPublicPlatformService apiPublicPlatformService;

    @Autowired
    private ApiWeixinUserPoService apiWeixinUserPoService;
    @Autowired
    ApiWeixinUserListener apiWeixinUserListener;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    /**
     * 微信平台对接接口
     * @param request
     * @param response
     * @param which 支持多个公众号，标识哪一个，如配置：publicplatform.hz.appID，则which应该是hz
     */
    @RequestMapping(value = "/service/core/{which}",method = RequestMethod.POST,produces = "application/xml;charset=UTF-8")
    public void message(HttpServletRequest request, HttpServletResponse response, @PathVariable String which){
        InputStream inputStream = null;
        PrintWriter out = null;
        try{
            // 从请求中读取整个post数据
            inputStream = request.getInputStream();
            String postData = IOUtils.toString(inputStream, "UTF-8");
            // 调用核心业务类接收消息、处理消息
            String respMessage = apiPublicPlatformService.processMsg(postData,which);

            // 响应消息
            out = response.getWriter();
            out.print(respMessage);

        }catch (Exception e){
            logger.error("Error occurs when process wechat request:", e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if(out!=null)
                out.close();
        }
    }
    /**
     * 微信服务器消息验证
     * @param signature
     *            微信加密签名
     * @param echostr
     *            随机字符串
     * @param timestamp
     *            时间戳
     * @param nonce
     *            随机数
     */
    @RequestMapping(value = "/service/core/{which}",method = RequestMethod.GET)
    @ResponseBody
    public String validate(String signature,
                           String echostr, String timestamp, String nonce, @PathVariable String which) {
        // 确认请求是否来自微信
        String result = PublicUtils.validate(signature, echostr, timestamp, nonce,
                PublicUtils.getAppToken(which));

        return result;
    }


    /**
     * 获取js接口调用配置信息
     *
     * @return
     */
    @RequestMapping(value = "getJsInterfaceConfig",method = RequestMethod.GET)
    public ResponseEntity getJsInterfaceConfig(String realUrl,String which) {
		logger.info("====》getJsInterfaceConfig: realUrl：{},which：{}，userId：{}",realUrl,which,getLoginUserId());
        ResponseJsonRender resultData = new ResponseJsonRender("成功");
        resultData.setData(PublicUtils.getJsInterfaceConfig(PublicUtils.getJsapiTicket(which), realUrl, which));

        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    /**
     * 跳转到用户授权页面
     * @param which
     * @return
     */
    @RequestMapping(value = "redirectToAuthAuthorizePage/{which}",method = RequestMethod.GET)
        public String redirectToAuthAuthorizePage(@PathVariable String which,String redirectUrl,String scope,String state){

        String url = PublicConstants.AUTH_REDIRECT_URL.replace(CommonConstants.PARAM_APPID,PublicUtils.getAppid(which))
                .replace(PublicConstants.PARAM_REDIRECT_URI,redirectUrl)
                .replace(PublicConstants.PARAM_AUTHORIZE_SCOPE,scope)
                .replace(PublicConstants.PARAM_AUTHORIZE_STATE,state)
                ;
        return "redirect:"+url;
    }

    /**
     * 获取用户授权页面url,拿到url后用户自行跳转
     * @param which
     * @param redirectUrl
     * @param scope
     * @param state
     * @return
     */
    @RequestMapping(value = "authAuthorizePageUrl/{which}",method = RequestMethod.GET)
    public ResponseEntity authAuthorizePageUrl(@PathVariable String which,String redirectUrl,String scope,String state) {
        ResponseJsonRender resultData = new ResponseJsonRender("成功");
        String url = PublicConstants.AUTH_REDIRECT_URL.replace(CommonConstants.PARAM_APPID,PublicUtils.getAppid(which))
                .replace(PublicConstants.PARAM_REDIRECT_URI,redirectUrl)
                .replace(PublicConstants.PARAM_AUTHORIZE_SCOPE,scope)
                .replace(PublicConstants.PARAM_AUTHORIZE_STATE,state)
                ;
        resultData.setData(url);

        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 用户授权后，获取用户信息,这个请求是微信重定向来的
     * @param code 得到的code
     * @param state
     * @param which
     * @return
     */
    @RequestMapping(value = "getAuthUserInfo/{which}/{client}",method = RequestMethod.GET)
    public String getAuthUserInfo(String code,String state,@PathVariable String which,@PathVariable String client,String redirectUrl) {
        wxuserInfo(code,state,which,client);

        return "redirect:" + redirectUrl;
    }
    /**
     * 用户授权后，获取用户信息
     * @param code 得到的code
     * @param state
     * @param which
     * @return
     */
    @RequestMapping(value = "authUserInfo/{which}/{client}",method = RequestMethod.POST)
    public ResponseEntity authUserInfo(String code,String state,@PathVariable String which,@PathVariable String client) {

        ResponseJsonRender resultData = new ResponseJsonRender("成功");
        wxuserInfo(code,state,which,client);

        return returnDto(SecurityUtils.getSubject().getSession().getAttribute("publickplatform_openid_" + which),resultData);
    }

    private void wxuserInfo(String code,String state, String which, String client){
        // 根据code获取accessToken
        AuthorizeAccessToken authorizeAccessToken = PublicUtils.getAuthorizeAccessToken(code,which);

        // 获取到后，把openid放到session里
        SecurityUtils.getSubject().getSession().setAttribute("publickplatform_openid_" + which,authorizeAccessToken.getOpenid());

        // 根据openid查询是否存在数据
        WeixinUserPo weixinUserPoCondition = new WeixinUserPo();
        weixinUserPoCondition.setOpenid(authorizeAccessToken.getOpenid());
        weixinUserPoCondition.setType(DictEnum.WxAccountType.weixin_publicplatform.name());
        weixinUserPoCondition.setWhich(which);
        weixinUserPoCondition.setDelFlag(BasePo.YesNo.N.name());

        WeixinUserPo weixinUserPoDb = apiWeixinUserPoService.selectOneSimple(weixinUserPoCondition);
        BaseLoginClientPo loginClientPo = apiBaseLoginClientPoService.selectByClientCode(client);
        String clientId = null;
        if (loginClientPo != null) {
            clientId = loginClientPo.getId();
        }
        // 如果库里没有，插入
        if (weixinUserPoDb == null) {

            // 根据accessToken获取用户信息
            WeixinUserPo userPo = PublicUtils.getAuthorizeWeixinUser(authorizeAccessToken);
            userPo = apiWeixinUserPoService.preInsert(userPo, BasePo.DEFAULT_USER_ID);
            WeixinUserDto weixinUserDto = apiWeixinUserPoService.insert(userPo);



            if(StringUtils.isEmpty(clientId)){
                ShiroUser su = ShiroUtils.getCurrentUser();
                if (su != null) {
                    clientId = su.getLoginClientId();
                }
            }
            //调用监听
            apiWeixinUserListener.onAddWexinUser(weixinUserDto,clientId);
        }else {
            // 每天每个用户只更新一次信息
            if(Math.abs(CalendarUtils.getIntervalDays(new Date(), weixinUserPoDb.getUpdateAt())) > 0) {
                //调用监听
                // 根据accessToken获取用户信息
                WeixinUserPo userPo = PublicUtils.getAuthorizeWeixinUser(authorizeAccessToken);
                WeixinUserPo weixinUserPoToBeUpdate = new WeixinUserPo();
                weixinUserPoToBeUpdate.setId(weixinUserPoDb.getId());
                weixinUserPoToBeUpdate.setNickname(userPo.getNickname());
                weixinUserPoToBeUpdate.setGender(userPo.getGender());
                weixinUserPoToBeUpdate.setCity(userPo.getCity());
                weixinUserPoToBeUpdate.setProvince(userPo.getProvince());
                weixinUserPoToBeUpdate.setCountry(userPo.getCountry());
                weixinUserPoToBeUpdate.setHeadImageUrl(userPo.getHeadImageUrl());
                weixinUserPoToBeUpdate.setUpdateAt(new Date());

                apiWeixinUserPoService.updateByPrimaryKeySelective(weixinUserPoToBeUpdate);

            }
            apiWeixinUserListener.onUpdateWexinUser(apiWeixinUserPoService.selectByPrimaryKey(weixinUserPoDb.getId()),clientId);
        }
    }
}
