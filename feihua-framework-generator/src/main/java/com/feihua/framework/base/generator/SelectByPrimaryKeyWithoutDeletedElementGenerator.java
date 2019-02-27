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
public class SelectByPrimaryKeyWithoutDeletedElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "selectByPrimaryKeyWithoutDeleted";

    public SelectByPrimaryKeyWithoutDeletedElementGenerator() {
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
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));

        }
        answer.addElement(new TextElement("and del_flag = '"+ BasePo.YesNo.N.name() +"'"));


        parentElement.addElement(answer);
    }
}
