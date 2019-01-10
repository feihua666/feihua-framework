package com.feihua.framework.cms.front.web.mvc;

import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.api.ApiCmsContentPoService;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.rest.mvc.SuperController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * cms前端ajax调用基础类
 * Created by yangwei
 */
@Controller
public class FrontBaseController extends SuperController {
    private static Logger logger = LoggerFactory.getLogger(FrontBaseController.class);

    public static final String self_session_id_name = "sid";

    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;

}
