package com.feihua.framework.base.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.GeneratedKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 插入，全部插入，包括主键指定
 * Created by yangwei
 * Created at 2017年11月8日 16:10:27
 */
public class InsertSelectiveWithPrimaryKeyElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "insertSelectiveWithPrimaryKey";

    public InsertSelectiveWithPrimaryKeyElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");

        answer.addAttribute(new Attribute(
                "id", selectAllStatementId)); 

        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        answer.addAttribute(new Attribute("parameterType", 
                parameterType.getFullyQualifiedName()));

        context.getCommentGenerator().addComment(answer);


        StringBuilder sb = new StringBuilder();

        sb.append("insert into ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement insertTrimElement = new XmlElement("trim"); 
        insertTrimElement.addAttribute(new Attribute("prefix", "(")); 
        insertTrimElement.addAttribute(new Attribute("suffix", ")")); 
        insertTrimElement.addAttribute(new Attribute("suffixOverrides", ",")); 
        answer.addElement(insertTrimElement);

        XmlElement valuesTrimElement = new XmlElement("trim"); 
        valuesTrimElement.addAttribute(new Attribute("prefix", "values (")); 
        valuesTrimElement.addAttribute(new Attribute("suffix", ")")); 
        valuesTrimElement.addAttribute(new Attribute("suffixOverrides", ",")); 
        answer.addElement(valuesTrimElement);

        for (IntrospectedColumn introspectedColumn : ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable
                .getAllColumns())) {

            if (introspectedColumn.isSequenceColumn()
                    || introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                // if it is a sequence column, it is not optional
                // This is required for MyBatis3 because MyBatis3 parses
                // and calculates the SQL before executing the selectKey

                // if it is primitive, we cannot do a null check
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities
                        .getEscapedColumnName(introspectedColumn));
                sb.append(',');
                insertTrimElement.addElement(new TextElement(sb.toString()));

                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities
                        .getParameterClause(introspectedColumn));
                sb.append(',');
                valuesTrimElement.addElement(new TextElement(sb.toString()));

                continue;
            }

            XmlElement insertNotNullElement = new XmlElement("if"); 
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); 
            insertNotNullElement.addAttribute(new Attribute(
                    "test", sb.toString())); 

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(',');
            insertNotNullElement.addElement(new TextElement(sb.toString()));
            insertTrimElement.addElement(insertNotNullElement);

            XmlElement valuesNotNullElement = new XmlElement("if"); 
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); 
            valuesNotNullElement.addAttribute(new Attribute(
                    "test", sb.toString())); 

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            sb.append(',');
            valuesNotNullElement.addElement(new TextElement(sb.toString()));
            valuesTrimElement.addElement(valuesNotNullElement);
        }
        parentElement.addElement(answer);
    }
}
