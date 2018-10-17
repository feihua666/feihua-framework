package com.feihua.framework.image.migration;

import com.feihua.framework.image.migration.sourcefrom.ApiSource;
import com.feihua.framework.image.migration.targetto.ApiTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/9 10:16
 */

public class ApiMainImpl implements ApiMain{
    private static Logger logger = LoggerFactory.getLogger(ApiMainImpl.class);
    @Autowired
    private ApiSource apiSource;
    @Autowired
    private ApiTarget apiTarget;
    @Autowired
    private Config config;

    @Autowired
    private Context context;

    public void go() {
        context.setConfig(config);
        logger.info("****************** 图片迁移开始 *************");
        List<Map<String, String>> configSourceTables = config.getSourceTables();
        for (int i = 0; i < configSourceTables.size(); i++) {
            Map<String,String> sourceTableItem = configSourceTables.get(i);
            context.setCount(i + 1);
            context.setSourceTableName(sourceTableItem.get(Config.sourceTableName));
            context.setSourceSql(sourceTableItem.get(Config.sourceSql));
            logger.info("{}/{} 张表 {}",context.getCount(),configSourceTables.size(),context.getSourceTableName());

            List<Map<String,Object>> list = apiSource.fromSourceDb();

            logger.info("表 {} 共有 {} 条数据",context.getSourceTableName(),list.size());


            for (int i1 = 0; i1 < list.size(); i1++) {
                logger.info("正在处理表 {},{}/{}",context.getSourceTableName(),i1 + 1,list.size());

                Map<String, Object> stringObjectMap = list.get(i1);
                Map<String,Object> resultMap = new HashMap<>();
                for (String key : stringObjectMap.keySet()) {
                    resultMap.put(key,stringObjectMap.get(key));
                    // text column
                    if(apiSource.isTextColumn(key)){
                        Object columnText = stringObjectMap.get(key);
                        if (columnText != null) {
                            List<String> urls = apiSource.fetchUrlFromText(columnText.toString());
                            if (urls != null) {
                                for (String url : urls) {
                                    if(apiSource.checkUrl(url)){
                                        String path = downloadAndUpload(url,apiSource.getPrefix(true));
                                        String targetUrl = apiTarget.getPrefix() + path;
                                        resultMap.put(key,stringObjectMap.get(key).toString().replace(url,targetUrl));
                                    }
                                }// end for
                            }// end if
                        }// end if
                    }else{
                        Object columnPath = stringObjectMap.get(key);
                        if (columnPath != null) {
                            String url = columnPath.toString();
                            if(apiSource.checkUrl(url)){
                                String path = downloadAndUpload(columnPath.toString(),apiSource.getPrefix(false));
                                resultMap.put(key,path);
                            }
                        }
                    }// end if
                    apiTarget.saveToTargetDb(resultMap);
                }// end inner for
            }// end outer for
        }

    }

    private String downloadAndUpload(String url,String prefix){
        byte[] bytes = new byte[0];
        try {
            bytes = apiSource.downloadImage(url,prefix);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        String path = apiTarget.uploadImage(bytes);
        return path;
    }
}
