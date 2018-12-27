package com.feihua.framework.spider;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/12/17 18:44
 */
public class JsoupUtils {

    /**
     *
     * @param html
     * @return
     */
    public static Document htmlStringToDoc(String html){
        if (StringUtils.isEmpty(html)) {
            return null;
        }
        return Jsoup.parse(html);
    }
    public static Element selectOne(String selector, Element element){
        if (StringUtils.isEmpty(selector) || element == null) {
            return null;
        }
        Element r =  element.selectFirst(selector);
        return r;
    }
    public static List<Element> selectList(String selector, Element element){
        if (StringUtils.isEmpty(selector) || element == null) {
            return null;
        }
        List<Element> r =  element.select(selector);
        return r;
    }
    public static String selectInnerHtml(String selector, Element element){
        Element r =  selectOne(selector,element);

        if (r != null) {
            return r.html();
        }
        return null;
    }
    public static String selectOuterHtml(String selector, Element element){
        Element r =  selectOne(selector,element);

        if (r != null) {
            return r.outerHtml();
        }
        return null;
    }
    public static String getText( String cssSelector,Element element){
        return getText(selectOne(cssSelector,element));
    }
    public static String getOwnText( String cssSelector,Element element){
        return getOwnText(selectOne(cssSelector,element));
    }
    public static String getAttr( String cssSelector,String attr,Element element){
        Element _element = selectOne(cssSelector,element);
        if (_element == null) {
            return null;
        }
        return _element.attr(attr);
    }
    public static String getAttrHref( String cssSelector,Element element){
        return getAttr(cssSelector,"href",element);
    }
    public static String getAttrSrc( String cssSelector,Element element){
        return getAttr(cssSelector,"src",element);
    }
    public static String getTextForList( String cssSelector,String seperator,Element element){

        if (StringUtils.isEmpty(cssSelector) || element == null) return null;

        if(StringUtils.isEmpty(seperator)){
            seperator = ",";
        }
        List<Element> elements = selectList(cssSelector,element);
        if (elements != null) {
            StringBuffer sb = new StringBuffer();
            for (Element element1 : elements) {
                sb.append(getText(element1));
                sb.append(seperator);
            }
            return sb.substring(0,sb.length() - seperator.length());
        }
        return null;

    }
    public static String getText( Element element){
        if (element == null) {
            return null;
        }
        return element.text();
    }
    public static String getOwnText( Element element){
        if (element == null) {
            return null;
        }
        return element.ownText();
    }
    public static String getDocTitle( Document doc){
        if (doc == null) {
            return null;
        }
        return doc.title();
    }
}
