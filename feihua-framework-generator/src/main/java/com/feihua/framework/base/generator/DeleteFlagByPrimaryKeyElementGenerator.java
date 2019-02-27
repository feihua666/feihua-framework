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
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Iterator;

/**
 * 删除，更新删除标记为删除
 * Created by yangwei
 * Created at 2017/8/25 9:11
 */
public class DeleteFlagByPrimaryKeyElementGenerator extends
        AbstractXmlElementGenerator {

    public static String statementId = "deleteFlagByPrimaryKey";
    private boolean isSimple;

    public DeleteFlagByPrimaryKeyElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }
    public DeleteFlagByPrimaryKeyElementGenerator() {
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

            if(!"del_flag".equalsIgnoreCase(introspectedColumn.getActualColumnName())){
                continue;
            }
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            //sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(" 'Y' ");

            /*if (iter.hasNext()) {
                sb.append(',');
            }*/

            answer.addElement(new TextElement(sb.toString()));

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
            sb.append(" = "); 
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
            answer.addElement(new TextElement("and del_flag = 'N'"));
        }
        parentElement.addElement(answer);
    }
}
