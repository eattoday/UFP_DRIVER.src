<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%--<jsp:include page="../basePage.jsp"/>--%>
<%
    String tenantId = request.getParameter("tenantId");
    if(tenantId == null || "".equals(tenantId)){
        tenantId = "default";
    }
%>
<html>
<head>
<style type="text/css">
    .item { margin-bottom: 15px;}
</style>
</head>
<body>

<div class="container-fluid">
    <div class="input-group col-sm-12 item">
        <label class="">发起工单名称：</label>
        <div class=""><input id="name" class="form-control"/></div>
        <div class="clearfix"></div>
    </div>
    <div class="input-group col-sm-12 item">
        <label class="">自定义表单：</label>
        <div class="input-group col-sm-12">
            <input id="formType" type="hidden" />
            <input id="customFormID" type="hidden" />
            <%--<input id="cloudFormName" readonly="readonly" type="text" class="form-control" value="${setting.formName}"/>--%>
            <input id="customFormName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc"/>
            <span onclick="selectCloudForm()" class="input-group-addon glyphicon glyphicon-list-alt" data-toggle="tooltip" data-placement="top" title="选择云表单"></span>
            <span onclick="selectCusDevForm()" class="input-group-addon glyphicon glyphicon-wrench" data-toggle="tooltip" data-placement="top" title="选择自行开发表单"></span>
        </div>
    </div>
    <div class="input-group col-sm-12 item">
        <label class="">流程定义名称：</label>
        <div class="input-group col-sm-12">
            <input id="processModelName" type="hidden" />
            <input id="processChName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc"/>
            <span onclick="selectProcess()" class="input-group-addon glyphicon glyphicon-th" data-toggle="tooltip" data-placement="top" title="选择流程"></span>
        </div>
        <div class="clearfix"></div>
    </div>
    <div class="input-group col-sm-12 item">
        <label class="">按钮：</label>
        <div class="input-group col-sm-12">
            <input id="btnIDs" type="hidden" />
            <%--<input id="cloudFormName" readonly="readonly" type="text" class="form-control" value="${setting.formName}"/>--%>
            <input id="btnNames" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.btnNames}"/>
            <span onclick="clearBtns()" class="input-group-addon glyphicon glyphicon-remove"></span>
            <span onclick="selectButton()" class="input-group-addon glyphicon glyphicon-unchecked" data-toggle="tooltip" data-placement="top" title="选择按钮"></span>
        </div>
    </div>
    <div class="input-group col-sm-12 item" style="display: none">
        <label class="">租户ID：</label>
        <div class=""><input id="tenantId" class="form-control" value="<%=tenantId%>"/></div>
        <div class="clearfix"></div>
    </div>
    <div class="form-group" style="text-align:center">
        <button class="btn btn-default" onclick="saveSettings()">发布</button>
    </div>
</div>

</body>
<script type="text/javascript">

    $(document).ready(function(){

        $('.input-group-addon').tooltip();

        <%--$.ajax({--%>
            <%--url: '/pmos_trial/processController.do?method=queryProcessList&tenantId=<%=tenantId%>',--%>
            <%--type: 'POST',--%>
            <%--async: true,--%>
            <%--dataType: 'json',--%>
            <%--data : { dtGridPager : "{pageSize : 1000 , startRecord : 0 , fastQueryParameters : {}}" , globalUniqueID : '${globalUniqueID}'},--%>
            <%--success: function (response) {--%>
                <%--if (response.exhibitDatas) {--%>

                    <%--var processList = response.exhibitDatas;--%>

                    <%--for(var i = 0 ; i < processList.length ; i++){--%>

                        <%--var process = processList[i];--%>
                        <%--var option = $('<option value="' + process.processCode + '">' + process.processCode + '</option>');--%>
                        <%--$('#processModelName').append(option);--%>
                    <%--}--%>

                <%--} else {--%>
                    <%--alert('获取流程定义列表失败');--%>
                <%--}--%>
            <%--},--%>
            <%--error: function (XMLHttpRequest, textStatus, errorThrown) {--%>
                <%--alert('获取流程定义列表失败');--%>
            <%--}--%>
        <%--})--%>

    });


    function saveSettings(){
        var name = $('#name').val();
        name = trim(name);
        if(name == ''){
            __show_bootstrap_msg('' , '请输入发起工单名称');
            $('#name').focus().val(name);
            return;
        }
        var customFormID = $('#customFormID').val();
        var customFormName = $('#customFormName').val();
        if(customFormName == ''){
            __show_bootstrap_msg('' , '请选择表单');
            return;
        }
        var processModelName = $('#processModelName').val();
        var tenantId = $('#tenantId').val();
        var formType = $('#formType').val();

        var btnIDs = $('#btnIDs').val();
        var btnNames = $('#btnNames').val();

        var data = {};
        data.name = name;
        data.customFormID = customFormID;
        data.customFormName = customFormName;
        data.processModelName = processModelName;
        data.btnIDs = btnIDs;
        data.btnNames = btnNames;
        data.tenantId = tenantId;
        data.formType = formType;

        $.ajax({
            url: _PATH + '/publishhWorkOrderController.do?method=save',
            data: data,
            type: 'POST',
            async: true,
            dataType: 'json',
            success: function (response) {
                if(response.success){
                    alert('发布成功');
                    location.href = _PATH + '/base/page/workOrderListDev.jsp?tenantId=' + tenantId;
                } else {
                    alert('发布失败，请重试');
                }

            } ,
            error: function(){
                alert('发布失败，请重试');
            }
        })
    }

    function selectCloudForm(){
        <%--var formReturn = window.showModalDialog(_PATH + "/base/page/cloudFormList.jsp?tenantId=<%=tenantId%>");--%>
//        $('#customFormID').val(formReturn.formId);
//        $('#customFormName').text(formReturn.formName);
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/cloudFormList.jsp?tenantId=<%=tenantId%>');
    }

    function selectCusDevForm(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/cusDevFormList.jsp?forSelect=1&tenantId=<%=tenantId%>');
    }

    function selectButton(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/flowButtonList.jsp?forSelect=1&tenantId=<%=tenantId%>');
    }

    function selectProcess(){
        showIModal({
            title:'选择流程',
            type: 2,
            maxmin:false,
            area: ['500px' , '400px'],
            content: _PATH + '/framework/page/processActiTree.jsp?radioType=1',//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            btn:['选择' , '关闭'],
            btn1:function(index, layero){
                var selected = layero.find('iframe')[0].contentWindow.getSelected();
                if(selected == ''){
                    iMsg('请选择流程');
                } else {
                    var processDefCodes = '';
                    var processDefNames = '';
                    for(var i = 0 ; i < selected.length ; i++){
                        if(selected[i].type != 1){
                            continue;
                        }
                        processDefCodes += selected[i].code + ',';
                        processDefNames += selected[i].label + ',';
                    }

                    if(processDefCodes != ''){
                        processDefCodes = processDefCodes.substring(0 , processDefCodes.length - 1);
                        processDefNames = processDefNames.substring(0 , processDefNames.length - 1);
                    }

                    $('#processModelName').val(processDefCodes);
                    $('#processChName').val(processDefNames);

                    layer.close(index);
                }

                return false;
            }
        })
    }

    function clearBtns(){
        $('#btnIDs').val('');
        $('#btnNames').val('');
    }
</script>
</html>
