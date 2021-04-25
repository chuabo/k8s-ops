package com.github.puhaiyang.k8sops.bean;

import java.util.Date;

/**
 * @author puhaiyang
 * @date 2021/4/25 20:41
 * TaskVO
 */
public class TaskVO {
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * cron表达式
     */
    private String cronExp;
    /**
     * 类型 1-cron-task 2-deployemnt-task
     */
    private Short type;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次运行时间
     */
    private Date lastExceuteTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastExceuteTime() {
        return lastExceuteTime;
    }

    public void setLastExceuteTime(Date lastExceuteTime) {
        this.lastExceuteTime = lastExceuteTime;
    }
}
