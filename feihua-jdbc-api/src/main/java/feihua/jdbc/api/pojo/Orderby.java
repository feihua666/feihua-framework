package feihua.jdbc.api.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 排序对象
 * Created by yw on 2016/1/26.
 */
public class Orderby implements Serializable{
    public static String type_desc = "desc";
    public static String type_asc = "asc";
    private boolean orderable = false;
    List<Statement> statements = new ArrayList<>();

    public List<Statement> getStatements() {
        if (this.statements.isEmpty()) return statements;
        Collections.sort(this.statements,new Comparator<Statement>(){
            @Override
            public int compare(Statement s1, Statement s2) {
                if(s1.getIndex() == s2.getIndex()){
                    return 0;
                }else if (s1.getIndex() > s2.getIndex()){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(Statement statement:getStatements()){
            if(statement.isEnable()){
                sb.append(statement.getName()).append(" ").append(statement.getType()).append(",");
            }
        }
        return sb.substring(0,sb.length()-1);
    }

    public static class  Statement {
        public Statement(int index,String name,String type,boolean enable){
            this.index = index;
            this.name = name;
            this.type = type;
            this.enable = enable;
        }
        public Statement(int index,String name,String type){
            this(index,name,type,true);
        }
        /**
         * 顺序
         */
        private int index;
        private String name;
        private String type = type_asc;
        private boolean enable = true;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

    }
}
