package com.feihua.framework.base.generator;

import feihua.jdbc.api.pojo.BasePo;
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
 * 更新方法
 * Created by yangwei
 * Created at 2017/8/25 9:11
 */
public class UpdateBatchByPrimaryKeysElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "updateBatchByPrimaryKeys";

    public UpdateBatchByPrimaryKeysElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute(
                "id", selectAllStatementId));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); 
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); 

        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns()).iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); 
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString().replace("#{","#{entity.")));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

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

        parentElement.addElement(answer);
    }
}
