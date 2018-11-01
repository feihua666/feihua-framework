package com.feihua.framework.image.migration.sourcefrom;

import com.feihua.framework.image.migration.Context;
import com.feihua.framework.mybatis.orm.MultipleDataSource;
import com.feihua.framework.mybatis.orm.mapper.NativeSqlMapper;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.io.StreamUtils;
import com.feihua.utils.string.RegularExpression;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwei
 * Created at 2018/10/9 13:06
 */
public abstract class ApiSourceImpl implements ApiSource {


    @Autowired
    private Context context;

    @Autowired
    private NativeSqlMapper nativeSqlMapper;

    @Override
    public List<Map<String, Object>> fromSourceDb() {
        MultipleDataSource.setDataSourceKey(MultipleDataSource.DataSourceKey.dataSourceDefault.name());
        return nativeSqlMapper.selectByNativeSqlForList(context.getSourceSql());
    }
    public List<String> fetchUrlFromText(String text) {
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(RegularExpression.url.getReg());

        // 现在创建 matcher 对象
        Matcher m = r.matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()){
            String str = m.group();
            if (str != null) {
                list.add(str);
            }
        }
        return list;
    }

    public boolean isTextColumn(String column) {
        return false;
    }

    public boolean checkUrl(String url) {
        return true;
    }

    public byte[] downloadImage(String urlPath, String prefix) throws IOException {
        String _prefix = prefix;
        if(_prefix == null) {
            _prefix = "";
        }
        String oriPath = _prefix + urlPath;
        HttpClient client = HttpClientUtils.getClient();
        HttpGet get = new HttpGet(oriPath);

        HttpResponse httpResponse =  client.execute(get);
        InputStream inputStream = httpResponse.getEntity().getContent();
        byte[] bytes = StreamUtils.inputStreamToByteArray(inputStream);
        get.releaseConnection();
        return bytes;
    }

}
