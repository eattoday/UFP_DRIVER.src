<%@ page import="com.metarnet.core.common.utils.Constants" %>
<%@ page import="com.metarnet.core.common.workflow.TaskInstance" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <title >任务详情</title>
</head>
<body >

<%
    String srcList =(String) request.getAttribute("srcList");
    List list=JSONObject.parseObject(srcList, List.class);
    if (list != null) {
%>
<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="#">任务详情</a>
        <ul class="nav nav-tabs">
            <li><a onclick="button0()">工单详情</a></li>
            <li><a  onclick="his()">历史工单</a></li>
            <li><a onclick="button<%=list.size()-1%>()">当前环节</a></li>
        </ul>
    </div>
</div>
    <div id="his"  style="display: none;width: 100%">
<%
        for (int i=1;i<list.size()-1;i++) {
            String show="";
            show="历史工单-"+i;
%>
        <input type="button" value="<%=show%>" class="btn btn-mini"  style="text-align:left;width: 100%" onclick="button<%=i%>()">
        <br>
        <iframe  id="frame<%=i%>"  width="100%" scrolling="0"
                 frameborder="0"  marginheight="0" style="display: none"></iframe>
    <script>
        function button<%=i%>() {
            var iframe=document.getElementById("frame<%=i%>");
            iframe.setAttribute("src","<%=list.get(i)%>");
            iframe.setAttribute("height",document.documentElement.clientHeight);

            if(iframe.getAttribute("style")=="display: none"){
                iframe.setAttribute("style","display: table");
            }else {
                iframe.setAttribute("style","display: none");
            }
        }
    </script>

<%  }}   %>

    </div>



<iframe  id="frame" width="100%" scrolling="0" frameborder="0"  marginheight="0" style="display: table"></iframe>


<script>
    function button0() {
        document.getElementById("his").setAttribute("style","display: none");
        var iframe=document.getElementById("frame");
        iframe.setAttribute("style","display: table");
        iframe.setAttribute("src","<%=list.get(0)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight);
    }

    function his() {
        document.getElementById("his").setAttribute("style","display: table;width: 100%");
        document.getElementById("frame").setAttribute("style","display: none");
    }
    function button<%=list.size()-1%>() {
        document.getElementById("his").setAttribute("style","display: none");
        var iframe=document.getElementById("frame");
        iframe.setAttribute("style","display: table");
        iframe.setAttribute("src","<%=list.get(list.size()-1)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight);
    }

    function onload() {
        var iframe=document.getElementById("frame");
        iframe.setAttribute("src","<%=list.get(list.size()-1)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight);
    }
    window.onload = onload();
</script>

</body>
</html>
