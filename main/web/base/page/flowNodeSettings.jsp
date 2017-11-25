<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:include page="../basePage.jsp"/>
<%
    String processModelName = request.getParameter("processModelName");
    String saveBtn = request.getParameter("saveBtn");
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
    <div class="item" style="float:left;padding: 15px 0;border-bottom:2px dashed #ccc;width:50%;font-size: 20px;">
        <b>流程名称：</b><%=processModelName%>
    </div>
    <div class="item" style="float:left;padding: 15px 0;border-bottom:2px dashed #ccc;width:50%;font-size: 20px;">
        <b>环节定义ID：</b><%=activityDefID%>
    </div>

    <div class="item" style="font-size: 20px;font-weight: bold">
        域名称
    </div>
    <div class="item"style="padding-left: 18px;">
        <input id="areaName" type="text" class="form-control" style="border:1px solid #ccc" value="${setting.areaName}"/>
    </div>

    <div class="item" style="font-size: 20px;font-weight: bold">
        表单设置
    </div>
    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="2" type="radio"><b>自定义URL：</b></label>
        <div style="padding-left: 18px;"><textarea id="customURL" class="form-control" style="height:50px;resize: none;">${setting.customURL}</textarea></div>
        <label><input id="extand" type="checkbox" style="margin-left:18px;margin-right:5px;"/>是否增加扩展功能</label>
    </div>
    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="1" type="radio"><b>云表单：</b></label>
        <div class="input-group" style="padding-left: 18px;">
            <input id="cloudFormID" type="hidden" value="${setting.formID}" />
            <input id="cloudFormName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.formName}"/>
            <%--<div id="cloudFormName" style="border:1px solid #ccc;height:28px;line-height: 24px;padding-left: 5px;">${setting.formName}</div>--%>
            <span onclick="clearCloudForm()" class="input-group-addon glyphicon glyphicon-remove"></span>
            <span onclick="selectCloudForm()" class="input-group-addon glyphicon glyphicon-th"></span>
        </div>
    </div>
    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="4" type="radio"><b>自行开发表单：</b></label>
        <div class="input-group" style="padding-left: 18px;">
            <input id="cusDevFromID" type="hidden" value="${setting.componentID}" />
            <input id="cusDevFromName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.componentName}"/>
            <%--<div id="cloudFormName" style="border:1px solid #ccc;height:28px;line-height: 24px;padding-left: 5px;">${setting.formName}</div>--%>
            <span onclick="clearCusDevForm()" class="input-group-addon glyphicon glyphicon-remove"></span>
            <span onclick="selectDevFromForm()" class="input-group-addon glyphicon glyphicon-th"></span>
        </div>
    </div>
    <div class="item">
        <label><input style="margin-right:5px;" name="formType" value="3" type="radio"><b>技术手段：</b></label>
        <div style="padding-left: 18px;">
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
    <div class="item" style="font-size: 20px;font-weight: bold">
        按钮设置
    </div>
    <div class="item">
        <%--<label><input style="margin-right:5px;" name="formType" value="4" type="radio"><b>按钮：</b></label>--%>
        <div class="input-group" style="padding-left: 18px;">
            <input id="btnIDs" type="hidden" value="${setting.btnIDs}" />
            <input id="btnNames" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.btnNames}"/>
            <span onclick="clearBtns()" class="input-group-addon glyphicon glyphicon-remove"></span>
            <span onclick="selectFlowButtons()" class="input-group-addon glyphicon glyphicon-th"></span>
        </div>
    </div>
    <%if(!"0".equals(saveBtn)){
        %>
    <div class="form-group" style="text-align:center">
        <button class="btn btn-default" onclick="saveSettings()">保存</button>
    </div><%
    }%>

</div>
<div class="modal fade" id="selectFormWin" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width:90%;left:5%;position:absolute">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="selectFormWinTitle">请选择表单</h4>
            </div>
            <div class="modal-body" id="selectFormWinBody"></div>
        </div>
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

    function selectCloudForm(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/cloudFormList.jsp?tenantId=<%=tenantId%>');
//        var formReturn = window.showModalDialog(_PATH + "/base/page/cloudFormList.jsp?tenantId=" + tenantId);
//        $('#cloudFormID').val(formReturn.formId);
//        $('#cloudFormName').val(formReturn.formName);
    }

    function selectDevFromForm(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/cusDevFormList.jsp?forSelect=1&tenantId=<%=tenantId%>');
    }

    function selectFlowButtons(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/flowButtonList.jsp?forSelect=1&tenantId=<%=tenantId%>');
    }

    function closeSelectFormWin(){
        $('#selectFormWin').modal('hide');
    }


    function saveSettings(){

        var operSuccess = false;

        var btnIDs = $('#btnIDs').val();
        var cloudFormID = $('#cloudFormID').val();
        var cloudFormName = $('#cloudFormName').val();
        var customURL = $('#customURL').val();
        var tenantId = $('#tenantId').val();
        var cusDevFromID = $('#cusDevFromID').val();
        var cusDevFromName = $('#cusDevFromName').val();

//        获取选中radio的三种方法
//        $('input:radio:checked').val();
//        $("input[type='radio']:checked").val();
//        $("input[name='rd']:checked").val();
        var formType = $("input[name='formType']:checked").val();

        if(btnIDs == ''){
            if(formType == 2 && customURL == '') {
                $('#customURL').focus();
                alert('请输入自定义URL');
                return;
            } else if(formType == 1 && cloudFormID == ''){
                alert('请选择自定义表单');
                return;
            } else if(formType == 4 && cusDevFromID == '') {
                alert('请选择自行开发表单');
                return;
            }
        }


        var extand = $("#extand").attr('checked') == 'checked' ? true : false;

        var areaName = $('#areaName').val();
        var component = $('#component').val();
        var editLinks = $('#editLinks').val();
        var showLinks = $('#showLinks').val();

        var btnIDs = $('#btnIDs').val();
        var btnNames = $('#btnNames').val();

        $.ajax({
            url: _PATH + '/flowNodeSettingController.do?method=save',
            data: {
                settingID : '<%=settingID%>' ,
                processModelName : '<%=processModelName%>' ,
                activityDefID : '<%=activityDefID%>' ,
                extand : extand ,
                formType : formType,
                formID : cloudFormID,
                formName : cloudFormName,
                componentID : cusDevFromID,
                componentName : cusDevFromName,
                customURL : customURL,
                tenantId : tenantId,
                areaName : areaName,
                component : component,
                editLinks : editLinks,
                showLinks : showLinks,
                btnIDs : btnIDs,
                btnNames : btnNames
            },
            type: 'POST',
            async: false,
            dataType: 'json',
            success: function (response) {
                if(response.success && response.settingID){
                    alert('保存成功');
                    operSuccess = true;
                    window.returnValue = response.settingID;
                    window.close();

                } else {
                    alert('保存失败，请重试');
                }

            } ,
            error: function(){
                alert('保存失败，请重试');
            }
        })

        return operSuccess;
    }

    function clearCloudForm(){
        clearValues(['cloudFormID' , 'cloudFormName']);
    }
    function clearCusDevForm(){
        clearValues(['cusDevFromID' , 'cusDevFromName']);
    }
    function clearBtns(){
        clearValues(['btnIDs' , 'btnNames']);
    }

    function clearValues(items){
        for(var i = 0 ; i < items.length ; i++){
            $('#' + items[i]).val('');
        }
    }
</script>
</html>
