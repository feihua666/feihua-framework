package com.feihua.framework.base.generator;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * 查询多个数据方法
 * Created by yangwei
 * Created at 2017年11月8日 16:10:27
 */
public class SelectByPrimaryKeysElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "selectByPrimaryKeys";

    public SelectByPrimaryKeysElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");

        answer.addAttribute(new Attribute(
                "id", selectAllStatementId));

        String resultMap = introspectedTable.getBaseResultMapId();
        if(introspectedTable.hasBLOBColumns()){
            resultMap = introspectedTable.getResultMapWithBLOBsId();
        }
        answer.addAttribute(new Attribute("resultMap",resultMap));
        String parameterType = "java.util.List";

        answer.addAttribute(new Attribute("parameterType",
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());
        if (introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(","));
            answer.addElement(getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));



        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" in ");
            answer.addElement(new TextElement(sb.toString()));

            XmlElement foreach = new XmlElement("foreach");
            foreach.addAttribute(new Attribute("collection", "primaryKeys"));
            foreach.addAttribute(new Attribute("index", "index"));
            foreach.addAttribute(new Attribute("item", "item"));
            foreach.addAttribute(new Attribute("open", "("));
            foreach.addAttribute(new Attribute("separator", ","));
            foreach.addAttribute(new Attribute("close", ")"));
            foreach.addElement(new TextElement("#{item}"));
            answer.addElement(foreach);

        }
        //include
        XmlElement isNotNullElement = new XmlElement("if");
        isNotNullElement.addAttribute(new Attribute("test", "includeDeleted == false"));
        isNotNullElement.addElement(new TextElement("and del_flag = '"+ BasePo.YesNo.N.name() +"'"));
        answer.addElement(isNotNullElement);


        parentElement.addElement(answer);
    }
}
