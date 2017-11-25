<%@ page import="com.metarnet.core.common.utils.Constants" %>
<%@ page import="com.metarnet.core.common.workflow.TaskInstance" %>
<%@ page import="java.util.List" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>
<%
    String formID= (String) request.getAttribute("formID");
    String tenantId=(String) request.getAttribute("tenantId");
%>


<head>
    <title>表单</title>
    <%--<meta http-equiv="content-type" content="application/vnd.ms-excel;charset=UTF-8"/>--%>
    <style>

    </style>
</head>
<body>
<br>
<br>
<br>
<br>
<br>
<div >表单</div>

<form  id="myForm" method="post" action="http://localhost:9087/UFP_DRIVER/workFlowController.do?method=submitTask" >

    <input id="formId" type="text" name="formId" value="">
    <input id="formDataId" type="text" name="formDataId" value="">
    <input id="formType" type="text" name="formType" value="1">


    <input id="tenantId" type="hidden" name="tenantId" value="default">
    <input id="participants" type="hidden" name="participants" value="['ght']">
    <input id="accountId" type="hidden" name="accountId" value="ght">
    <input id="taskInstanceID" type="hidden" name="taskInstanceID" value="18122">

    <input id="jqsubmit" type="button" value="提交">

</form>

<script >
    $("#jqsubmit").click(function () {
//        var myDate = new Date();
//        var date=myDate.getDate();
//        $("#formId").val(date);
//        $("#formDataId").val(date+"data");
        $("#myForm").submit();
    })

</script>




</body>
</html>
