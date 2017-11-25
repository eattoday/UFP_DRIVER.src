<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ include file="/base/basePage.jsp" %>--%>
<%
//    String path = request.getContextPath();
    String tenantId = request.getParameter("tenantId");
    if(tenantId == null || "".equals(tenantId)){
        tenantId = "default";
    }
    String forSelect = request.getParameter("forSelect");
    if(!"1".equals(forSelect)){
        %><%@ include file="/base/basePage.jsp" %><%
    }
%>

<html>
<head>
    <title>自行开发表单</title>
    <style>
        .glyphicon-trash , .glyphicon-edit { padding-left: 0 !important; }
    </style>
</head>
<body>
<div id="big-title-metar-cusDevForm" class="big-title-metar">自行开发表单注册</div>
<div id="dtGridContainer_cusDevForm" class="dt-grid-container" style="height:400px"></div>
<div id="dtGridToolBarContainer_cusDevForm" class="dt-grid-toolbar-container"></div>
<%if(!"1".equals(forSelect)){
%>
<div class="modal fade" id="addComponentFormWin" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><i class="fa fa-plus"></i>&nbsp;&nbsp;自行开发表单注册</h4>
            </div>
            <div class="modal-body form-horizontal">
                <input id="componentId" type="hidden"/>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">表单名称</label></div><div class="col-sm-8"><input id="componentName" class="form-control" /></div>
                </div>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">PC端编辑模式URL</label></div><div class="col-sm-8"><input id="pcEditURL" class="form-control" /></div>
                </div>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">PC端只读模式URL</label></div><div class="col-sm-8"><input id="pcShowURL" class="form-control" /></div>
                </div>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">移动端编辑模式URL</label></div><div class="col-sm-8"><input id="mobileEditURL" class="form-control" /></div>
                </div>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">移动端只读模式URL</label></div><div class="col-sm-8"><input id="mobileShowURL" class="form-control" /></div>
                </div>
                <div class="form-group">
                    <div class="col-sm-3 text-right"><label class="control-label">是否发布</label></div>
                    <div class="col-sm-8">
                        <span class="radio">
                        <label style="padding-right:50px;">
                            <input name="release" type="radio" value="true" checked>是
                        </label>
                        <label style="padding-right:50px;">
                            <input name="release" type="radio" value="false">否
                        </label>
                    </span>
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
<script type="text/javascript">
    function toUpdate(formId){
        $('#addComponentFormWin').modal('show');
        $.ajax({
            url: _PATH + '/componentFormFindById.do',
            type: 'POST',
            async: true,
            dataType: 'json',
            data: { id : formId },
            success: function (response) {
                if(response){
                    $('#componentId').val(response.id);
                    $('#componentName').val(response.componentName);
                    $('#pcEditURL').val(response.pcEditURL);
                    $('#pcShowURL').val(response.pcShowURL);
                    $('#mobileEditURL').val(response.mobileEditURL);
                    $('#mobileShowURL').val(response.mobileShowURL);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                __show_bootstrap_msg('' , '获取信息失败，请稍后重试');
            }
        })
    }

    function save(){

        var componentName = $('#componentName').val();
        componentName = trim(componentName);

        if(componentName == ''){
//            alert('表单名字不能为空');
            __show_bootstrap_msg('componentName' , '表单名字不能为空');
            $('#componentName').focus().val(componentName);
            return;
        }

        var pcEditURL = $('#pcEditURL').val();
        pcEditURL = trim(pcEditURL);
        var pcShowURL = $('#pcShowURL').val();
        pcShowURL = trim(pcShowURL);
        var mobileEditURL = $('#mobileEditURL').val();
        mobileEditURL = trim(mobileEditURL);
        var mobileShowURL = $('#mobileShowURL').val();
        mobileShowURL = trim(mobileShowURL);

        if(pcEditURL == '' && pcShowURL == '' && mobileEditURL == '' && mobileShowURL == ''){
            __show_bootstrap_msg('pcEditURL' , '至少填写一个URL');
            $('#pcEditURL').focus();
            return;
        }

        var release = $("input[name='release']:checked").val();

        var componentId = $('#componentId').val();

        var data = {};
        data.componentName = componentName;
        data.pcEditURL = pcEditURL;
        data.pcShowURL = pcShowURL;
        data.mobileEditURL = mobileEditURL;
        data.mobileShowURL = mobileShowURL;
        data.release = release;
        data.id = componentId;
        data.tenantId = '<%=tenantId%>';

        $.ajax({
            url: _PATH + '/componentFormSave.do',
            type: 'POST',
            async: true,
            dataType: 'json',
            data: data,
            success: function (response) {
                if(response.success){
                    __show_bootstrap_msg('' , '注册成功');
                    $('#addComponentFormWin').modal('hide');
                    grid_cusDevForm.load();

                    $('#componentName').val('');
                    $('#pcEditURL').val('');
                    $('#pcShowURL').val('');
                    $('#mobileEditURL').val('');
                    $('#mobileShowURL').val('');

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
                url: _PATH + '/componentFormDelete.do',
                type: 'POST',
                async: true,
                dataType: 'json',
                data: { id : formId },
                success: function (response) {
                    if(response.success){
                        __show_bootstrap_msg('' , '删除成功');
                        grid_cusDevForm.load();
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
</script>
<%
}%>

<script type="text/javascript">
    var forSelect = '<%=forSelect%>';
    var seletedValue = {};
    __show_metar_loading();
    //映射内容
    var dtGridColumns_cusDevForm = [
        {id : 'componentName', title: '表单名称' , headerStyle : 'text-align:left;'},
        {id : 'lastUpdateTime',title : '更新时间' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px'},
        {id : 'release', title: '是否发布' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px' , resolution:function(value, record, column, grid, dataNo, columnNo){
            return value == true ? '是' : '否';
        }},
        {title: '操作' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px' , resolution:function(value, record, column, grid, dataNo, columnNo){
            return '<i class="glyphicon glyphicon-trash" title="删除" onclick="del('+record.id+')"></i><i class="glyphicon glyphicon-edit" title="编辑" onclick="toUpdate('+record.id+')"></i>';
        }}
    ];

    var tools = 'refresh|faseQuery|add';

//    alert(forSelect);
    //供选择的表单列表
    if(forSelect == '1'){
        $('#big-title-metar-cusDevForm').html('自行开发表单选择');
        dtGridColumns_cusDevForm = [
            {id : 'componentName', title: '表单名称' , headerStyle : 'text-align:left;' , columnStyle : 'text-align:left;'}
        ];
        tools = '';
    }


    var dtGridTitleSpan = [
        {id : 'componentName', title: '表单名称' , colspan: 2},
        {id : 'lastUpdateTime',title : '更新时间' , colspan : 2}
    ];
    var dtGridOption_cusDevForm = {
        check:true,
        checkColumnStyle:'width:50px;text-align:center;padding-right:0',
        lang : 'zh-cn',
        ajaxLoad : true,
        loadURL: '<%=request.getContextPath()%>/componentFormQuery.do',
        exportFileName : '自行开发表单列表',
        columns : dtGridColumns_cusDevForm,
//        titlespan :dtGridTitleSpan,
        gridContainer : 'dtGridContainer_cusDevForm',
        toolbarContainer : 'dtGridToolBarContainer_cusDevForm',
        tools:tools,
        pageSize : 10,
        pageSizeLimit : [10, 20, 50],
        newRecordFunction : function(){
            $('#addComponentFormWin').modal('show');
            $('#componentId').val('');
            $('#componentName').val('');
            $('#pcEditURL').val('');
            $('#pcShowURL').val('');
            $('#mobileEditURL').val('');
            $('#mobileShowURL').val('');
        },
        afterRender : function(){
            if(forSelect == '1'){
                $('#dt_grid_'+grid_cusDevForm.option.id+'_check').hide();
            }
//            grid_cusDevForm.spanTD(3 , 3 , 2 , 1);
        },
        onCheck : function(isChecked, record, grid, dataNo, row, extraCell, e) {
            if(forSelect == '1'){
                var gridId = grid_cusDevForm.option.id;
                $('#dt_grid_'+gridId+'_check').attr('checked', false);
                $('.dt-grid-check').attr('checked', false);
                if(isChecked){
                    seletedValue.formId  = record.id;
                    seletedValue.formName = record.componentName;
                } else {
                    seletedValue = {};
                }
                $('#dt_grid_'+gridId+'_check_'+dataNo)[0].checked = isChecked;
            }
        }
    };

    var grid_cusDevForm = $.fn.DtGrid.init(dtGridOption_cusDevForm);
    $(function () {
        grid_cusDevForm.load(function(){
            if(forSelect == '1'){
                var btnSelect = $('<button class="btn btn-danger" style="float: left;margin:5px;">选择</button>');
                $('#dtGridToolBarContainer_cusDevForm').prepend(btnSelect);
                btnSelect.click(function(){
                    if(!seletedValue.formId){
                        __show_bootstrap_msg('' , '请选择表单');
                        return;
                    }
                    if($('#cusDevFromID').length == 0){
                        $('#formType').val(4);
                        $('#customFormID').val(seletedValue.formId);
                        $('#customFormName').val('自行开发表单：' + seletedValue.formName);
                        $('#name').val(seletedValue.formName);
                    } else {
                        $('#cusDevFromID').val(seletedValue.formId);
                        $('#cusDevFromName').val(seletedValue.formName);
                    }
                    $('#selectFormWin').modal('hide');
                });
            }
            __hide_metar_loading();
        });

    });



</script>
</body>

</html>
