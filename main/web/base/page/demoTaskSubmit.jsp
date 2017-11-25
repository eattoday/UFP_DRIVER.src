<%@ page import="com.metarnet.core.common.utils.Constants" %>
<%@ page import="com.metarnet.core.common.workflow.TaskInstance" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <title>待办详情</title>
    <%--<meta http-equiv="content-type" content="application/vnd.ms-excel;charset=UTF-8"/>--%>
    <style>

    </style>
</head>
<body >

<div >待办详情</div>

<%
    String srcList =(String) request.getAttribute("srcList");
    List list=JSONObject.parseObject(srcList, List.class);
    if (list != null) {
        for (int i=0;i<list.size()-1;i++) {

%>

    <input type="button" value="选择" onclick="button<%=i%>()">
    <br>

    <iframe  id="frame<%=i%>" width="100%" scrolling="auto" frameborder="1"  marginheight="5px"
            src=""  style="display: none">
    </iframe>

<script>
    function button<%=i%>() {
        <%--alert("<%=i%><%=i%><%=i%><%=i%>");--%>
        var iframe=document.getElementById("frame<%=i%>");
        iframe.setAttribute("src","<%=list.get(i)%>");

//        var iDoc = iframe.contentDocument || iframe.document
//        var height = calcPageHeight(iDoc)
//        iframe.setAttribute("height",height);
        iframe.setAttribute("height",document.documentElement.clientHeight/2);

        if(iframe.getAttribute("style")=="display: none"){
            iframe.setAttribute("style","display: table");
        }else {
            iframe.setAttribute("style","display: none");
        }
    }

    function calcPageHeight(doc) {
        var cHeight = doc.body.clientHeight+doc.documentElement.clientHeight
        var sHeight = Math.max(doc.body.scrollHeight, doc.documentElement.scrollHeight)
        var height  = cHeight;
        return height
    }
</script>

<%
        }%>

    <input type="text" value="选择" onclick="button<%=list.size()-1%>()">
    <br>

    <iframe  id="frame<%=list.size()-1%>" width="100%" scrolling="0" frameborder="1"  marginheight="5px"
             src="<%=list.get(list.size()-1)%>"  >
    </iframe>
    <script>
        function onload() {
            var iframe=document.getElementById("frame<%=list.size()-1%>");
            var iDoc = iframe.contentDocument || iframe.document
            var height = calcPageHeight(iDoc)
//            iframe.setAttribute("height",document.documentElement.clientHeight);
            iframe.setAttribute("height",height);
        }

        function calcPageHeight(doc) {
            var cHeight = Math.max(doc.body.clientHeight, doc.documentElement.clientHeight)
            var sHeight = Math.max(doc.body.scrollHeight, doc.documentElement.scrollHeight)
            var height  = cHeight+sHeight;
            return height
        }


        window.onload = onload();
    </script>

<%
    }
%>


<%--<iframe id="frame1" width="90%" height="300px"
        src="http://10.225.222.200/cform/jsp/cform/tasklist/render/formrender.jsp?formId=<%=formID%>&tenantId=<%=tenantId%>"  >
</iframe>
<br>
<input onclick="sub()" type="button" value="提交">
<script>
    function sub() {
//        var doc ;
        &lt;%&ndash;$("#frame1").contents().find("<%=formID%>")&ndash;%&gt;
        &lt;%&ndash;$("#frame1").contents().find("#BaoXiaoLiuCheng30").submit();&ndash;%&gt;
//        doc= document.getElementById("frame1").contentDocument;
//        doc.getElementById("BaoXiaoLiuCheng30").action("http://localhost:9087/UFP_DRIVER/workFlowController.do?method=getTable");
//        doc.getElementById("BaoXiaoLiuCheng30").submit();
    }
</script>--%>




</body>
</html>
