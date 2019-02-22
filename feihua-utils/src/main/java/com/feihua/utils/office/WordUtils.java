package com.feihua.utils.office;

import org.apache.poi.xwpf.usermodel.*;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.w3c.dom.Node;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwei
 * Created at 2019/1/28 16:22
 * 使用poi解析word 只支持docx文件类型
 */
public class WordUtils {
    /** docx中定义的部分常量引用  **/
    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "bookmarkStart";
    public static final String BOOKMARK_END_TAG = "bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";
    /**
     * 创建一个word对象
     */
    public static XWPFDocument createDocument() {
        XWPFDocument document = new XWPFDocument();
        return document;
    }
    /**
     * 打开创建word文档
     * @param path 文档所在路径
     */
    public static XWPFDocument createDocument(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        return new XWPFDocument(is);
    }
    /**
     * 打开创建word文档
     * @param is 文档输入流
     */
    public static XWPFDocument createDocument(InputStream is) throws IOException {
        return new XWPFDocument(is);
    }

    /**
     * 保存word文档
     * @param document 文档对象
     * @param savePath    保存路径
     */
    public static void outPutWord(XWPFDocument document, String savePath) throws IOException {
        OutputStream os = new FileOutputStream(savePath);
        document.write(os);
        os.close();
    }

    /**
     * 获取所有段落中的书签
     * @param document
     * @return
     */
    public static List<CTBookmark> getBookmarkStartInParas(XWPFDocument document){
        return getBookmarkStart(getParas(document));
    }

    /**
     * 获取所有表格中的段落书签
     * @param document
     * @return
     */
    public static List<CTBookmark> getBookmarkStartInTableParas(XWPFDocument document){

        return getBookmarkStart(getParasInTables(document));
    }

    /**
     * 获取书签开始
     * @param paragraphs
     * @return
     */
    public static List<CTBookmark> getBookmarkStart(List<XWPFParagraph> paragraphs){
        List<CTBookmark> r = new ArrayList<>();
        for (XWPFParagraph paragraph : paragraphs) {
            List<CTBookmark> bookmarkList = paragraph.getCTP().getBookmarkStartList();
            r.addAll(bookmarkList);
        }
        return r;
    }

    /**
     * 获取书签开始
     * @param paragraph
     * @return
     */
    public static List<CTBookmark> getBookmarkStart(XWPFParagraph paragraph){
        List<CTBookmark> bookmarkList = paragraph.getCTP().getBookmarkStartList();

        return bookmarkList;
    }
    /**
     * 获取段落
     * @param document
     * @return
     */
    public static List<XWPFParagraph> getParas(XWPFDocument document){
        return document.getParagraphs();
    }

    /**
     * 获取所有表格中的段落
     * @param document
     * @return
     */
    public static List<XWPFParagraph> getParasInTables(XWPFDocument document){
        Iterator<XWPFTable> iterator = document.getTablesIterator();
        List<XWPFParagraph> r = new ArrayList<>();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    r.addAll(paras);
                }
            }
        }
        return r;
    }
    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    public static void replaceTextInParas(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceTextInPara(para, params);
        }
    }
    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    public static void replaceTextInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();

            List<Map<String,Object>> strList = new ArrayList<>();
            Map<String,Object> map = null;
            for (int i = 0; i < runs.size(); i++) {
                String str = "";
                XWPFRun run = runs.get(i);
                str = run.text();
                if(matcher(str).find()){

                    map = new HashMap<>();
                    map.put("start",i);
                    map.put("end",i);
                    map.put("text",run.text());
                    strList.add(map);
                }else{
                    if(str.contains("$") || str.contains("${"))
                        for(int j = i+1;j<runs.size();j++){
                            str += runs.get(j).text();
                            if(matcher(str).find()){
                                map = new HashMap<>();
                                map.put("start",i);
                                map.put("end",j);
                                map.put("text",str);
                                strList.add(map);
                                break;
                            }
                        }
                }

            }
            for(int m = strList.size()-1;m >= 0;m--){
                map = strList.get(m);
                int start = (int) map.get("start");
                int end = (int) map.get("end");
                String str = (String) map.get("text");
                for (int i = start; i <= end; i++) {
                    if(i == start){
                        int exist = 0;
                        for (String key : params.keySet()) {
                            if (str.contains(key)) {
                                runs.get(i).setText(str.replace(key,(String) params.get(key)), 0);
                                exist = 1;
                                break;
                            }
                        }
                        if(exist == 0){
                            break;
                        }
                    }else{
                        para.removeRun(i);
                        i--;
                        end--;
                    }

                }
            }

        }
    }

    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    public static void replaceTextInTable(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        replaceTextInPara(para, params);
                    }
                }
            }
        }
    }
    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }
    /**
     * 替换变量文本
     * @param document
     * @param param
     */
    public static void replaceTextVariables(XWPFDocument document, Map<String,Object> param){

        replaceTextInParas(document,param);
        replaceTextInTable(document,param);
    }
    public static void insertTextBeforeBookmarkVariables(XWPFDocument document, Map<String,Object> param){

        List<XWPFParagraph> paragraphs = new ArrayList<>();
        paragraphs.addAll(getParas(document));
        paragraphs.addAll(getParasInTables(document));
        for (XWPFParagraph xwpfParagraph : paragraphs) {
            List<CTBookmark> bookmarks = getBookmarkStart(xwpfParagraph);
            for (CTBookmark bookmark : bookmarks) {
                String name = bookmark.getName();
                for (String key : param.keySet()) {
                    if(key.equals(name)){
                        XWPFRun run = xwpfParagraph.createRun();
                        run.setText((String) param.get(key));
                        insertBeforeBookmark(run,bookmark,xwpfParagraph);
                        break;
                    }
                }
            }

        }
    }

    /**
     * 在书签前插入
     * @param run 插入的内容
     * @param bookmark 书签
     * @param para 书签所在段落
     */
    public static void insertBeforeBookmark(XWPFRun run,CTBookmark bookmark,XWPFParagraph para) {
        Node insertBeforeNode = null;
        Node childNode = null;
        Node styleNode = null;

        // Get the dom node from the bookmarkStart tag and look for another
        // node immediately preceding it.
        insertBeforeNode = bookmark.getDomNode();
        childNode = insertBeforeNode.getPreviousSibling();
        // If a node is found, try to get the styling from it.
        if (childNode != null) {
            styleNode = getStyleNode(childNode);

            // If that previous node was styled, then apply this style to the
            // text which will be inserted.
            if (styleNode != null) {
                run.getCTR().getDomNode().insertBefore(
                        styleNode.cloneNode(true), run.getCTR().getDomNode().getFirstChild());
            }
        }

        // Insert the text into the paragraph immediately in front of the
        // bookmarkStart tag.
        para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), insertBeforeNode);
    }

    /**
     * 获取节点的样式node
     * @param parentNode
     * @return
     */
    private static Node getStyleNode(Node parentNode) {
        Node childNode = null;
        Node styleNode = null;
        if (parentNode != null) {

            // If the node represents a run and it has child nodes then
            // it can be processed further. Note, whilst testing the code, it
            // was observed that although it is possible to get a list of a nodes
            // children, even when a node did have children, trying to obtain this
            // list would often return a null value. This is the reason why the
            // technique of stepping from one node to the next is used here.
            if (parentNode.getNodeName().equalsIgnoreCase(RUN_NODE_NAME)
                    && parentNode.hasChildNodes()) {

                // Get the first node and catch it's reference for return if
                // the first child node is a style node (w:rPr).
                childNode = parentNode.getFirstChild();
                if (childNode.getNodeName().equals(STYLE_NODE_NAME)) {
                    styleNode = childNode;
                } else {
                    // If the first node was not a style node and there are other
                    // child nodes remaining to be checked, then step through
                    // the remaining child nodes until either a style node is
                    // found or until all child nodes have been processed.
                    while ((childNode = childNode.getNextSibling()) != null) {
                        if (childNode.getNodeName().equals(STYLE_NODE_NAME)) {
                            styleNode = childNode;
                            // Note setting to null here if a style node is
                            // found in order order to terminate any further
                            // checking
                            childNode = null;
                        }
                    }
                }
            }
        }
        return (styleNode);
    }
}
