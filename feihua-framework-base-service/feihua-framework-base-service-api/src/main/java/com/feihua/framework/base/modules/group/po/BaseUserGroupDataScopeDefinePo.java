package com.feihua.framework.base.modules.group.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-20 13:26:41
 * Database Table Remarks:
 *   用户分组数据范围定义表
 *
 * This class corresponds to the database table base_user_group_data_scope_define
 * @mbg.generated do_not_delete_during_merge 2019-06-20 13:26:41
*/
public class BaseUserGroupDataScopeDefinePo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   数据范围ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_group_data_scope_define.DATA_SCOPE_ID
     *
     * @mbg.generated 2019-06-20 13:26:41
     */
    private String dataScopeId;

    /**
     * Database Column Remarks:
     *   数据范围类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_group_data_scope_define.TYPE
     *
     * @mbg.generated 2019-06-20 13:26:41
     */
    private String type;

    public String getDataScopeId() {
        return dataScopeId;
    }

    public void setDataScopeId(String dataScopeId) {
        this.dataScopeId = dataScopeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public com.feihua.framework.base.modules.group.api.ApiBaseUserGroupDataScopeDefinePoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.base.modules.group.api.ApiBaseUserGroupDataScopeDefinePoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dataScopeId=").append(dataScopeId);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }
}