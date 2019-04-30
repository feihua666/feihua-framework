package feihua.jdbc.api.pojo;

import java.util.Date;

/**
 * 数据持久化对象基类
 * Created by yangwei
 * Created at 2017-08-16 15:23:26
 */
public class BasePo<PK> extends BasePojo{


    /**
     * 系统默认的用户id
     */
    public static final String DEFAULT_USER_ID = "0";
    public static final String COLUMN_DEL_FLAG = "del_flag";
    public static final String COLUMN_UPDATE_BY = "update_by";
    public static final String COLUMN_UPDATE_AT = "update_at";
    public static final String COLUMN_CREATE_BY = "create_by";
    public static final String COLUMN_CREATE_AT = "create_at";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA_USER_ID = "data_user_id";
    public static final String COLUMN_DATA_OFFICE_ID = "data_office_id";
    public static final String COLUMN_DATA_TYPE = "data_type";
    public static final String COLUMN_AREA_TYPE = "data_area_id";

    public static final String PROPERTY_DEL_FLAG = "delFlag";
    public enum YesNo {
        /**
         * 否
         */
        Y,
        /**
         * 是
         */
        N
    };

    /**
     * 主键
     */
    private PK id;
    /**
     * 创建人
     */
    private PK createBy;
    /**
     * 更新人
     */
    private PK updateBy;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 更新时间
     */
    private Date updateAt;
    /**
     * 是否删除
     */
    private String delFlag = YesNo.N.name();
    /**
     * 数据归属人id
     */
    private PK dataUserId;
    /**
     * 数据归属机构id
     */
    private PK dataOfficeId;
    /**
     * 数据分类
     */
    private String dataType;
    private PK dataAreaId;
    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public PK getCreateBy() {
        return createBy;
    }

    public void setCreateBy(PK createBy) {
        this.createBy = createBy;
    }

    public PK getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(PK updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public PK getDataUserId() {
        return dataUserId;
    }

    public void setDataUserId(PK dataUserId) {
        this.dataUserId = dataUserId;
    }

    public PK getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(PK dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public PK getDataAreaId() {
        return dataAreaId;
    }

    public void setDataAreaId(PK dataAreaId) {
        this.dataAreaId = dataAreaId;
    }
}
