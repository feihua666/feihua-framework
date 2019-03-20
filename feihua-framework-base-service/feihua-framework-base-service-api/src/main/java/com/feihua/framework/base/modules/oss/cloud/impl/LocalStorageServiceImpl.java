package com.feihua.framework.base.modules.oss.cloud.impl;

import com.feihua.framework.base.modules.oss.cloud.dto.LocalStorageConfig;
import com.feihua.framework.utils.FileHelper;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.io.StreamUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by yangwei
 * Created at 2019/3/14 11:45
 */
public class LocalStorageServiceImpl extends AbstractCloudStorageService {
    private static Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

    private LocalStorageConfig config;
    public LocalStorageServiceImpl(LocalStorageConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
    }

    @Override
    public String upload(byte[] data, String filepath) {
        return upload(new ByteArrayInputStream(data),filepath);
    }

    @Override
    public String uploadSuffix(byte[] data,String prefixPath, String suffix) {
        return upload(new ByteArrayInputStream(data),getPath(config.getLocalPrefix() + "/" + prefixPath,suffix));
    }

    @Override
    public String upload(InputStream inputStream, String filepath) {
        String resultPath = null;
        try {
            String originalFileExtention = FileHelper.getExtention(filepath);
            String filenameTemp = "";
            if(StringUtils.isNotEmpty(originalFileExtention)){
                filenameTemp += "temp." + originalFileExtention;
            }
            String relativePath = "";
            if (StringUtils.isNotEmpty(filepath)) {
                relativePath = filepath.substring(0,filepath.lastIndexOf("/"));
            }
            resultPath = FileHelper.saveToDisk(inputStream, filenameTemp,this.config.getLocationPath(), relativePath);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        if (StringUtils.isNotEmpty(resultPath)) {
            resultPath = RequestUtils.convertToSlash(resultPath);
        }
        return resultPath;
    }

    @Override
    public String uploadSuffix(InputStream inputStream,String prefixPath, String suffix) {
        return upload(inputStream,getPath(config.getLocalPrefix() + "/" + prefixPath,suffix));
    }

    @Override
    public byte[] download(String objectName) {

        try {
            return StreamUtils.inputStreamToByteArray(new FileInputStream(new File(this.config.getLocationPath() +"/"+ objectName)));
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public int delete(String objectName) {
        int r = 0;
        try {
            FileHelper.deleteDiskFile(this.config.getLocationPath() + "/" + config.getLocalPrefix() ,objectName);
            r = 1;
        }catch (Exception e){
            r = 0;
        }
        return 0;
    }
}
