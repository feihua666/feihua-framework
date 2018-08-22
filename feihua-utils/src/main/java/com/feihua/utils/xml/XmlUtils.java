package com.feihua.utils.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangwei
 * Created at 2018/7/20 11:57
 */
public class XmlUtils {

    private static Logger logger = LoggerFactory.getLogger(XmlUtils.class);


    /**
     * xml字符串转doc
     * @param xml
     * @return
     */
    public static Document stringToDocument(String xml){
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            logger.error(e.getMessage(),e);
        }
        return document;
    }

    /**
     * 获取节点内容
     * @param name
     * @param document
     * @return
     */
    public static String getElementText(String name,Document document){
        //定位根节点
        Element root = document.getRootElement();
        //根据名称定位节点
        Element element = root.element(name);
        //返回节点内容
        return element.getText();
    }
}
