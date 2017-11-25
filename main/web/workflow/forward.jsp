<%
    String url = request.getParameter("url");
    String globalUniqueID = request.getParameter("globalUniqueID");

    String BPS_IP = "http://10.249.6.31:8180";

    response.sendRedirect(BPS_IP + "/workspace/frame/permission/oss2_login.jsp?requestTag=0&tenantId=&userId=sysadmin&globalUniqueID=" + globalUniqueID + "&mode=&newUrl=" + url);
%>