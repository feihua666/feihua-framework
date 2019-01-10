package com.feihua.framework.spider.rest.mvc;

import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.spider.JsoupUtils;
import com.feihua.framework.spider.api.ApiSpiderService;
import com.feihua.framework.spider.rest.dto.FetchFormDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/12/17 19:59
 */
@RestController
@RequestMapping("/spider")
public class SpiderController extends SuperController {

    private static final String selector_text_suffix = ":text";
    @Autowired
    private ApiSpiderService apiSpiderService;
    /**
     * 单资源，获取id消息管理
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/fetch",method = RequestMethod.POST)
    public ResponseEntity fetch(FetchFormDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String html = null;
        if (dto.isDynamic()) {
            html = apiSpiderService.fetchDynamic(dto.getUrl(),dto.getDynamicWaitSecond());
        }else{
            html = apiSpiderService.fetchStatic(dto.getUrl());
        }
        if(StringUtils.isNotEmpty(html)){
            Map<String,String> selector = dto.getSelector();
            if (selector != null && !selector.isEmpty()) {
                Map<String,String> result = new HashMap<>(selector.size());
                Document doc = JsoupUtils.htmlStringToDoc(html);
                for (String key : selector.keySet()) {
                    String selectorStr = selector.get(key);
                    if(StringUtils.isNotEmpty(selectorStr)){

                        if(selectorStr.endsWith(selector_text_suffix)){
                            Element element = JsoupUtils.selectOne(selectorStr.substring(0,selectorStr.length() - selector_text_suffix.length()),doc);
                            result.put(key,JsoupUtils.getText(element));
                        }else{
                            result.put(key,JsoupUtils.selectOuterHtml(selectorStr,doc));
                        }
                    }
                }
                resultData.setData(result);
            }
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
