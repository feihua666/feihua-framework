package com.feihua.framework.base.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2017/11/7 9:32
 */
public class CachePlugin extends org.mybatis.generator.plugins.CachePlugin {

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable){
        super.sqlMapDocumentGenerated(document,introspectedTable);



        List<Element> elements = document.getRootElement().getElements();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if(element instanceof XmlElement && ((XmlElement) element).getName().equals("cache")){
                ((XmlElement) element).addAttribute(new Attribute("type","com.feihua.framework.mybatis.orm.cache.RedisCache"));
            }
        }
        return true;
    }
}
