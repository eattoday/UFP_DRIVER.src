<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <title>待办查询</title>
    <%--<meta http-equiv="content-type" content="application/vnd.ms-excel;charset=UTF-8"/>--%>
    <style>

    </style>
</head>
<body>

<br>
<br>
<div>待办查询</div>
<br>
<br>

<script>
    $(function () {
        $.post("http://localhost:9087/UFP_DRIVER/workFlowController.do?method=getMyWaitingTasks",
            {"accountId": "root"}, function (data) {
                var dataList=data.exhibitDatas;
                for (i = 0; i < dataList.length; i++) {
                    $("#itemu").append(
                        "<tr>" +
                        "<td >" +
                        "<input value='活动名称:" + dataList[i].activityInstName + "'>" +
                        "</td>" +
                        "<td >" +
                        "<input value='流程实例ID:" + dataList[i].processInstID + "'>" +
                        "</td>" +
                        "<td >" +
                        "<input value='流程ID:" + dataList[i].processModelId + "'>" +
                        "</td>" +
                        "<td >" +
                        "<input value='任务ID:" + dataList[i].taskInstID + "'>" +
                        "</td>" +
                        "<td >" +
                        "<a href=http://localhost:9087/UFP_DRIVER/workFlowController.do?method=getTable&activityDefID=" + dataList[i].activityDefID + "&processModelId=" + dataList[i].processModelId + ">" + "查询表单" + "</a>" +
                        "</td>" +
                        "<td >" +
                        "<a href=http://localhost:9087/UFP_DRIVER/workFlowController.do?method=submitTask&participants=['root']&accountId=ght&taskInstanceID=" + dataList[i].taskInstID + ">" + "提交任务" + "</a>" +
                        "</td>" +
                        "</tr>");
                }
            }, "json");
    })
</script>

<table id="itemu"></table>

</body>
</html>
