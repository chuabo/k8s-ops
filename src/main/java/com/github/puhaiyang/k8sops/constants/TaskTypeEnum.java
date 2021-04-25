package com.github.puhaiyang.k8sops.constants;

/**
 * @author puhaiyang
 * @date 2021/4/24 9:33
 * task类型
 */
public enum TaskTypeEnum {
    /**
     * 1:cronjob类型task
     */
    CRONJOB(Integer.valueOf(1).shortValue(), "cronjob类型task"),
    /**
     * 2:deployment类型task,高频task
     */
    DEPLOYMENT(Integer.valueOf(2).shortValue(), "deployment类型task,高频task");

    private final Short type;
    private final String desc;

    TaskTypeEnum(Short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据code代码获取对应的TaskTypeEnum
     *
     * @param type type
     * @return TaskTypeEnum
     */
    public static TaskTypeEnum getByCode(Short type) {
        TaskTypeEnum[] values = TaskTypeEnum.values();
        for (TaskTypeEnum taskTypeEnum : values) {
            if (taskTypeEnum.getType().equals(type)) {
                return taskTypeEnum;
            }
        }
        return null;
    }

    public Short getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
