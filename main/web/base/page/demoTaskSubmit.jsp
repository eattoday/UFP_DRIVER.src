<%@ page import="com.metarnet.core.common.utils.Constants" %>
<%@ page import="com.metarnet.core.common.workflow.TaskInstance" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/base/_css/bootstrap.css"/>
<head>
    <title >任务详情</title>
</head>
<body >

<%
    String srcList =(String) request.getAttribute("srcList");
    String hisActivity =(String) request.getAttribute("hisActivity");
    String processInstID =(String) request.getAttribute("processInstID");
    List<String> list=JSONObject.parseObject(srcList, List.class);
    List<String> hisActivityList=JSONObject.parseObject(hisActivity, List.class);
    if (list != null) {
%>
<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="#">任务详情</a>
        <ul class="nav nav-tabs">
            <li id="first" ><a onclick="button0()" style="cursor: pointer">工单详情</a></li>
            <li id="history" ><a  onclick="his()" style="cursor: pointer">历史工单</a></li>
            <li id="now" ><a onclick="button<%=list.size()-1%>()" style="cursor: pointer">当前环节</a></li>
            <li id="monitor" ><a onclick="monitor()" style="cursor: pointer">流程监控</a></li>
        </ul>
    </div>
</div>
    <div id="his"  style="display: none;width: 100%">
<%
        if(list.size()==2||list.size()==1){
%>
            暂无历史信息
<%
        }else {
            for (int i=1;i<list.size()-1;i++) {
                String show="";
                show=hisActivityList.get(i-1);
%>
        <input type="button" value="<%=show%>" class="btn btn-mini"  style="text-align:left;width: 100%;line-height: 40px" onclick="button<%=i%>()">
        <br>

        <iframe  id="frame<%=i%>"  width="100%" scrolling="0"
                 frameborder="0"  marginheight="0" style="display: none;box-sizing: border-box"></iframe>

    <script>
        function button<%=i%>() {
            var iframe=document.getElementById("frame<%=i%>");
            iframe.setAttribute("src","<%=list.get(i)%>");
            iframe.setAttribute("height",500);

            if(iframe.style.display=="none"){
                iframe.style.display="table";
            }else {
                iframe.style.display="none";
            }
        }
    </script>

<%
            }
        }
    }
%>
    </div>

    <iframe  id="frame" width="100%" scrolling="0" frameborder="0"  marginheight="0"
             style="display: table;box-sizing: border-box" ></iframe>

<script>

    function return2Todo() {
        window.location.href="<%=path%>/base/page/todo.jsp";
    }
    function button0() {
        document.getElementById("now").setAttribute("class","");
        document.getElementById("history").setAttribute("class","");
        document.getElementById("first").setAttribute("class","active");
        document.getElementById("monitor").setAttribute("class","");

        document.getElementById("his").style.display="none";
        var iframe=document.getElementById("frame");
        iframe.style.display="table";
        iframe.setAttribute("src","<%=list.get(0)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight-64);
    }

    function his() {
        document.getElementById("now").setAttribute("class","");
        document.getElementById("history").setAttribute("class","active");
        document.getElementById("first").setAttribute("class","");
        document.getElementById("monitor").setAttribute("class","");

        document.getElementById("his").style.display="table";
        document.getElementById("frame").style.display="none";
    }
    function button<%=list.size()-1%>() {
        document.getElementById("now").setAttribute("class","active");
        document.getElementById("history").setAttribute("class","");
        document.getElementById("first").setAttribute("class","");
        document.getElementById("monitor").setAttribute("class","");

        document.getElementById("his").style.display="none";
        var iframe=document.getElementById("frame");
        iframe.style.display="table";
        iframe.setAttribute("src","<%=list.get(list.size()-1)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight-64);
    }
    function monitor() {
        document.getElementById("now").setAttribute("class","");
        document.getElementById("history").setAttribute("class","");
        document.getElementById("first").setAttribute("class","");
        document.getElementById("monitor").setAttribute("class","active");

        document.getElementById("his").style.display="none";
        var iframe=document.getElementById("frame");
        iframe.style.display="table";
        iframe.setAttribute("src","<%=path%>/workFlowController.do?method=getGeneralInfoPage&processInstID=<%=processInstID%>");
//        iframe.setAttribute("src","base/page/workOrderStart.jsp");
        iframe.setAttribute("height",document.documentElement.clientHeight-64);
    }

    function onload() {
        document.getElementById("now").setAttribute("class","active");
        var iframe=document.getElementById("frame");
        iframe.setAttribute("src","<%=list.get(list.size()-1)%>");
        iframe.height = document.documentElement.clientHeight-64;
    }

    window.onload = onload();
</script>

</body>
</html>
