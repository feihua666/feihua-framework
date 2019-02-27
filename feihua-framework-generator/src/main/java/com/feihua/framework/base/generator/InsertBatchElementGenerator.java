package com.feihua.framework.base.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
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
public class InsertBatchElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "insertBatch";

    public InsertBatchElementGenerator() {
        super();
    }
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert"); 

        answer.addAttribute(new Attribute(
                "id", selectAllStatementId)); 

        String parameterType = "java.util.List";

        answer.addAttribute(new Attribute("parameterType",
                parameterType));

        context.getCommentGenerator().addComment(answer);

        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            IntrospectedColumn introspectedColumn = introspectedTable
                    .getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (introspectedColumn != null) {
                if (gk.isJdbcStandard()) {
                    answer.addAttribute(new Attribute(
                            "useGeneratedKeys", "true"));  
                    answer.addAttribute(new Attribute(
                            "keyProperty", introspectedColumn.getJavaProperty())); 
                    answer.addAttribute(new Attribute(
                            "keyColumn", introspectedColumn.getActualColumnName())); 
                } else {
                    XmlElement selectKey = getSelectKey(introspectedColumn, gk);
                    selectKey.getElements().clear();
                    selectKey.addElement(new TextElement("select replace(uuid(),'-','') from dual"));
                    // 注释掉，批量插入不生成selectedkey
                    // answer.addElement(selectKey);
                }
            }
        }

        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();

        insertClause.append("insert into "); 
        insertClause.append(introspectedTable
                .getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" ("); 

        //valuesClause.append("values (");
        valuesClause.append(" (");

        List<String> valuesClauses = new ArrayList<String>();
        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn introspectedColumn = columns.get(i);

            insertClause.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            //如果是主键
            if(introspectedColumn.getActualColumnName().equals(gk.getColumn())){
                valuesClause.append("replace(uuid(),'-','')");
            }else{
                valuesClause.append(MyBatis3FormattingUtilities
                        .getParameterClause(introspectedColumn));
            }

            if (i + 1 < columns.size()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }

            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);

                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }

        insertClause.append(')').append(" values ");
        answer.addElement(new TextElement(insertClause.toString()));

        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());
        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "entities"));
        foreach.addAttribute(new Attribute("index", "index"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("separator", ","));
        for (String clause : valuesClauses) {
            foreach.addElement(new TextElement(clause.replace("#{","#{item.")));
        }
        answer.addElement(foreach);
        parentElement.addElement(answer);
    }
}
