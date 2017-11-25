<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/component/Font-Awesome-3.2.1/css/font-awesome.min.css"/>

<head>
    <title>表单管理选择</title>
    <style>
        .form-select { position:absolute;width:50%;height:100%;border:none;font-size: 13px;}
        .glyphicon { font-size: 100px; }
        .form-container { position:absolute;top:50%;left:50%;margin-top:-50px;margin-left:-50px; }
    </style>
</head>
<body>
<a href="/cform/jsp/cform/form/queryform.jsp?tenantId=default" class="form-select btn btn-default"><div class="form-container"><i class="glyphicon glyphicon-list-alt"></i><div>云表单定制</div></div></a>
<a href="cusDevFormList.jsp" class="form-select btn btn-default" style="left:50%;"><div class="form-container"><i class="glyphicon glyphicon-wrench"></i><div>自行开发表单注册</div></div></a>
</body>

</html>
