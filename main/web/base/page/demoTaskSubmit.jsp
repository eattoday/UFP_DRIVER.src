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


    <%--<meta http-equiv="content-type" content="application/vnd.ms-excel;charset=UTF-8"/>--%>
</head>
<body >
<%--<input type="button" class="btn btn-large "  value="任务详情" >--%>
<%--<br>--%>


<%
    String srcList =(String) request.getAttribute("srcList");
    List list=JSONObject.parseObject(srcList, List.class);
    if (list != null) {
        for (int i=0;i<list.size()-1;i++) {
            String show="";
            if(i==0){
                show="工单详情";
            }else {
                show="历史工单-"+i;
            }
%>

    <input type="button" class="btn btn-large btn-info" style="text-align: left" value="<%=show%>" onclick="button<%=i%>()">


    <%--<iframe  id="frame<%=i%>" width="100%" scrolling="auto" frameborder="1"  marginheight="5px"--%>
            <%--src=""  style="display: none">--%>
    <%--</iframe>--%>

<script>
    function button<%=i%>() {
        <%--alert("<%=i%><%=i%><%=i%><%=i%>");--%>
        <%--var iframe=document.getElementById("frame<%=i%>");--%>
        var iframe=document.getElementById("frame");
        iframe.setAttribute("src","<%=list.get(i)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight);
//        if(iframe.getAttribute("style")=="display: none"){
//            iframe.setAttribute("style","display: table");
//        }else {
//            iframe.setAttribute("style","display: none");
//        }
    }

</script>

<%
        }%>

    <input type="button" value="当前环节" class="btn btn-large " style="text-align: left" onclick="button<%=list.size()-1%>()">

    <%--<iframe  id="frame<%=list.size()-1%>" width="100%" scrolling="0" frameborder="1"  marginheight="5px"--%>
             <%--src="<%=list.get(list.size()-1)%>"  >--%>
    <%--</iframe>--%>
    <script>



        function button<%=list.size()-1%>() {
            var iframe=document.getElementById("frame");
            iframe.setAttribute("src","<%=list.get(list.size()-1)%>");
            iframe.setAttribute("height",document.documentElement.clientHeight);
//            if(iframe.getAttribute("style")=="display: none"){
//                iframe.setAttribute("style","display: table");
//            }else {
//                iframe.setAttribute("style","display: none");
//            }
        }

    </script>

<%
    }
%>

<iframe  id="frame" width="100%" scrolling="0" frameborder="0"  marginheight="0" style="display: table">
</iframe>


<script>
    function onload() {
        <%--var iframe=document.getElementById("frame<%=list.size()-1%>");--%>
        var iframe=document.getElementById("frame");
        iframe.setAttribute("src","<%=list.get(list.size()-1)%>");
        iframe.setAttribute("height",document.documentElement.clientHeight);
    }
    window.onload = onload();
</script>

</body>
</html>
