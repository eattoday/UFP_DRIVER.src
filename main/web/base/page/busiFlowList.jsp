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
        %><%@ include file="/framework/basePage.jsp" %><%
    }
%>

<html>
<head>
    <title>业务流程</title>
    <style>
        .glyphicon-trash , .glyphicon-edit { padding-left: 0 !important; }
        .form-horizontal .form-group { margin-left:0;margin-right:0;}
    </style>
</head>
<body>
<div class="big-title-metar">业务流程管理</div>
<div id="dtGridContainer_busiFlow" class="dt-grid-container" style="height:400px"></div>
<div id="dtGridToolBarContainer_busiFlow" class="dt-grid-toolbar-container"></div>
<%if(!"1".equals(forSelect)){
%>
<div id="addComponentFormWin" style="display:none;padding-top:20px;">
    <div class="form-horizontal">
        <input id="busiFlowID" type="hidden"/>
        <div class="form-group">
            <div class="col-sm-3 text-right"><label class="control-label">业务流程名称</label></div><div class="col-sm-8"><input id="busiFlowName" class="form-control" /></div>
        </div>
        <div class="form-group">
            <div class="col-sm-3 text-right"><label class="control-label">开发流程</label></div>
            <div class="col-sm-8">
                <div class="input-group">
                    <input id="processDefIDs" type="hidden" />
                    <input id="processDefCodes" type="hidden" />
                    <input id="processDefNames" readonly="readonly" type="text" class="form-control form-readonly" style="border:1px solid #ccc"/>
                    <span onclick="selectProcess()" class="input-group-addon glyphicon glyphicon-th"></span>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function(){

        layui.use(['layer'], function(){
            showILoading();
        });

        initForm();
    });

    function initForm(){
        $('#busiFlowID').val('');
        $('#busiFlowName').val('');

        $('#processDefIDs').val('');
        $('#processDefCodes').val('');
        $('#processDefNames').val('');
        $('#specialtyIDs').val('');
        $('#specialtyNames').val('');

    }

    function toUpdate(formId){
        showIModal({
            title:'修改业务流程',
            type: 1,
            maxmin:true,
            area: '800px',
            content: $('#addComponentFormWin'),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            success : function(){

                showILoading();

                $.ajax({
                    url: _PATH + '/busiFlowFindById.do',
                    type: 'POST',
                    async: true,
                    dataType: 'json',
                    data: { id : formId },
                    success: function (response) {
                        if(response){

                            $('#busiFlowID').val(response.id);
                            $('#busiFlowName').val(response.name);
                            $('#processDefIDs').val(response.processDefIDs);
                            $('#processDefCodes').val(response.processDefCodes);
                            $('#processDefNames').val(response.processDefNames);
                        }

                        closeILoading();
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        iMsg('获取信息失败，请稍后重试');
                        closeILoading();
                    }
                })
            },
            btn:['保存' , '关闭'],
            btn1:function(index, layero){
                save();
                return false;
            }

        });
    }

    function save(){

        var busiFlowName = $('#busiFlowName').val();
        busiFlowName = trim(busiFlowName);
        if(busiFlowName == ''){
            iMsg('【业务流程名称】不能为空');
            $('#busiFlowName').focus().val(busiFlowName);
            return;
        }
        var processDefIDs = $('#processDefIDs').val();
        var processDefCodes = $('#processDefCodes').val();
        var processDefNames = $('#processDefNames').val();
        if(processDefIDs == ''){
            iMsg('请选择【流程】');
            $('#processDefNames').focus();
            return;
        }

        var busiFlowID = $('#busiFlowID').val();

        var data = {};
        data.name = busiFlowName;
        data.processDefIDs = processDefIDs;
        data.processDefCodes = processDefCodes;
        data.processDefNames = processDefNames;
//        data.specialtyIDs = specialtyIDs;
//        data.specialtyNames = specialtyNames;
        data.id = busiFlowID;
        data.tenantId = '<%=tenantId%>';

        showILoading();

        $.ajax({
            url: _PATH + '/busiFlowSave.do',
            type: 'POST',
            async: true,
            dataType: 'json',
            data: data,
            success: function (response) {
                if(response.success){
                    iMsg('保存成功' , 6);

                    closeIModal();
                    closeILoading();
                    grid_busiFlow.load();
                    initForm();

                } else {
                    iMsg(response.msg);
                    closeILoading();
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                iMsg('保存失败，请稍后重试');
                closeILoading();
            }
        })
    }

    function del(formId){

        iConfirm('确认删除吗?' , {icon : 3 , title : '提示'} , function(){

            showILoading();

            $.ajax({
                url: _PATH + '/busiFlowDelete.do',
                type: 'POST',
                async: true,
                dataType: 'json',
                data: { id : formId },
                success: function (response) {
                    if(response.success){
                        iMsg('删除成功' , 6);
                        grid_busiFlow.load();
                        closeILoading();
                    } else {
                        iMsg(response.msg);
                        closeILoading();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    iMsg('保存失败，请稍后重试');
                    closeILoading();
                }
            })
        });
    }

    function selectProcess(){
        showIModal({
            title:'选择流程',
            type: 2,
            maxmin:false,
            area: ['500px' , '400px'],
            content: _PATH + '/framework/page/processActiTree.jsp',//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            btn:['选择' , '关闭'],
            btn1:function(index, layero){
                var selected = layero.find('iframe')[0].contentWindow.getSelected();
                if(selected == ''){
                    iMsg('请选择流程');
                } else {
                    var processDefIDs = '';
                    var processDefCodes = '';
                    var processDefNames = '';
                    for(var i = 0 ; i < selected.length ; i++){
                        if(selected[i].type != 1){
                            continue;
                        }
                        processDefIDs += selected[i].id + ',';
                        processDefCodes += selected[i].code + ',';
                        processDefNames += selected[i].label + ',';
                    }

                    if(processDefIDs != ''){
                        processDefIDs = processDefIDs.substring(0 , processDefIDs.length - 1);
                        processDefCodes = processDefCodes.substring(0 , processDefCodes.length - 1);
                        processDefNames = processDefNames.substring(0 , processDefNames.length - 1);
                    }

                    $('#processDefIDs').val(processDefIDs);
                    $('#processDefCodes').val(processDefCodes);
                    $('#processDefNames').val(processDefNames);
                    if($('#busiFlowName').val() == ''){
                        $('#busiFlowName').val(processDefNames);
                    }

                    layer.close(index);
                }

                return false;
            }
        })
    }

</script>
<%
}%>

<script type="text/javascript">



    var forSelect = '<%=forSelect%>';
    var seletedValues = {};

    //映射内容
    var dtGridColumns_busiFlow = [
        {id : 'name', title: '业务流程名称' , headerStyle : 'text-align:left;' },
        {id : 'specialtyNames', title: '专业' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px'},
        {id : 'lastUpdateTime',title : '更新时间' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px'},
        {title: '操作' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px' , resolution:function(value, record, column, grid, dataNo, columnNo){
            return '<i class="glyphicon glyphicon-trash" title="删除" onclick="del('+record.id+')"></i><i class="glyphicon glyphicon-edit" title="编辑" onclick="toUpdate('+record.id+')"></i>';
        }}
    ];

    var tools = 'refresh|faseQuery|add';

    var check = false;

//    alert(forSelect);
    //供选择的表单列表
    if(forSelect == '1'){
        $('.big-title-metar').html('按钮选择');
        dtGridColumns_busiFlow = [
            {id : 'buttonName', title: '业务流程名称' , headerStyle : 'text-align:left;' , columnStyle : 'text-align:left;'},
            {id : 'buttonType', title: '专业' , headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px;text-align:left;', columnClass:'text-left'}
        ];
        tools = '';
        check = true;
    }


    var dtGridOption_busiFlow = {
        check:check,
        checkColumnStyle:'width:50px;text-align:center;padding-right:0',
        lang : 'zh-cn',
        ajaxLoad : true,
        loadURL: '<%=request.getContextPath()%>/busiFlowQuery.do',
        exportFileName : '自行开发表单列表',
        columns : dtGridColumns_busiFlow,
        gridContainer : 'dtGridContainer_busiFlow',
        toolbarContainer : 'dtGridToolBarContainer_busiFlow',
        tools:tools,
        pageSize : 10,
        pageSizeLimit : [10, 20, 50],
        newRecordFunction : function(){
            showIModal({
                title:'新增业务流程',
                type: 1,
                maxmin:true,
                area: '800px',
                content: $('#addComponentFormWin'),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btn:['保存' , '关闭'],
                btn1:function(index, layero){
                    save();
                    return false;
                }

            });
//            $('#addComponentFormWin').modal('show');
            $('#buttonId').val('');
        },
        afterRender : function(){

//            grid_busiFlow.spanTD(3 , 3 , 2 , 1);
        },
        onCheck : function(isChecked, record, grid, dataNo, row, extraCell, e) {
            if(forSelect == '1'){
                var gridId = grid_busiFlow.option.id;
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

    var grid_busiFlow = $.fn.DtGrid.init(dtGridOption_busiFlow);
    $(function () {
        grid_busiFlow.load(function(){
            if(forSelect == '1'){
                var btnSelect = $('<button class="btn btn-danger" style="float: left;margin:5px;">选择</button>');
                $('#dtGridToolBarContainer_busiFlow').prepend(btnSelect);
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
                        iMsg('请选择按钮');
                        return;
                    }

                    $('#btnIDs').val(btnIDs);
                    $('#btnNames').val(btnNames);
                    $('#selectFormWin').modal('hide');
                });
            }
            closeILoading();
        });

    });



</script>
</body>

</html>
