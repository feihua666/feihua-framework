package com.feihua.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson 工具类
 * Created by lyy on 2015/9/17.
 */
public class JSONUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Map<JsonInclude.Include,ObjectMapper> objectMapperMap =  new HashMap();

    static {
        for (JsonInclude.Include include : JsonInclude.Include.values()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(include);
            objectMapperMap.put(include,objectMapper);
        }
    }
    private JSONUtils() {
     }

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
    public static String obj2json(Object obj,JsonInclude.Include include) throws Exception {
        return objectMapperMap.get(include).writeValueAsString(obj);
    }
    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz)
            throws Exception {
        return objectMapper.readValue(jsonStr, clazz);
    }

    /**
     * json string convert to map
     */
    public static Map<String, Object> json2map(String jsonStr)
            throws Exception {
        return objectMapper.readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
            throws Exception {
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            if (entry.getValue() == null){
                result.put(entry.getKey(), null);
            }else
            if (entry.getValue() instanceof Map){
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }else{
                result.put(entry.getKey(), (T) entry.getValue());
            }
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}
