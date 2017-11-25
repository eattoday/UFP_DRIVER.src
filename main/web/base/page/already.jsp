<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>
<head>
    <title>已办列表</title>
    <%--<meta http-equiv="content-type" content="application/vnd.ms-excel;charset=UTF-8"/>--%>
    <link rel="stylesheet" type="text/css" href="<%=path%>/component/jquery-outFetterTable/jquery.outFetterTable.css"/>
<style>

</style>
</head>
<body>
<div id="todoListContainer"></div>
<script type="text/javascript" src="<%=path%>/component/jquery-outFetterTable/jquery.outFetterTable.js"></script>
<script type="text/javascript">

    var __settings = {
        renderTo:'todoListContainer',
        loadURL : _PATH + '/workFlowController.do?method=getMyWaitingTasks&accountId=ght',
//        extendRow : function(record){
//            return '<tr><td colspan="2" style="padding-top:0px;">'+record.jobTitle+'</a></td><td colspan="2" style="padding-top:0px;text-align:right">'+record.activityInstName+'</td></tr>';
//        },
        columns : [{
            title : '工单编号',
            column : 'jobCode',
            wrapFunction : function(record , __data_value){
                return '<a style="text-decoration:underline" href="<%=path%>/driverController.do?method=build&fromPage=already&type=already&buildMethod=build&'+
                        '&processInstID='+record.processInstID+
                        '&processModelId='+record.processModelId+
                        '&processModelName='+record.processModelName+
                        '&activityInstID='+record.activityInstID+
                        '&activityDefID='+record.activityDefID+
                        '&taskInstID='+record.taskInstID+
                        '&activityInstName='+encodeURIComponent(encodeURIComponent(record.activityInstName))+
                        '&jobTitle='+encodeURIComponent(encodeURIComponent(record.jobTitle))+
                        '&jobCode='+encodeURIComponent(encodeURIComponent(record.jobCode))+
                        '&jobID='+record.jobID+
                        '&appID='+record.appID+
                        '&shard='+record.shard+
                        '&businessId='+record.businessId+
                        '&rootProcessInstId='+record.rootProcessInstId+
                        '&createDate='+record.createDate+
                        '&taskWarning='+record.taskWarning+
                        '&strColumn4='+record.strColumn4+
                        '&__returnUrl=/base/frame/todo.jsp'+
                        '">'+__data_value+'</a>';
            },
            highQuery:true,
            highQueryType:'lk'
        } , {
            title : '工单环节',
            column : 'activityInstName',
            width : '20%',
            textAlign:'left',
            highQuery:true,
            highQueryType:'lk'

        }, {
            title : '操作时间',
            column : 'completionDate',
            width : '20%',
            textAlign:'left',
            highQuery:true,
            highQueryType:'range'
        } ]
    }

    var outFetterTable = $.fn.outFetterTable.init({
        __settings : __settings
    });
</script>
</body>
</html>
