package com.feihua.utils.properties;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.feihua.utils.collection.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 系统配置，单例,包装PropertiesLoader
 * Created by yw on 2015/9/16.
 */
public class PropertiesUtils {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static String[] propertyPath;

    private static Properties properties = null;

    static {
        init();
    }
    private static void init(){
        if(propertyPath == null) return;
        properties = new Properties();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        for (String location : propertyPath) {
            InputStream is = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                properties.load(new InputStreamReader(is,"UTF-8"));
            } catch (IOException e) {
                logger.error("Could not load properties from path:{}" , location, e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    logger.error("when close inputStream error", e);
                }
            }
        }
    }
    /**
     * 取出String类型的Property，但以System的Property优先.
     */
    public static String getProperty(String key) {
        if (useType_apollo.equals(useType)) {
            return getPropertyApollo(key,null);
        }else if (useType_default.equals(useType)) {
            return _getProperty(key);
        }else if (useType_mix.equals(useType)) {
            String r = null;
            if(useType_default.equals(useTypeMixFirst)){
                r = _getProperty(key);
                if (r == null) {
                    r = getPropertyApollo(key,null);
                }

            }else if(useType_apollo.equals(useTypeMixFirst)){
                r = getPropertyApollo(key,null);
                if (r == null) {
                    r = _getProperty(key);
                }
            }
            return r;
        }else {
            return null;
        }

    }
    private static String _getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return null;
    }
    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public static Integer getInteger(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        String value = getProperty(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public static Double getDouble(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public static Double getDouble(String key, Integer defaultValue) {
        String value = getProperty(key);
        return value != null ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     */
    public static Boolean getBoolean(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     */
    public static Boolean getBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.valueOf(value) : defaultValue;
    }

    public static void setPropertyPath(String[] propertyPath) {
        PropertiesUtils.propertyPath = propertyPath;
        init();
    }
    public static void addPropertyPath(String[] propertyPath) {
        if (PropertiesUtils.propertyPath == null) {
            PropertiesUtils.propertyPath = propertyPath;
        }else {
            PropertiesUtils.propertyPath = CollectionUtils.catArray(PropertiesUtils.propertyPath,propertyPath);
        }

        init();
    }

    //*********************** 以下为apollo方式
    // 说明，本类支持的阿波罗配置是扫描所有命名空间，所以一定要保证一个应用的所有命名空间下的key全局唯一
    /**
     * 设置使用方式 default mix apollo
     * default = 只使用本地properties方式
     * mix = 混合使用，同时支持default和apollo
     * apollo = 只获取apollo
     */
    private static String useType = "mix";

    private static String useType_mix = "mix";
    private static String useType_default = "default";
    private static String useType_apollo = "apollo";

    // 如果使用混合方式，先用默认的方式取还是先用apollo方式取，默认是default, 备选apollo
    private static String useTypeMixFirst = "default";
    // apollo的命名空间，以逗号分隔，如果没有指定，默认会取application命名空间的值
    private static String namespaces;

    public static void setUseType(String useType){
        PropertiesUtils.useType = useType;
    }
    public static void setNamespaces(String namespaces){
        PropertiesUtils.namespaces = namespaces;
    }
    public static void setUseTypeMixFirst(String useTypeMixFirst){
        PropertiesUtils.useTypeMixFirst = useTypeMixFirst;
    }
    /**
     * 获取所有namespace的配置信息
     * @return
     */
    private static List<Config> getApolloConfig(){
        if(StringUtils.isEmpty(PropertiesUtils.namespaces)){
            return null;
        }
        String namespaces[] = PropertiesUtils.namespaces.split(",");
        List<Config> r = new ArrayList<>(namespaces.length);
        Config config = null;
        for (String namespace : namespaces) {
            config = ConfigService.getConfig(namespace);
            if (config != null) {
                r.add(config);
            }
        }
        return r;
    }

    public static String getPropertyApollo(String key, String defaultValue) {
        List<Config> configs = getApolloConfig();
        if (configs == null) {
            return defaultValue;
        }
        String r = defaultValue;
        String _defaultValue = UUID.randomUUID().toString();
        for (Config config : configs) {
            String value = config.getProperty(key,_defaultValue);
            if(_defaultValue.equals(value)){
                continue;
            }else {
                r = value;
                break;
            }
        }
        return r;
    }
}
