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
 * 更新方法
 * Created by yangwei
 * Created at 2017/8/25 9:11
 */
public class UpdateBatchByPrimaryKeyElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "updateBatchByPrimaryKey";

    public UpdateBatchByPrimaryKeyElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute(
                "id", selectAllStatementId));

        String parameterType = "java.util.List";

        answer.addAttribute(new Attribute("parameterType", 
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); 
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column

        XmlElement trimSet = new XmlElement("trim");
        trimSet.addAttribute(new Attribute("prefix","set"));
        trimSet.addAttribute(new Attribute("suffixOverrides",","));
        answer.addElement(trimSet);

        sb.setLength(0);
        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns()).iterator();
        while (iter.hasNext()) {

            IntrospectedColumn introspectedColumn = iter.next();
            XmlElement trimColum = new XmlElement("trim");
            trimColum.addAttribute(new Attribute("prefix",""+ introspectedColumn.getActualColumnName() +" = case"));
            trimColum.addAttribute(new Attribute("suffix","end,"));
            trimSet.addElement(trimColum);

            XmlElement foreach = new XmlElement("foreach");
            foreach.addAttribute(new Attribute("collection", "entities"));
            foreach.addAttribute(new Attribute("index", "index"));
            foreach.addAttribute(new Attribute("item", "item"));

            foreach.addElement(new TextElement("when id=#{item.id} then #{item."+ introspectedColumn.getJavaProperty() +"}"));
            trimColum.addElement(foreach);

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
            foreach.addAttribute(new Attribute("collection", "entities"));
            foreach.addAttribute(new Attribute("index", "index"));
            foreach.addAttribute(new Attribute("item", "item"));
            foreach.addAttribute(new Attribute("open", "("));
            foreach.addAttribute(new Attribute("separator", ","));
            foreach.addAttribute(new Attribute("close", ")"));
            foreach.addElement(new TextElement("#{item."+ introspectedColumn.getJavaProperty() +"}"));
            answer.addElement(foreach);

        }

        parentElement.addElement(answer);
    }
}
