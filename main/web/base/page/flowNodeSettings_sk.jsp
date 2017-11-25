<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:include page="../basePage.jsp"/>
<%
    String processModelName = request.getParameter("processModelName");
    String activityDefID = request.getParameter("activityDefID");
    String settingID = request.getParameter("settingID");
    String tenantId = request.getParameter("tenantId");
    if("null".equals(tenantId)){
        tenantId = "default";
    }
%>
<html>
<head>
<title>流程节点设置</title>
<style type="text/css">
    .item { margin-bottom: 15px;}
</style>
</head>
<body>
<input type="hidden" id="tenantId" value="<%=tenantId%>"/>
<div class="container-fluid">
    <div>
        <div class="item" style="padding-bottom: 5px;">
            <h2>表单设置</h2>
        </div>
    </div>

    <div class="item" style="float:left;padding-bottom: 15px;border-bottom:2px dashed #ccc;width:50%">
        <b>流程名称：</b><%=processModelName%>
    </div>
    <div class="item" style="float:left;padding-bottom: 15px;border-bottom:2px dashed #ccc;width:50%">
        <b>环节定义ID：</b><%=activityDefID%>
    </div>

    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="2" type="radio"><b>自定义URL：</b></label>
        <div style="padding-left: 18px;"><textarea id="customURL" class="form-control" style="height:50px;resize: none;">${setting.customURL}</textarea></div>
        <label><input id="extand" type="checkbox" style="margin-left:18px;margin-right:5px;"/>是否增加扩展功能</label>
    </div>
    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="1" type="radio"><b>自定义表单：</b></label>
        <div class="input-group" style="padding-left: 18px;">
            <input id="customFormID" type="hidden" value="${setting.formID}" />
            <input id="customFormName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.formName}"/>
            <%--<div id="customFormName" style="border:1px solid #ccc;height:28px;line-height: 24px;padding-left: 5px;">${setting.formName}</div>--%>
            <span id="select_custom_form" onclick="selectForm()" class="input-group-addon glyphicon glyphicon-th"></span>
        </div>
    </div>
    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="3" type="radio"><b>技术手段：</b></label>
        <div style="padding-left: 18px;">
            <div class="input-group">
                <span class="input-group-addon"><label style="width:70px;">areaName</label></span><input id="areaName" class="form-control" value="${setting.areaName}"/>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><label style="width:70px;">component</label></span><input id="component" class="form-control" value="${setting.component}"/>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><label style="width:70px;">editLinks</label></span><input id="editLinks" class="form-control" value="${setting.editLinks}"/>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><label style="width:70px;">showLinks</label></span><input id="showLinks" class="form-control" value="${setting.showLinks}"/>
            </div>
        </div>
    </div>
    <div class="form-group" style="text-align:center">
        <button class="btn btn-default" onclick="saveSettings()">保存</button>
    </div>
</div>
</body>
<script type="text/javascript">

    $(document).ready(function(){
        var formType = '${setting.formType}';
        if(formType == ''){
            formType = 2;
        }
        $("input[value='"+formType+"']").attr('checked','true');

        var extand = '${setting.extand}';
        if(extand == 'true'){
            $("#extand").attr('checked',true);
        } else {
            $("#extand").attr('checked',false);
        }

    });

    function selectForm(){
        var tenantId = $('#tenantId').val();
        var formReturn = window.showModalDialog(_PATH + "/base/page/cformList_sk.jsp?tenantId=" + tenantId);
//        alert(JsonObjectToString(formReturn));
//        alert(formReturn.formId);
//        alert(formReturn.formName);
        $('#customFormID').val(formReturn.formId);
        $('#customFormName').val(formReturn.formName);
    }


    function saveSettings(){

        var customFormID = $('#customFormID').val();
        var customFormName = $('#customFormName').val();
        var customURL = $('#customURL').val();
        var tenantId = $('#tenantId').val();

//        获取选中radio的三种方法
//        $('input:radio:checked').val();
//        $("input[type='radio']:checked").val();
//        $("input[name='rd']:checked").val();

        var formType = $("input[name='formType']:checked").val();

        if(formType == 1 && customFormID == ''){
            alert('请选择自定义表单');
            return;
        } else if(formType == 2 && customURL == '') {
            $('#customURL').focus();
            alert('请输入自定义URL');
            return;
        }

        var extand = $("#extand").attr('checked') == 'checked' ? true : false;

        var areaName = $('#areaName').val();
        var component = $('#component').val();
        var editLinks = $('#editLinks').val();
        var showLinks = $('#showLinks').val();

        $.ajax({
            url: _PATH + '/flowNodeSettingController.do?method=save',
            data: {
                settingID : '<%=settingID%>' ,
                processModelName : '<%=processModelName%>' ,
                activityDefID : '<%=activityDefID%>' ,
                extand : extand ,
                formType : formType,
                formID : customFormID,
                formName : customFormName,
                customURL : customURL,
                tenantId : tenantId,
                areaName : areaName,
                component : component,
                editLinks : editLinks,
                showLinks : showLinks
            },
            type: 'POST',
            async: true,
            dataType: 'json',
            success: function (response) {
                if(response.success && response.settingID){
                    alert('保存成功');
                    window.returnValue = response.settingID;
//                    var pWindow = window.dialogArguments;
//                    if(pWindow != null){
//                        pWindow.setActivityFormID(response.activityDefID , response.settingID);
//                    }else{
//                        window.opener.setActivityFormID(response.activityDefID , response.settingID);
//                    }
                    window.close();
                } else {
                    alert('保存失败，请重试');
                }

            } ,
            error: function(){
                alert('保存失败，请重试');
            }
        })
    }
</script>
</html>
