package com.github.puhaiyang.k8sops.service;

import com.github.puhaiyang.k8sops.bean.DeploymentVO;
import com.github.puhaiyang.k8sops.bean.PodVO;
import com.github.puhaiyang.k8sops.bean.TaskVO;
import com.github.puhaiyang.k8sops.constants.TaskTypeEnum;

import java.util.List;

/**
 * @author puhaiyang
 * @date 2021/4/25 20:50
 * MonitorService
 */
public interface MonitorService {
    /**
     * 获取deployment和cronjob的task列表
     *
     * @param namespace namespace
     * @return List<TaskVO>
     */
    List<TaskVO> getTaskList(String namespace);

    /**
     * 获取deployment列表
     *
     * @param namespace namespace
     * @return List<DeploymentVO>
     */
    List<DeploymentVO> getDeploymentList(String namespace);

    /**
     * 获取pod列表
     *
     * @param namespace    namespace
     * @param taskTypeEnum taskTypeEnum
     * @param taskname     taskname,deployment或cronjob的名称
     * @return List<PodVO>
     */
    List<PodVO> getPodList(String namespace, TaskTypeEnum taskTypeEnum, String taskname);

    /**
     * 获取pod的日志
     *
     * @param namespace namespace
     * @param podname   podname
     * @return 日志
     */
    String getPodLog(String namespace, String podname);
}
