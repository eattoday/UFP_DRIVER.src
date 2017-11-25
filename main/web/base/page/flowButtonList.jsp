<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>
<%
//    String path = request.getContextPath();
    String tenantId = request.getParameter("tenantId");
    if(tenantId == null || "".equals(tenantId)){
        tenantId = "default";
    }
    String forSelect = request.getParameter("forSelect");
    if(!"1".equals(forSelect)){
        %>
<%--<%@ include file="/base/basePage.jsp" %>--%>
<%
    }
%>

<html>
<head>
    <title>自定义按钮</title>
    <style>
        .glyphicon-trash , .glyphicon-edit { padding-left: 0 !important; }
    </style>
</head>
<body>
<div id="big-title-metar-flowButton" class="big-title-metar">自定义按钮注册</div>
<div id="dtGridContainer_flowButton" class="dt-grid-container" style="height:400px"></div>
<div id="dtGridToolBarContainer_flowButton" class="dt-grid-toolbar-container"></div>
<%if(!"1".equals(forSelect)){
%>
<div class="modal fade" id="addComponentFormWin" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><i class="fa fa-plus"></i>&nbsp;&nbsp;自定义按钮注册</h4>
            </div>
            <div class="modal-body form-horizontal">
                <input id="cusDevFormID" type="hidden"/>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">按钮名称</label></div><div class="col-sm-8"><input id="buttonName" class="form-control" /></div>
                </div>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">按钮类型</label></div>
                    <div class="col-sm-8">
                        <span class="radio">
                        <label style="padding-right:20px;">
                            <input name="buttonType" type="radio" value="1" checked >后台
                        </label>
                        <label style="padding-right:20px;">
                            <input name="buttonType" type="radio" value="2">弹出云表单
                        </label>
                        <label>
                            <input name="buttonType" type="radio" value="3">弹出自行开发表单
                        </label>
                        </span>
                    </div>
                </div>
                <div id="buttonType1" class="buttonType">
                    <div class="form-group">
                        <div class="col-sm-3 text-right"><label class="control-label">按钮触发URL</label></div><div class="col-sm-8"><input id="triggerURL" class="form-control" /></div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-3 text-right"><label class="control-label">保存表单前</label></div><div class="col-sm-8"><input id="beforeSaveURL" class="form-control" /></div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-3 text-right"><label class="control-label">保存表单后提交工作项前</label></div><div class="col-sm-8"><input id="afSaveBefSubmitURL" class="form-control" /></div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-3 text-right"><label class="control-label">提交工作项后</label></div><div class="col-sm-8"><input id="afterSubmitURL" class="form-control" /></div>
                    </div>
                </div>
                <div id="buttonType2" class="buttonType">
                    <div class="form-group">
                        <div class="col-sm-3 text-right"><label class="control-label">云表单</label></div>
                        <div class="col-sm-8">
                            <div class="input-group">
                                <input id="cloudFormID" type="hidden" value="${setting.cloudFormID}" />
                                <input id="cloudFormName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.cloudFormName}"/>
                                <span onclick="selectCloudForm()" class="input-group-addon glyphicon glyphicon-th"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="buttonType3" class="buttonType">
                    <div class="form-group">
                        <div class="col-sm-3 text-right"><label class="control-label">自行开发表单</label></div>
                        <div class="col-sm-8">
                            <div class="input-group">
                                <input id="cusDevFromID" type="hidden" value="${setting.cusDevFromID}" />
                                <input id="cusDevFromName" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc" value="${setting.cusDevFromName}"/>
                                <span onclick="selectCusDevForm()" class="input-group-addon glyphicon glyphicon-th"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" onclick="save()">注册</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
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

<script type="text/javascript">
    $(document).ready(function(){
        initForm();

        $('input[name="buttonType"]').click(function(){
            activeButtonTypeDiv(this.value);
        });
    });

    function initForm(){
        activeButtonTypeDiv(1);

        $('#cusDevFormID').val('');

        $('#buttonName').val('');
        $('#triggerURL').val('');
        $('#beforeSaveURL').val('');
        $('#afSaveBefSubmitURL').val('');
        $('#afterSubmitURL').val('');

        $('#cloudFormID').val('');
        $('#cloudFormName').val('');

        $('#cusDevFromID').val('');
        $('#cusDevFromName').val('');
    }

    function activeButtonTypeDiv(buttonType){
        $('.buttonType').hide();
        $('input[name="buttonType"]')[buttonType - 1].checked = true;
        $('#buttonType' + buttonType).fadeIn();
    }

    function toUpdate(formId){
        $('#addComponentFormWin').modal('show');
        $.ajax({
            url: _PATH + '/flowButtonFindById.do',
            type: 'POST',
            async: true,
            dataType: 'json',
            data: { id : formId },
            success: function (response) {
                if(response){

                    activeButtonTypeDiv(response.buttonType);

                    $('#cusDevFormID').val(response.id);
                    $('#buttonName').val(response.buttonName);
                    $('#triggerURL').val(response.triggerURL);
                    $('#beforeSaveURL').val(response.beforeSaveURL);
                    $('#afSaveBefSubmitURL').val(response.afSaveBefSubmitURL);
                    $('#afterSubmitURL').val(response.afterSubmitURL);
                    $('#cloudFormID').val(response.cloudFormID);
                    $('#cloudFormName').val(response.cloudFormName);
                    $('#cusDevFromID').val(response.cusDevFromID);
                    $('#cusDevFromName').val(response.cusDevFromName);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                __show_bootstrap_msg('' , '获取信息失败，请稍后重试');
            }
        })
    }

    function save(){

        var buttonName = $('#buttonName').val();
        buttonName = trim(buttonName);
        if(buttonName == ''){
            __show_bootstrap_msg('buttonName' , '【按钮名字】不能为空');
            $('#buttonName').focus().val(buttonName);
            return;
        }


        var buttonType = $("input[name='buttonType']:checked").val();

        var triggerURL = $('#triggerURL').val();
        triggerURL = trim(triggerURL);
        if(buttonType == 1 && triggerURL == ''){
            __show_bootstrap_msg('triggerURL' , '【按钮触发URL】不能为空');
            $('#triggerURL').focus().val(triggerURL);
            return;
        }

        var beforeSaveURL = $('#beforeSaveURL').val();
        beforeSaveURL = trim(beforeSaveURL);
        var afSaveBefSubmitURL = $('#afSaveBefSubmitURL').val();
        afSaveBefSubmitURL = trim(afSaveBefSubmitURL);
        var afterSubmitURL = $('#afterSubmitURL').val();
        afterSubmitURL = trim(afterSubmitURL);


        var cloudFormID = $('#cloudFormID').val();
        var cloudFormName = $('#cloudFormName').val();
        if(buttonType == 2 && cloudFormID == ''){
            __show_bootstrap_msg('cloudFormName' , '请选择【云表单】');
            $('#cloudFormName').focus();
            return;
        }

        var cusDevFromID = $('#cusDevFromID').val();
        var cusDevFromName = $('#cusDevFromName').val();
        if(buttonType == 3 && cusDevFromID == ''){
            __show_bootstrap_msg('cusDevFromName' , '请选择【自行开发表单】');
            $('#cusDevFromName').focus();
            return;
        }

        var cusDevFormID = $('#cusDevFormID').val();

        var data = {};
        data.buttonName = buttonName;
        data.triggerURL = triggerURL;
        data.beforeSaveURL = beforeSaveURL;
        data.afSaveBefSubmitURL = afSaveBefSubmitURL;
        data.afterSubmitURL = afterSubmitURL;
        data.buttonType = buttonType;
        data.cloudFormID = cloudFormID;
        data.cloudFormName = cloudFormName;
        data.cusDevFromID = cusDevFromID;
        data.cusDevFromName = cusDevFromName;
        data.id = cusDevFormID;
        data.tenantId = '<%=tenantId%>';

        $.ajax({
            url: _PATH + '/flowButtonSave.do',
            type: 'POST',
            async: true,
            dataType: 'json',
            data: data,
            success: function (response) {
                if(response.success){
                    __show_bootstrap_msg('' , '注册成功');
                    $('#addComponentFormWin').modal('hide');
                    grid_flowButton.load();

                    initForm();

                } else {
                    __show_bootstrap_msg('' , response.msg);
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                __show_bootstrap_msg('' , '注册失败，请稍后重试');
            }
        })
    }

    function del(formId){

        __show_bootstrap_confirm('确认删除吗?' , function(){
            $.ajax({
                url: _PATH + '/flowButtonDelete.do',
                type: 'POST',
                async: true,
                dataType: 'json',
                data: { id : formId },
                success: function (response) {
                    if(response.success){
                        __show_bootstrap_msg('' , '删除成功');
                        grid_flowButton.load();
                    } else {
                        __show_bootstrap_msg('' , response.msg);
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    __show_bootstrap_msg('' , '注册失败，请稍后重试');
                }
            })
        });
    }

    function selectCloudForm(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/cloudFormList.jsp?tenantId=<%=tenantId%>');
    }

    function selectCusDevForm(){
        $('#selectFormWin').modal('show');
        $('#selectFormWinBody').empty().load(_PATH + '/base/page/cusDevFormList.jsp?forSelect=1&<%=tenantId%>');
    }

</script>
<%
}%>

<script type="text/javascript">



    var forSelect = '<%=forSelect%>';
    var seletedValues = {};
    __show_metar_loading();
    //映射内容
    var dtGridColumns_flowButton = [
        {id : 'buttonName', title: '按钮名称' , headerStyle : 'text-align:left;' , resolution:function(value, record, column, grid, dataNo, columnNo){
            return '<span class="btn btn-danger">'+value+'</span>';
        }},
        {id : 'lastUpdateTime',title : '更新时间' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px'},
        {id : 'buttonType', title: '按钮类型' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px' , resolution:function(value, record, column, grid, dataNo, columnNo){
            var newValue = '';
            if(value == 1){
                newValue = '后台';
            } else if(value == 2){
                newValue = '弹出云表单';
            } else if(value == 3){
                newValue = '弹出自行开发表单';
            }
            return newValue;
        }},
        {title: '操作' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px' , resolution:function(value, record, column, grid, dataNo, columnNo){
            return '<i class="glyphicon glyphicon-trash" title="删除" onclick="del('+record.id+')"></i><i class="glyphicon glyphicon-edit" title="编辑" onclick="toUpdate('+record.id+')"></i>';
        }}
    ];

    var tools = 'refresh|faseQuery|add';

    var check = false;

//    alert(forSelect);
    //供选择的表单列表
    if(forSelect == '1'){
        $('#big-title-metar-flowButton').html('按钮选择');
        dtGridColumns_flowButton = [
            {id : 'buttonName', title: '按钮名称' , headerStyle : 'text-align:left;' , columnStyle : 'text-align:left;' , resolution:function(value, record, column, grid, dataNo, columnNo){
                return '<span class="btn btn-danger">'+value+'</span>';
            }},{id : 'buttonType', title: '按钮类型' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px;text-align:left;' , resolution:function(value, record, column, grid, dataNo, columnNo){
                var newValue = '';
                if(value == 1){
                    newValue = '后台';
                } else if(value == 2){
                    newValue = '弹出云表单';
                } else if(value == 3){
                    newValue = '弹出自行开发表单';
                }
                return newValue;
            } , columnClass:'text-left'}
        ];
        tools = '';
        check = true;
    }


    var dtGridOption_flowButton = {
        check:check,
        checkColumnStyle:'width:50px;text-align:center;padding-right:0',
        lang : 'zh-cn',
        ajaxLoad : true,
        loadURL: '<%=request.getContextPath()%>/flowButtonQuery.do',
        exportFileName : '自行开发表单列表',
        columns : dtGridColumns_flowButton,
        gridContainer : 'dtGridContainer_flowButton',
        toolbarContainer : 'dtGridToolBarContainer_flowButton',
        tools:tools,
        pageSize : 10,
        pageSizeLimit : [10, 20, 50],
        newRecordFunction : function(){
            $('#addComponentFormWin').modal('show');
            $('#buttonId').val('');
        },
        afterRender : function(){

//            grid_flowButton.spanTD(3 , 3 , 2 , 1);
        },
        onCheck : function(isChecked, record, grid, dataNo, row, extraCell, e) {
            if(forSelect == '1'){
                var gridId = grid_flowButton.option.id;
//                $('.dt-grid-check').attr('checked', false);
                if(isChecked){
                    seletedValues[record.id] = {buttonID : record.id , buttonName : record.buttonName};
                } else {
                    seletedValues[record.id] = '';
                }
                $('#dt_grid_'+gridId+'_check_'+dataNo).attr('checked', isChecked);
            }
        }
    };

    var grid_flowButton = $.fn.DtGrid.init(dtGridOption_flowButton);
    $(function () {
        grid_flowButton.load(function(){
            if(forSelect == '1'){
                var btnSelect = $('<button class="btn btn-danger" style="float: left;margin:5px;">选择</button>');
                $('#dtGridToolBarContainer_flowButton').prepend(btnSelect);
                btnSelect.click(function(){
                    var btnIDs = '';
                    var btnNames = '';
                    for(var o in seletedValues){
                        btnIDs += seletedValues[o].buttonID + ',';
                        btnNames += seletedValues[o].buttonName + ',';
                    }

                    if(btnIDs != ''){
                        btnIDs = btnIDs.substring(0 , btnIDs.length - 1);
                        btnNames = btnNames.substring(0 , btnNames.length - 1);
                    } else {
                        __show_bootstrap_msg('' , '请选择按钮');
                        return;
                    }

                    $('#btnIDs').val(btnIDs);
                    $('#btnNames').val(btnNames);
                    $('#selectFormWin').modal('hide');
                });
            }
            __hide_metar_loading();
        });

    });



</script>
</body>

</html>
