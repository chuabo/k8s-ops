package com.github.puhaiyang.k8sops.controller;

import com.github.puhaiyang.k8sops.bean.DeploymentVO;
import com.github.puhaiyang.k8sops.bean.PodVO;
import com.github.puhaiyang.k8sops.bean.Result;
import com.github.puhaiyang.k8sops.bean.TaskVO;
import com.github.puhaiyang.k8sops.constants.TaskTypeEnum;
import com.github.puhaiyang.k8sops.service.MonitorService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author puhaiyang
 * @date 2021/4/25 20:32
 * 监控controller
 */
@RestController
@RequestMapping(value = "monitor")
public class MonitorController {
    @Resource
    private SimpMessagingTemplate messagingTemplate;
    @Resource
    private MonitorService monitorService;

    /**
     * 获取pod的日志
     *
     * @param namespace namespace
     * @param podname   podname
     * @return Result<String>
     */
    @GetMapping(value = "podLog.do")
    public Result<String> getPodLog(String namespace, String podname) {
        return Result.success(monitorService.getPodLog(namespace, podname));
    }

    /**
     * 获取pod列表
     *
     * @param namespace namespace
     * @param type      应用类型,参看com.github.puhaiyang.k8sops.constants.TaskTypeEnum
     * @param taskname  应用名称
     * @return Result<List < PodVO>>
     */
    @GetMapping(value = "podList.do")
    public Result<List<PodVO>> getPodList(String namespace, Short type, String taskname) {
        //获取pod列表
        List<PodVO> podVOList = monitorService.getPodList(namespace, TaskTypeEnum.getByCode(type), taskname);
        return Result.success(podVOList);
    }

    /**
     * 获取task列表,cronjob和deployment资源列表
     *
     * @param namespace namespace
     * @return Result<List < TaskVO>>
     */
    @GetMapping(value = "taskList.do")
    public Result<List<TaskVO>> getTaskList(String namespace) {
        List<TaskVO> taskVoList = monitorService.getTaskList(namespace);
        return Result.success(taskVoList);
    }

    /**
     * 获取deployment列表
     *
     * @param namespace namespace
     * @return Result<List < DeploymentVO>>
     */
    @GetMapping(value = "deploymentList.do")
    public Result<List<DeploymentVO>> deploymentList(String namespace) {
        List<DeploymentVO> deploymentVOList = monitorService.getDeploymentList(namespace);
        return Result.success(deploymentVOList);
    }
}
