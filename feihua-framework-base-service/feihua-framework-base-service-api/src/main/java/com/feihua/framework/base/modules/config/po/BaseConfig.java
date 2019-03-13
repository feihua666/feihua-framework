package com.feihua.framework.base.modules.config.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author revolver 2019-03-12 15:01:39
 *
 * This class corresponds to the database table base_config
 * @mbg.generated do_not_delete_during_merge 2019-03-12 15:01:39
*/
public class BaseConfig extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   配置KEY
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_config.config_key
     *
     * @mbg.generated 2019-03-12 15:01:39
     */
    private String configKey;

    /**
     * Database Column Remarks:
     *   配置VALUE
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_config.config_value
     *
     * @mbg.generated 2019-03-12 15:01:39
     */
    private String configValue;

    /**
     * Database Column Remarks:
     *   配置描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_config.description
     *
     * @mbg.generated 2019-03-12 15:01:39
     */
    private String description;

    /**
     * Database Column Remarks:
     *   可用状态：Y/N
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_config.status
     *
     * @mbg.generated 2019-03-12 15:01:39
     */
    private String status;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public com.feihua.framework.base.modules.config.api.ApiBaseConfigService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.base.modules.config.api.ApiBaseConfigService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", configKey=").append(configKey);
        sb.append(", configValue=").append(configValue);
        sb.append(", description=").append(description);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}