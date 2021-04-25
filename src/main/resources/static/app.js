$(function () {
    $.getJSON("monitor/taskList.do", function (result) {
        $.each(result.data, function (i, task) {
            $("#task_body").append('<tr>' +
                '<td>' + (i + 1) + '</td>' +
                '<td>' + task.namespace + '</td>' +
                '<td>' + task.name + '</td>' +
                '<td>' + (task.type == 1 ? ('cronjob') : (task.type == 2 ? 'deployment' : '未知')) + '</td>' +
                '<td>' + (task.cronExp == null ? '-' : task.cronExp) + '</td>' +
                '<td><button type="button" class="btn btn-info" data-namespace="' + task.namespace + '" data-taskname="' + task.name + '" data-type="' + task.type + '">查看日志</button></td>' +
                '</tr>');
        });

        $(".btn").on("click", function () {
            var namespace = $(this).data('namespace');
            var type = $(this).data('type');
            var taskname = $(this).data('taskname');
            //获取容器列表
            $.getJSON("monitor/podList.do?namespace=" + namespace + "&type=" + type + "&taskname=" + taskname, function (podResult) {
                if (podResult.data.length == 1) {
                    //只一个容器则直接获取它的日志
                    $("#logModal").modal();
                    var podName = podResult.data[0].name;
                    $.getJSON("monitor/podLog.do?namespace=" + namespace + "&podname=" + podName, function (logResult) {
                        $("#pod_name").html(podName)
                        $("#log_window").val(logResult.data)
                    })
                } else {
                    //有多个容器，则让用户选择查看哪个容器的日志
                    $("#choosePodModal").modal();
                    $("#task_name").html(taskname);
                    $("#pod_list_body").html('');
                    $.each(podResult.data, function (i, pod) {
                        $("#pod_list_body").append('<tr>' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + pod.name + '</td>' +
                            '<td>' + pod.createTime + '</td>' +
                            '<td><button type="button" class="pod-log-btn btn btn-info" data-namespace="' + pod.namespace + '" data-podname="' + pod.name + '" data-type="' + pod.type + '">查看日志</button></td>' +
                            '</tr>');
                    });

                    $(".pod-log-btn").on("click", function () {
                        var podName = $(this).data('podname');
                        $.getJSON("monitor/podLog.do?namespace=" + namespace + "&podname=" + podName, function (logResult) {
                            $("#choosePodModal").modal('hide');
                            $("#logModal").modal();
                            $("#pod_name").html(podName);
                            $("#log_window").val(logResult.data);
                        })
                    });
                }
            })
        });
    });
});