<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/base/basePage.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>测试父窗体</title>
    <script language="javascript" type="text/javascript">

        function OpenWindow(){
            window.open('<%=path%>/base/page/cloudFormList.jsp?tenantId=t1&R='+Math.random());
        }

        function setValue(m_strValue){
            document.getElementById("txt_Value").value = m_strValue;
        }

    </script>
</head>
<body>

<input type="text" name="txt_Value" id="txt_Value" class="container">
<input type="button"  value="选择表单" onclick="OpenWindow();" />



</body>

</html>
