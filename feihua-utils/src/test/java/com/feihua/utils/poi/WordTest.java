package com.feihua.utils.poi;

import com.feihua.utils.office.WordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2019/1/28 16:52
 */
public class WordTest {
    public static void main(String[] args) throws IOException {

        XWPFDocument wordDoc1 = WordUtils.createDocument("C:\\Users\\Lenovo\\Desktop\\testTemplate1.docx");
        XWPFDocument wordDoc2 = WordUtils.createDocument("C:\\Users\\Lenovo\\Desktop\\testTemplate2.docx");
        Map<String,Object> params1 = new HashMap<>();
        params1.put("${name1}","名字1");
        params1.put("${name2}","名字2");
        params1.put("${name3}","名字3");
        params1.put("${name4}","名字4");
        params1.put("${name5}","名字5");
        params1.put("${name6}","名字6");
        params1.put("${name7}","名字7");
        params1.put("${name8}","名字8");
        params1.put("${name9}","名字9");
        WordUtils.replaceTextVariables(wordDoc1,params1);
        WordUtils.outPutWord(wordDoc1,"C:\\Users\\Lenovo\\Desktop\\testTemplate11.docx");

        Map<String,Object> params2 = new HashMap<>();
        params2.put("name1","名字1");
        params2.put("name2","名字2");
        params2.put("name3","名字3");
        WordUtils.insertTextBeforeBookmarkVariables(wordDoc2,params2);
        WordUtils.outPutWord(wordDoc2,"C:\\Users\\Lenovo\\Desktop\\testTemplate22.docx");
    }
}
