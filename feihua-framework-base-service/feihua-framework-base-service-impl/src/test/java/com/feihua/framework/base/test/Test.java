package com.feihua.framework.base.test;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/14 18:02
 */
public class Test {
    public static void main(String[] args) {
        // System.out.println(DictEnum.OfficeDataScope.all.name());
        //BaseMessageTemplateDto baseUserRoleRelDto = new BaseMessageTemplateDto();
         // getSetMethod(new BaseMessageDto(),"baseMessageDto","po");
        //runCollectionUtils2();
        //System.out.println(RandomStringUtils.random(10,true,true));
        //System.out.println(com.feihua.utils.collection.CollectionUtils.catArray(new String[]{"s","2"},new String[]{"s","2"}));
    }

    /**
     * po 转dto
     * @param obj
     * @param setPrefix
     * @param getPrefix
     */
    public static void getSetMethod(Object obj,String setPrefix,String getPrefix){
        Method[]methods = obj.getClass().getMethods();
        for (Method method : methods) {
            String name = method.getName();
            String parentIdXPrefix = "setParentId";
            if(name.startsWith(parentIdXPrefix) && name.length() > parentIdXPrefix.length()){
                continue;
            }
            if(name.startsWith("setDelFlag")
                    || name.startsWith("setCreateBy")
                    || name.startsWith("setCreateAt")
                    ||name.startsWith("setUpdateBy")){
                continue;
            }

            if(name.startsWith("set")){

                System.out.println(setPrefix + "." + name + "(" + getPrefix + ".get" + name.substring(3) + "());");
            }
        }
    }
    /**
     * CollectionUtils性能
     */
    public static void runCollectionUtils1(){
        int i = 1000000;
        long now = System.currentTimeMillis();
        for (int j = 0; j < i; j++) {
            List list = new ArrayList();
            if(CollectionUtils.isEmpty(list)){

            }
        }
        long end = System.currentTimeMillis();
        System.out.println("utiltime:" + (end - now));

         now = System.currentTimeMillis();
        for (int j = 0; j < i; j++) {
            List list = new ArrayList();
            if(list == null && list.isEmpty()){

            }
        }
         end = System.currentTimeMillis();
        System.out.println("normal:" + (end - now));
    }
    /**
     * CollectionUtils性能
     */
    public static void runCollectionUtils2(){
        int i = 1000000;

        long now1 = System.currentTimeMillis();
        for (int j = 0; j < i; j++) {
            String s = "sss";
            if(s == null || !"".equals(s)){

            }
        }
        long end1 = System.currentTimeMillis();
        System.out.println("normal:" + (end1 - now1));


        long now = System.currentTimeMillis();
        for (int j = 0; j < i; j++) {
            String s = "sss";
            if(StringUtils.isEmpty(s)){

            }
        }
        long end = System.currentTimeMillis();
        System.out.println("utiltime:" + (end - now));


    }
}
