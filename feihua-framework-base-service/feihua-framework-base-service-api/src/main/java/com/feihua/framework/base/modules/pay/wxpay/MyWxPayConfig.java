package com.feihua.framework.base.modules.pay.wxpay;

import com.feihua.framework.base.modules.config.api.ApiBaseConfigService;
import com.feihua.framework.constants.ConfigConstant;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.spring.SpringContextHolder;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyWxPayConfig extends WXPayConfig {
    private static PayConfig payConfig;

    static {
        ApiBaseConfigService apiBaseConfigService = SpringContextHolder.getBean(ApiBaseConfigService.class);
        MyWxPayConfig.payConfig = apiBaseConfigService.getConfigObject(ConfigConstant.WXPAY_STORAGE_CONFIG_KEY, PayConfig.class);
    }

    /**
     * 加载证书  这里证书需要到微信商户平台进行下载
     */
    private byte[] certData;

    public MyWxPayConfig() throws Exception {
        File file = FileUtils.createFile(payConfig.getCertFilePath());
        InputStream certStream = new FileInputStream(file);
        this.certData = IOUtils.toByteArray(certStream);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return payConfig.getAppID();
    }

    @Override
    public String getMchID() {
        return payConfig.getMchID();
    }

    @Override
    public String getKey() {
        return payConfig.getKey();
    }

    public int getHttpConnectTimeoutMs() {
        return payConfig.getHttpConnectTimeoutMs();
    }

    public int getHttpReadTimeoutMs() {
        return payConfig.getHttpReadTimeoutMs();
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }


    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}
