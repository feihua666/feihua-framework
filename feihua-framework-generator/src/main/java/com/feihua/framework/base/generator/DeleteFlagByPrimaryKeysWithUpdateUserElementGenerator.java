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
 * 删除，更新删除标记为删除
 * Created by yangwei
 * Created at 2017/8/25 9:11
 */
public class DeleteFlagByPrimaryKeysWithUpdateUserElementGenerator extends
        AbstractXmlElementGenerator {

    public static String statementId = "deleteFlagByPrimaryKeysWithUpdateUser";
    private boolean isSimple;

    public DeleteFlagByPrimaryKeysWithUpdateUserElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }
    public DeleteFlagByPrimaryKeysWithUpdateUserElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");

        answer
                .addAttribute(new Attribute(
                        "id", statementId));

        String parameterClass;
        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterClass = introspectedTable.getPrimaryKeyType();
        } else {
            // PK fields are in the base class. If more than on PK
            // field, then they are coming in a map.
            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                parameterClass = "map";
            } else {
                parameterClass = introspectedTable.getPrimaryKeyColumns()
                        .get(0).getFullyQualifiedJavaType().toString();
            }
        }
        answer.addAttribute(new Attribute("parameterType",
                parameterClass));

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

            if("del_flag".equalsIgnoreCase(introspectedColumn.getActualColumnName())){
                sb.append(MyBatis3FormattingUtilities
                        .getEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                //sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                sb.append(" 'Y' ");


                if (iter.hasNext()) {
                    sb.append(',');
                }
                answer.addElement(new TextElement(sb.toString()));
            } else
            if("update_at".equalsIgnoreCase(introspectedColumn.getActualColumnName())){
                sb.append(MyBatis3FormattingUtilities
                        .getEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                //sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                sb.append(" now() ");


                if (iter.hasNext()) {
                    sb.append(',');
                }
                answer.addElement(new TextElement(sb.toString()));
            } else
            if("update_by".equalsIgnoreCase(introspectedColumn.getActualColumnName())){
                sb.append(MyBatis3FormattingUtilities
                        .getEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                //sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                sb.append(" #{userId} ");


                if (iter.hasNext()) {
                    sb.append(',');
                }
                answer.addElement(new TextElement(sb.toString()));

            }else {
                continue;
            }




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

            answer.addElement(new TextElement("and del_flag = 'N'"));
        }
        parentElement.addElement(answer);
    }
}
