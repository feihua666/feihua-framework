package com.feihua.wechat.rest.miniprogram.mvc;


import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.encode.EncodeUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.json.JSONUtils;
import com.feihua.wechat.ParamsDto;
import com.feihua.wechat.common.api.ApiWeixinUserListener;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.dto.WeixinUserDto;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.feihua.wechat.miniprogram.MiniProgramUtils;
import com.feihua.wechat.miniprogram.api.ApiMiniProgramService;
import com.feihua.wechat.miniprogram.dto.LoginCredentialsDto;
import com.feihua.wechat.rest.miniprogram.dto.WeixinUserForm;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/4/28 14:51
 */
@RestController
@RequestMapping("/miniprogram")
public class MiniProgramController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(MiniProgramController.class);

    public static String loginCredentials_key = "miniprogram_LoginCredentials";

    @Autowired
    private ApiMiniProgramService apiMiniProgramService;
    @Autowired
    private ApiWeixinUserPoService apiWeixinUserPoService;
    @Autowired
    ApiWeixinUserListener apiWeixinUserListener;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    /**
     * 小程序对接接口
     * @param request
     * @param response
     * @param which 支持多个小程序，标识哪一个，如配置：miniprogram.hz.appID，则which应该是hz
     */
    @RequestMapping(value = "/service/core/{which}",method = RequestMethod.POST,produces = "application/xml;charset=UTF-8")
    public void message(HttpServletRequest request, HttpServletResponse response, @PathVariable String which){
        InputStream inputStream = null;
        PrintWriter out = null;
        try{
            // 从请求中读取整个post数据
            inputStream = request.getInputStream();
            String postData = IOUtils.toString(inputStream, "UTF-8");
            String respMessage = "";
            if(DictEnum.XmlOrJsonType.json.equals(MiniProgramUtils.getAppMsgType(which))){
                // 调用核心业务类接收消息、处理消息
                respMessage = apiMiniProgramService.processjsonMsg(postData,which);
            } else if(DictEnum.XmlOrJsonType.xml.equals(MiniProgramUtils.getAppMsgType(which))){
                // 调用核心业务类接收消息、处理消息
                respMessage = apiMiniProgramService.processXmlMsg(postData,which);
            }

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
        String result = MiniProgramUtils.validate(signature, echostr, timestamp, nonce,
                MiniProgramUtils.getAppToken(which));

        return result;
    }

    /**
     * 获取登录凭证
     * @param code
     * @param which 支持多个小程序，区分类型
     * @return
     */
    @RequestMapping(value = "/loginCredentials/{which}/{code}",method = RequestMethod.GET)
    public ResponseEntity getLoginCredentials(@PathVariable String code,@PathVariable String which){

        ResponseJsonRender resultData=new ResponseJsonRender();
        ParamsDto paramsDto = new ParamsDto();
        paramsDto.setAppId(MiniProgramUtils.getAppid(which));
        paramsDto.setSecret(MiniProgramUtils.getAppsecret(which));

        LoginCredentialsDto loginCredentialsDto = apiMiniProgramService.fetchLoginCredentials(code,paramsDto);

        //将登录凭证保存起来解密的时候用
        SecurityUtils.getSubject().getSession().setAttribute(loginCredentials_key,loginCredentialsDto);

        return super.returnDto(loginCredentialsDto,resultData);
    }

    /**
     * 保存小程序微信用户，请确保调用该接口前已经调用了 loginCredentials 接口
     * @param which
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/weixinuser/{which}",method = RequestMethod.POST)
    public ResponseEntity saveWeixinUser(@PathVariable String which,String fromClientCode, WeixinUserForm weixinUserForm){

        ResponseJsonRender resultData=new ResponseJsonRender();
        // 获取登录凭证，可以借用里面的openid，如果为空，必须解密加密的数据才可以获得openid
        LoginCredentialsDto loginCredentialsDto = (LoginCredentialsDto) SecurityUtils.getSubject().getSession().getAttribute(loginCredentials_key);
        if (loginCredentialsDto != null) {
            addWeixinUser(which,loginCredentialsDto.getOpenid(),loginCredentialsDto.getUnionid(),weixinUserForm,fromClientCode);

            return new ResponseEntity(resultData, HttpStatus.OK);
        }
        else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 通过解密的方式保存小程序用户信息，请确保调用该接口前已经调用了 loginCredentials 接口
     * 如果需要获取用户unionid需要调用些接口
     * @param which
     * @param rawData
     * @param signature
     * @param encryptedData
     * @param iv
     * @param language
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/weixinuser/{which}/decode",method = RequestMethod.POST)
    public ResponseEntity saveWeixinUserDecode(@PathVariable String which,String fromClientCode, String rawData,String signature,String encryptedData,String iv,String language){

        ResponseJsonRender resultData=new ResponseJsonRender();

        String result = decode(rawData,signature,encryptedData,iv);
        if (StringUtils.isNotEmpty(result)) {
            WeixinUserForm weixinUserForm = new WeixinUserForm();

            Map<String,Object> r = null;
            try {
                r = JSONUtils.json2map(result);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
            weixinUserForm.setNickName(r.get("nickName"  ).toString());
            weixinUserForm.setGender(r.get("gender").toString());
            weixinUserForm.setCity(r.get("city").toString());
            weixinUserForm.setProvince(r.get("province").toString());
            weixinUserForm.setCountry(r.get("country").toString());
            weixinUserForm.setAvatarUrl(r.get("avatarUrl").toString());
            weixinUserForm.setLanguage(language);

            String unionid = r.get("unionId") == null?null:r.get("unionId").toString();

            addWeixinUser(which,r.get("openId").toString(),unionid,weixinUserForm, fromClientCode);


            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }

    }
    private void addWeixinUser(String which,String openid,String unionid, WeixinUserForm weixinUserForm,String fromClientCode){
        WeixinUserPo weiXinUserPo = new WeixinUserPo();
        weiXinUserPo.setOpenid(openid);
        weiXinUserPo.setUnionid(unionid);
        weiXinUserPo.setNickname(weixinUserForm.getNickName());
        weiXinUserPo.setGender(weixinUserForm.getGender());
        weiXinUserPo.setCity(weixinUserForm.getCity());
        weiXinUserPo.setProvince(weixinUserForm.getProvince());
        weiXinUserPo.setCountry(weixinUserForm.getCountry());
        weiXinUserPo.setHeadImageUrl(weixinUserForm.getAvatarUrl());
        weiXinUserPo.setHowFrom(DictEnum.WeixinUserHowFrom.webAuthorize.name());
        weiXinUserPo.setLanguage(weixinUserForm.getLanguage());
        weiXinUserPo.setType(DictEnum.WxAccountType.weixin_miniprogram.name());
        weiXinUserPo.setWhich(which);

        // 根据openid查询是否存在数据
        WeixinUserPo weixinUserPoCondition = new WeixinUserPo();
        weixinUserPoCondition.setOpenid(openid);
        weixinUserPoCondition.setType(DictEnum.WxAccountType.weixin_miniprogram.name());
        weixinUserPoCondition.setWhich(which);
        weixinUserPoCondition.setDelFlag(BasePo.YesNo.N.name());

        WeixinUserPo weixinUserPoDb = apiWeixinUserPoService.selectOneSimple(weixinUserPoCondition);
        // 如果库里没有，插入
        if (weixinUserPoDb == null) {
            weiXinUserPo = apiWeixinUserPoService.preInsert(weiXinUserPo, BasePo.DEFAULT_USER_ID);
            WeixinUserDto weixinUserDto = apiWeixinUserPoService.insert(weiXinUserPo);

            String clientId = null;
            if (StringUtils.isNotEmpty(fromClientCode)) {
                BaseLoginClientPo loginClientPo = apiBaseLoginClientPoService.selectByClientCode(fromClientCode);
                if (loginClientPo != null) {
                    clientId = loginClientPo.getId();
                }

            }
            //调用监听
            apiWeixinUserListener.onAddWexinUser(weixinUserDto,clientId);
        }
    }
    private String decode(String rawData,String signature,String encryptedData,String iv){
        // 获取登录凭证，可以借用里面的openid，如果为空，必须解密加密的数据才可以获得openid
        LoginCredentialsDto loginCredentialsDto = (LoginCredentialsDto) SecurityUtils.getSubject().getSession().getAttribute(loginCredentials_key);
        String result = null;
        if (loginCredentialsDto != null) {
            // 被加密的数据
            try {
                byte[] dataByte = EncodeUtils.decodeBase64(encryptedData);

                // 加密秘钥
                byte[] keyByte = EncodeUtils.decodeBase64(loginCredentialsDto.getSessionKey());
                // 偏移量
                byte[] ivByte = EncodeUtils.decodeBase64(iv);
                // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
                int base = 16;
                if (keyByte.length % base != 0) {
                    int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                    byte[] temp = new byte[groups * base];
                    Arrays.fill(temp, (byte) 0);
                    System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                    keyByte = temp;
                }
                // 初始化
                Security.addProvider(new BouncyCastleProvider());
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
                byte[] resultByte = cipher.doFinal(dataByte);
                if (null != resultByte && resultByte.length > 0) {
                    result = new String(resultByte, "UTF-8");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }

        return result;
    }

}
