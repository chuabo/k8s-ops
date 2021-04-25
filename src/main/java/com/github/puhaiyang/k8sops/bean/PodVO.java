package com.github.puhaiyang.k8sops.bean;

import java.util.Date;


/**
 * @author puhaiyang
 * @date 2021/4/25 21:14
 * PodVO
 */
public class PodVO {
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
     * cronjob名称
     */
    private String cronjobName;
    /**
     * job名称
     */
    private String jobName;
    /**
     * deploymentName
     */
    private String deploymentName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 运行开始时间
     */
    private Date startTime;
    /**
     * 运行结束时间
     */
    private Date endTime;
    /**
     * 执行持续时间
     */
    private Long executeDuration;

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

    public String getCronjobName() {
        return cronjobName;
    }

    public void setCronjobName(String cronjobName) {
        this.cronjobName = cronjobName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getExecuteDuration() {
        return executeDuration;
    }

    public void setExecuteDuration(Long executeDuration) {
        this.executeDuration = executeDuration;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
}
