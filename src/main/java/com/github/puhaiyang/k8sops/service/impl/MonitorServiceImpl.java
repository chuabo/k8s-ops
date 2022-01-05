package com.github.puhaiyang.k8sops.service.impl;

import com.github.puhaiyang.k8sops.K8sUtils;
import com.github.puhaiyang.k8sops.bean.DeploymentVO;
import com.github.puhaiyang.k8sops.bean.JobVO;
import com.github.puhaiyang.k8sops.bean.PodVO;
import com.github.puhaiyang.k8sops.bean.TaskVO;
import com.github.puhaiyang.k8sops.constants.Constants;
import com.github.puhaiyang.k8sops.constants.TaskTypeEnum;
import com.github.puhaiyang.k8sops.service.MonitorService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.CronJobList;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobList;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author puhaiyang
 * @date 2021/4/25 20:50
 * MonitorServiceImpl
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<TaskVO> getTaskList(String namespace) {
        List<TaskVO> taskVOList = new ArrayList<>();
        try (final KubernetesClient k8s = K8sUtils.getClient()) {
            CronJobList cronJobList = k8s.batch().cronjobs().inNamespace(namespace).list();
            List<CronJob> items = cronJobList.getItems();
            for (CronJob cronJob : items) {
                TaskVO taskVO = new TaskVO();
                try {
                    taskVO.setLastExceuteTime(DateUtils.parseDate(cronJob.getStatus().getLastScheduleTime(), Constants.K8S_TIME_PATTERN));
                    taskVO.setCreateTime(DateUtils.parseDate(cronJob.getMetadata().getCreationTimestamp(), Constants.K8S_TIME_PATTERN));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                taskVO.setType(TaskTypeEnum.CRONJOB.getType());
                taskVO.setName(cronJob.getMetadata().getName());
                taskVO.setNamespace(cronJob.getMetadata().getNamespace());
                taskVO.setCronExp(cronJob.getSpec().getSchedule());
                taskVOList.add(taskVO);
            }
        }
        //将deployment的数据也添加进去
        taskVOList.addAll(convert2TaskVOList(getDeploymentList(namespace)));
        return taskVOList;
    }

    /**
     * 转为taskVO列表
     *
     * @param deploymentList deploymentList
     * @return List<TaskVO>
     */
    private List<TaskVO> convert2TaskVOList(List<DeploymentVO> deploymentList) {
        List<TaskVO> taskVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deploymentList)) {
            deploymentList.forEach(deploymentVO -> {
                TaskVO taskVO = new TaskVO();
                taskVO.setNamespace(deploymentVO.getNamespace());
                taskVO.setName(deploymentVO.getName());
                taskVO.setCreateTime(deploymentVO.getCreateTime());
                taskVO.setType(TaskTypeEnum.DEPLOYMENT.getType());
                taskVOList.add(taskVO);
            });
        }
        return taskVOList;
    }

    @Override
    public List<DeploymentVO> getDeploymentList(String namespace) {
        List<DeploymentVO> deploymentVOList = new ArrayList<>();
        try (final KubernetesClient k8s = K8sUtils.getClient()) {
            DeploymentList deploymentList = k8s.apps().deployments().inNamespace(namespace).list();
            List<Deployment> items = deploymentList.getItems();
            for (Deployment deployment : items) {
                DeploymentVO deploymentVO = new DeploymentVO();
                try {
                    //创建时间转换
                    deploymentVO.setCreateTime(DateUtils.parseDate(deployment.getMetadata().getCreationTimestamp(), Constants.K8S_TIME_PATTERN));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                deploymentVO.setName(deployment.getMetadata().getName());
                deploymentVO.setNamespace(deployment.getMetadata().getNamespace());
                deploymentVOList.add(deploymentVO);
            }
        }
        return deploymentVOList;
    }

    @Override
    public List<PodVO> getPodList(String namespace, TaskTypeEnum taskTypeEnum, String taskname) {
        List<PodVO> podVOList = new ArrayList<>();
        if (TaskTypeEnum.CRONJOB == taskTypeEnum) {
            List<JobVO> jobVOList = getJobListByCronjob(namespace, taskname);
            for (JobVO jobVO : jobVOList) {
                podVOList.addAll(getPodListByJob(namespace, jobVO.getName()));
            }
        } else if (TaskTypeEnum.DEPLOYMENT == taskTypeEnum) {
            podVOList = getPodListByDeployment(namespace, taskname);
        } else {
            logger.error("[getPodList] 未知的type类型.namespace:{} taskname:{}", namespace, taskname);
        }
        return podVOList;
    }

    @Override
    public String getPodLog(String namespace, String podname) {
        try (final KubernetesClient k8s = K8sUtils.getClient()) {
            return k8s.pods().inNamespace(namespace).withName(podname).getLog();
        }
    }

    /**
     * 根据deployment名称获取pod列表
     *
     * @param namespace      namespace
     * @param deploymentname deploymentname
     * @return List<PodVO>
     */
    private List<PodVO> getPodListByDeployment(String namespace, String deploymentname) {
        List<PodVO> podVOList = new ArrayList<>();
        try (final KubernetesClient k8s = K8sUtils.getClient()) {
            PodList podList = k8s.pods().inNamespace(namespace).list();
            List<Pod> items = podList.getItems();
            if (StringUtils.isNotBlank(deploymentname)) {
                //过滤出符合条件的数据
                items = items.stream().filter(job -> job.getMetadata().getName().startsWith(deploymentname)).collect(Collectors.toList());
            }
            for (Pod pod : items) {
                PodVO podVO = getPodVO(pod);
                podVO.setDeploymentName(deploymentname);
                podVOList.add(podVO);
            }
        }
        return podVOList;
    }

    private List<PodVO> getPodListByJob(String namespace, String jobname) {
        List<PodVO> pobVOList = new ArrayList<>();
        try (final KubernetesClient k8s = K8sUtils.getClient()) {
            PodList podList = k8s.pods().inNamespace(namespace).list();
            if (StringUtils.isNotBlank(jobname)) {
                podList = k8s.pods().inNamespace(namespace).withLabel("job-name", jobname).list();
            }
            List<Pod> items = podList.getItems();
            for (Pod pod : items) {
                PodVO podVo = getPodVO(pod);
                podVo.setJobName(jobname);
                pobVOList.add(podVo);
            }
        }
        return pobVOList;
    }

    private PodVO getPodVO(Pod pod) {
        PodVO podVO = new PodVO();
        try {
            podVO.setCreateTime(DateUtils.parseDate(pod.getMetadata().getCreationTimestamp(), Constants.K8S_TIME_PATTERN));
            podVO.setStartTime(DateUtils.parseDate(pod.getStatus().getStartTime(), Constants.K8S_TIME_PATTERN));
        } catch (Exception e) {
            e.printStackTrace();
        }
        podVO.setName(pod.getMetadata().getName());
        podVO.setNamespace(pod.getMetadata().getNamespace());
        return podVO;
    }

    /**
     * 根据cronjob名称获取job列表
     *
     * @param namespace   namespace
     * @param cronjobName cronjobName
     * @return List<JobVO>
     */
    private List<JobVO> getJobListByCronjob(String namespace, String cronjobName) {
        List<JobVO> jobVOList = new ArrayList<>();
        try (final KubernetesClient k8s = K8sUtils.getClient()) {
            JobList jobList = k8s.batch().jobs().inNamespace(namespace).list();
            List<Job> items = jobList.getItems();
            if (StringUtils.isNotBlank(cronjobName)) {
                //过滤出符合条件的数据
                items = items.stream().filter(job -> job.getMetadata().getName().startsWith(cronjobName)).collect(Collectors.toList());
            }
            for (Job job : items) {
                JobVO jobVo = new JobVO();
                try {
                    jobVo.setCreateTime(DateUtils.parseDate(job.getMetadata().getCreationTimestamp(), Constants.K8S_TIME_PATTERN));
                    jobVo.setStartTime(DateUtils.parseDate(job.getStatus().getStartTime(), Constants.K8S_TIME_PATTERN));
                    jobVo.setEndTime(DateUtils.parseDate(job.getStatus().getCompletionTime(), Constants.K8S_TIME_PATTERN));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jobVo.setName(job.getMetadata().getName());
                jobVo.setNamespace(job.getMetadata().getNamespace());
                jobVo.setCronjobName(cronjobName);
                jobVOList.add(jobVo);
            }
        }
        return jobVOList;
    }
}
