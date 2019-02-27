package com.feihua.framework.base.generator;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Iterator;

/**
 * 查询全部方法
 * Created by yangwei
 * Created at 2017/8/25 9:11
 */
public class SelectAllElementGenerator extends
        AbstractXmlElementGenerator {

    public static String selectAllStatementId = "selectAll";

    public SelectAllElementGenerator() {
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

        XmlElement isTrueElement = new XmlElement("if");
        sb.setLength(0);
        //_parameter是mybatis只有一个参数的固定取法，要不在if表达式中会报错
        sb.append("_parameter == false");
        isTrueElement.addAttribute(new Attribute("test", sb.toString()));
        answer.addElement(isTrueElement);
        sb.setLength(0);
        sb.append(" where del_flag = ").append("'").append(BasePo.YesNo.N.name()).append("'");
        isTrueElement.addElement(new TextElement(sb.toString()));

        String orderByClause = introspectedTable.getTableConfigurationProperty(PropertyRegistry.TABLE_SELECT_ALL_ORDER_BY_CLAUSE);
        boolean hasOrderBy = StringUtility.stringHasValue(orderByClause);
        if (hasOrderBy) {
            sb.setLength(0);
            sb.append("order by "); //$NON-NLS-1$
            sb.append(orderByClause);
            answer.addElement(new TextElement(sb.toString()));
        }

        parentElement.addElement(answer);
    }
}
