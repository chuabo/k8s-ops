package com.github.puhaiyang.k8sops.bean;

import java.util.Date;


/**
 * @author puhaiyang
 * @date 2021/4/25 21:01
 * DeploymentVo
 */
public class DeploymentVO {
    private String name;
    private String namespace;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
