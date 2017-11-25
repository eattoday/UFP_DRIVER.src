<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<html>
<head>
    <title>发起工单</title>
<style>
    #mainContainer { width:100%;}

    /*.processContainer { color:#fff!important;font-weight:bold;font-size:13px;padding-top:35px;width:200px;height:100px;border-radius:10px;margin:10px;box-shadow: #888 0 0 5px}*/
    /*.processContainer:hover { color:#fff!important; }*/
    .processContainer { color:#fff!important;transition: all 500ms ease 0s;font-size:15px;font-weight:bold;float: left;width:260px;height:150px;margin:10px;}
    .processContainer:hover , .glyphicon:hover { color:#fff!important;box-shadow: #888 0 0 10px}
    .processContainer .menu-icon { font-size:50px;margin-top:20px;margin-bottom: 10px;}

</style>
</head>
<body>
<div class="big-title-metar">发起工单</div>
<div id="mainContainer" class="container"></div>

</body>
<script>
    __show_metar_loading();
    $(document).ready(function(){
        var url='<%=path%>/publishhWorkOrderController.do?method=getList';
        $.ajax({
            url:url,
            type:"GET",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            success: function(json){
                $.each(json.datas,function(n,value) {
                    var name = value[0];
                    var customFormID = value[1];
                    var processModelName = value[3];
                    var tenantId = value[4];
                    var id = value[5];

                    <%--var processContainer = $('<a href="<%=path%>/base/page/launch.jsp?formId='+customFormID+'&tenantId='+tenantId+'&processModelName='+processModelName+'" class="btn btn-primary processContainer"><div><span class="glyphicon glyphicon-edit menu-icon"></span></div><div class="processName">'+name+'</div></a>');--%>
                    var processContainer = $('<a href="<%=path%>/toLaunch.do?id='+id+'" class="btn btn-primary processContainer"><div><span class="glyphicon glyphicon-edit menu-icon"></span></div><div class="processName">'+name+'</div></a>');

                    $('#mainContainer').append(processContainer);

                });

                __hide_metar_loading();
            }
        })
    })
</script>

</html>
