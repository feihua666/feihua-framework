package feihua.jdbc.api.pojo;

/**
 * 数据持久化对象树形结构类
 * Created by yangwei
 * Created at 2017-08-16 15:23:34
 */
public class BaseTreePo<PK> extends BasePo<PK> {

    public static final String defaultRootParentId = "0";
    public static final String defaultParentIdX = "0";

    public static final int defaultRootLevel = 1;

    public static final int maxLevel = 11;

    public static final String COLUMN_PARENT_ID = "parent_id";
    public static final String PROPERTY_PARENT_ID = "parentId";

    private Integer level;
    /**
     * 直接父级id
     */
    private PK parentId;
    /**
     * level为1的父级id
     */
    private PK parentId1;
    /**
     * level为2的父级id
     */
    private PK parentId2;
    /**
     * level为3的父级id
     */
    private PK parentId3;
    /**
     * level为4的父级id
     */
    private PK parentId4;
    /**
     * level为5的父级id
     */
    private PK parentId5;
    /**
     * level为6的父级id
     */
    private PK parentId6;
    /**
     * level为7的父级id
     */
    private PK parentId7;
    /**
     * level为8的父级id
     */
    private PK parentId8;
    /**
     * level为9的父级id
     */
    private PK parentId9;
    /**
     * level为10的父级id
     */
    private PK parentId10;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public PK getParentId() {
        return parentId;
    }

    public void setParentId(PK parentId) {
        this.parentId = parentId;
    }

    public PK getParentId1() {
        return parentId1;
    }

    public void setParentId1(PK parentId1) {
        this.parentId1 = parentId1;
    }

    public PK getParentId2() {
        return parentId2;
    }

    public void setParentId2(PK parentId2) {
        this.parentId2 = parentId2;
    }

    public PK getParentId3() {
        return parentId3;
    }

    public void setParentId3(PK parentId3) {
        this.parentId3 = parentId3;
    }

    public PK getParentId4() {
        return parentId4;
    }

    public void setParentId4(PK parentId4) {
        this.parentId4 = parentId4;
    }

    public PK getParentId5() {
        return parentId5;
    }

    public void setParentId5(PK parentId5) {
        this.parentId5 = parentId5;
    }

    public PK getParentId6() {
        return parentId6;
    }

    public void setParentId6(PK parentId6) {
        this.parentId6 = parentId6;
    }

    public PK getParentId7() {
        return parentId7;
    }

    public void setParentId7(PK parentId7) {
        this.parentId7 = parentId7;
    }

    public PK getParentId8() {
        return parentId8;
    }

    public void setParentId8(PK parentId8) {
        this.parentId8 = parentId8;
    }

    public PK getParentId9() {
        return parentId9;
    }

    public void setParentId9(PK parentId9) {
        this.parentId9 = parentId9;
    }

    public PK getParentId10() {
        return parentId10;
    }

    public void setParentId10(PK parentId10) {
        this.parentId10 = parentId10;
    }
}
