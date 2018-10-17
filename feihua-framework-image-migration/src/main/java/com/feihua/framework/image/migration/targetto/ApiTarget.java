package com.feihua.framework.image.migration.targetto;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/9/25 14:40
 */
public interface ApiTarget {

    public Object saveToTargetDb(Map<String,Object> map);

    public String uploadImage(byte[] imageByte);

    public String getPrefix();


}
