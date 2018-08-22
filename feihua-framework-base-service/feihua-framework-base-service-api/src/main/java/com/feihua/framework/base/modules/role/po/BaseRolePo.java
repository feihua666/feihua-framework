package com.feihua.framework.base.modules.role.po;

import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-01-05 16:33:35
 *
 * This class corresponds to the database table base_role
 * @mbg.generated do_not_delete_during_merge 2018-01-05 16:33:35
*/
public class BaseRolePo extends feihua.jdbc.api.pojo.BaseTreePo<String> {
    /**
     * Database Column Remarks:
     *   角色名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_role.NAME
     *
     * @mbg.generated 2018-01-05 16:33:35
     */
    private String name;

    /**
     * Database Column Remarks:
     *   角色编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_role.CODE
     *
     * @mbg.generated 2018-01-05 16:33:35
     */
    private String code;

    /**
     * Database Column Remarks:
     *   角色类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_role.TYPE
     *
     * @mbg.generated 2018-01-05 16:33:35
     */
    private String type;

    /**
     * Database Column Remarks:
     *   是否禁用，N=未禁用；Y=已禁用
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_role.DISABLED
     *
     * @mbg.generated 2018-01-05 16:33:35
     */
    private String disabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public ApiBaseRolePoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(ApiBaseRolePoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", name=").append(name);
        sb.append(", code=").append(code);
        sb.append(", type=").append(type);
        sb.append(", disabled=").append(disabled);
        sb.append("]");
        return sb.toString();
    }
}