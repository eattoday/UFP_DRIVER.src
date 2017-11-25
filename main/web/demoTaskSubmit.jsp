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
    <title>表单查询</title>
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
<div >表单查询</div>

<iframe id="frame1" width="90%" height="300px"
        src="http://10.225.222.200/cform/jsp/cform/tasklist/render/formrender.jsp?formId=<%=formID%>&tenantId=<%=tenantId%>"  >

</iframe>
<br>
<input onclick="sub()" type="button" value="提交">
<script>
    function sub() {
//        var doc ;
        $("#frame1").contents().find("<%=formID%>")
        $("#frame1").contents().find("#BaoXiaoLiuCheng30").submit();
//        doc= document.getElementById("frame1").contentDocument;
//        doc.getElementById("BaoXiaoLiuCheng30").action("http://localhost:9087/UFP_DRIVER/workFlowController.do?method=getTable");
//        doc.getElementById("BaoXiaoLiuCheng30").submit();
    }
</script>




</body>
</html>
