package com.feihua.framework.base.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;

/**
 * 查询全部方法
 * Created by yangwei
 * Created at 2017/8/25 9:11
 */
public class DeleteSelectiveElementGenerator extends
        AbstractXmlElementGenerator {

    public static String statementId = "deleteSelective";

    public DeleteSelectiveElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete");

        answer
                .addAttribute(new Attribute(
                        "id", statementId));

        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType",
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("delete from ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));


        XmlElement whereElement = new XmlElement("where");
        answer.addElement(whereElement);

        XmlElement trimElement = new XmlElement("trim");
        trimElement.addAttribute(new Attribute("prefix", ""));
        trimElement.addAttribute(new Attribute("suffix", ""));
        trimElement.addAttribute(new Attribute("prefixOverrides", "and"));
        whereElement.addElement(trimElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getAllColumns()) {
            XmlElement isNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            trimElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(" and ");
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));


            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        parentElement.addElement(answer);
    }
}
