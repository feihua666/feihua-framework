package com.feihua.framework.image.migration.sourcefrom;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/9/25 14:40
 */
public interface ApiSource {

    /**
     * 获取源图片数据
     * @return
     */
    public List<Map<String,Object>> fromSourceDb();

    /**
     * 从文本解析出图片url
     * @param text
     * @return
     */
    public List<String> fetchUrlFromText(String text);

    /**
     * 是否为文本字段
     * @param column
     * @return
     */
    public boolean isTextColumn(String column);

    /**
     * 检查图片url是否正确
     * @param url
     * @return
     */
    public boolean checkUrl(String url);

    /**
     * 下载图片
     * @param urlPath
     * @param prefix urlPath的前缀
     * @return
     */
    public byte[] downloadImage(String urlPath, String prefix) throws IOException;

    public String getPrefix(boolean isText);
}
