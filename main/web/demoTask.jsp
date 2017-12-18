<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <title>测试子页面</title>
    <script type="text/javascript">
        function childSay(){
            alert("child.html");
        }
        function callParent(){
            parent.say();
            parent.window.document.getElementById("button").value="调用结束";
        }
    </script>
</head>
11111111
<body>
<input id="button" type="button" value="调用parent.html中的say()函数" onclick="callParent()"/>
22222222222

</body>
</html>
