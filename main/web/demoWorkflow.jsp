<%@ page import="com.metarnet.core.common.utils.Constants" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>
<head>
    <title>流程查询</title>
</head>
<body>
<br>
<br>
<br>
<br>
<br>
<div>启动流程</div>

<form id="myForm" method="post" action="http://localhost:9087/UFP_DRIVER/workFlowController.do?method=startProcess">

    <table>

        <tr><input type="text" value="用户ID">
            <input id="accountId" type="text" name="accountId" value="ght"><br></tr>
        <tr><input type="text" value="候选人列表">
            <input id="participants" type="text" name="participants" value="['ght']"><br></tr>
        <tr><input type="text" value="流程模板ID">
            <input id="processModelID" type="text" name="processModelID" value="demo-1"><br></tr>
        <tr><input type="text" value="租户ID">
            <input id="tenantId" type="text" name="tenantId" value="default"><br></tr>
        <tr><input type="text" value="表单ID">
            <input id="formId" type="text" name="formId" value="1111"><br></tr>
        <tr><input type="text" value="表单数据ID">
            <input id="formDataId" type="text" name="formDataId" value="111data"><br></tr>
        <tr><input type="text" value="表单类型">
            <input id="formType" type="text" name="formType" value="1"><br></tr>


    </table>

    <input id="jqsubmit" type="button" value="启动新流程">

</form>

<script>
    $("#jqsubmit").click(function () {
        $("#myForm").submit();
    })

</script>


</body>
</html>
