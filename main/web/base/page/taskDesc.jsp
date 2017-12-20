<%@ page import="com.metarnet.core.common.utils.Constants" %>
<%@ page import="com.metarnet.core.common.workflow.TaskInstance" %>
<%@ page import="com.metarnet.core.common.model.GeneralInfoModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String srcList =(String) request.getAttribute("srcList");
    String accountId =(String) request.getAttribute("accountId");
    String hisActivity =(String) request.getAttribute("hisActivity");
    String processInstID =(String) request.getAttribute("processInstID");
    List<String> list=JSONObject.parseObject(srcList, List.class);
    List<String> hisActivityList=JSONObject.parseObject(hisActivity, List.class);

    String generaInfoListString=(String)request.getAttribute("generaInfoList");
    List<GeneralInfoModel> generaInfoList=JSONObject.parseArray(generaInfoListString, GeneralInfoModel.class);

%>
<link rel="stylesheet" type="text/css" href="<%=path%>/base/_css/bootstrap.css"/>
<head>
    <title >任务详情</title>
    <script type="text/javascript">

    </script>
</head>
<body >
<iframe  id="frame_first" width="100%" scrolling="0" frameborder="0"  marginheight="0"
style="display: table;box-sizing: border-box"
src="<%=list.get(0)%>">
</iframe>

<div style="height:45px;"></div>

<div style="width: 99% ; margin: auto"   >
<table class="table table-hover table-striped" style="margin-top: 5px;">
    <tbody id="monitor">
    <tr>
        <th style="width: 10%;">处理人部门</th>
        <th style="width: 15%;">处理人</th>
        <th style="width: 15%;">处理时间</th>
        <th style="width: 15%;">处理环节</th>
        <th style="width: 15%;">创建时间</th>
        <th style="width: 15%;">处理时长</th>
    </tr>

        <%
            for (int i=0;i<generaInfoList.size();i++){
                GeneralInfoModel item=generaInfoList.get(i);
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>

    <tr>
        <td style="width: 10%;"><%=item.getOperOrgName()%></td>
        <td style="width: 15%;"><%=item.getOperUserTrueName()%></td>
        <td style="width: 15%;">
            <%
                String operTime = sdf.format(item.getOperTime());
            %>
            <%=operTime%>
        </td>

        <td style="width: 15%;">
            <a href="<%=list.get(i)%>" target="_blank">
                <%=item.getActivityInstName()%>
            </a>
        </td>


        <td style="width: 15%;">
            <%
                String creationTime = sdf.format(item.getCreationTime());
            %>
            <%=creationTime%>
        </td>
        <td style="width: 15%;">
            <%
                Long time=(item.getOperTime().getTime()-item.getCreationTime().getTime())/1000;
                long second=time%60;
                long minute=time/60%60;
                long hour=time/60/60%24;
                long day=time/60/60/24;
                String shijian=day+"天"+hour+"时"+minute+"分"+second+"秒";
            %>
            <%=shijian%>
        </td>
    </tr>

    <%
        }
    %>





    </tbody>
</table>
</div>

<div style="height:45px;"></div>


<div style="width: 100% ; margin: auto;text-align: center"   >
<span class="btn " onclick="submit()">
    提交表单
</span>
<span class="btn " onclick="return2Todo()">
    返回列表
</span>
<span class="btn " onclick="diagram()">
    查看流程图
</span>
</div>

<div style="height:45px;"></div>

<script>
    function submit() {
        window.open("<%=list.get(list.size()-1)%>");
    }
    function diagram() {
        <%--window.open("workFlowController.do?method=getGeneralInfoDiagram&processInstID=<%=processInstID%>");--%>
        window.open("http://10.225.222.201:8090/activiti-rest/runtime/process-instances/<%=processInstID%>/diagram");
    }
    function return2Todo() {
        window.location.href="workFlowController.do?method=getWaitingList&accountId=<%=accountId%>";
        <%--window.location.href="http://http://10.225.222.203:8083/UFP_DRIVER/workFlowController.do?method=getWaitingList&accountId=<%=accountId%>";--%>
    }
    function onload() {
        document.getElementById("frame_first").setAttribute("height",document.documentElement.clientHeight-45);
    }
    window.onload = onload();
</script>

</body>
</html>
