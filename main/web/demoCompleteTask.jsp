<%@ page import="com.metarnet.core.common.utils.Constants" %>
<%@ page import="com.metarnet.core.common.workflow.TaskInstance" %>
<%@ page import="java.util.List" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <title>测试父页面</title>
    <script type="text/javascript">
        function say(){
            alert("parent.html");
        }
        function callChild(){
            myFrame.window.childSay();
            myFrame.window.document.getElementById("button").value="调用结束";
        }
    </script>
</head>
<body>
<input id="button" type="button" value="调用child.html中的函数say()" onclick="callChild()"/>
<iframe name="myFrame" src="demoTask.jsp"></iframe>

</body>
</html>
