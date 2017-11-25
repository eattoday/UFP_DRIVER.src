<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/framework/basePage.jsp" %>
<%
    String tenantId = request.getParameter("tenantId");
    if(tenantId == null || "".equals(tenantId)){
        tenantId = "default";
    }
%>

<html>
<head>
    <title>工单列表</title>
</head>
<script>
    var datas = new Array();

</script>
<body>
<div class="big-title-metar">发布工单列表</div>
<div id="dtGridContainer_publish" class="dt-grid-container"></div>
<div id="dtGridToolBarContainer_publish" class="dt-grid-toolbar-container"></div>
<div class="modal fade" id="addWorkOrderWin" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width:90%;left:5%;position:absolute">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="addWorkOrderWinTitle">发布工单</h4>
            </div>
            <div class="modal-body" id="addWorkOrderWinBody"></div>
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
</body>
<script>
    __show_metar_loading();
     //映射内容
    //name;    //发起工单名称
    //customFormName;    //自定义表单名称
    //processModelName;    //流程定义名称
    //tenantId;    //租户ID
    var dtGridColumns_publish = [
        {
            id: 'name', title: '发起工单名称', type: 'string', headerStyle : 'width:300px;text-align:left;' , columnStyle : 'width:300px',
            resolution:function(value, record, column, grid, dataNo, columnNo)
            {
                return '<a href="<%=path%>/toLaunch.do?id='+record.id+'">'+value+'</a>';
            }
        },{
            title: 'URL', type: 'string', headerStyle : 'text-align:left;', columnClass: 'text-center',
            resolution:function(value, record, column, grid, dataNo, columnNo)
            {
                return '<%=path%>/toLaunch.do?id=' + record.id;
            }
        }, {
            id: 'customFormName', title: '表单名称', type: 'string', headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px',columnClass:'text-center'
        },{
            id: 'formType', title: '表单类型', type: 'string', headerStyle : 'width:100px;text-align:left;' , columnStyle : 'width:100px',columnClass:'text-center'
        }, {
            id: 'processModelName', title: '流程定义名称', type: 'string', headerStyle : 'width:200px;text-align:left;' , columnStyle : 'width:200px',columnClass:'text-center'
        }
    ];


    var dtGridOption_publish = {
        lang: 'zh-cn',
        ajaxLoad: false,
        datas: datas,
        exportFileName: '工单列表',
        columns: dtGridColumns_publish,
        gridContainer: 'dtGridContainer_publish',
        toolbarContainer: 'dtGridToolBarContainer_publish',
        tools: 'refresh|faseQuery|add',
        pageSize: 10,
        pageSizeLimit: [10, 20, 50],
        newRecordFunction : function(){
            $('#addWorkOrderWin').modal('show');
            $('#addWorkOrderWinBody').empty().load(_PATH + '/publishhWorkOrderController.do?method=init&tenantId=<%=tenantId%>');
        }
    };
    var grid_publish = $.fn.DtGrid.init(dtGridOption_publish);

    $(document).ready(function(){
        var url='<%=path%>/publishhWorkOrderController.do?method=getList&tenantId=<%=tenantId%>';
        $.ajax({
            url:url,
            type:"GET",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            success: function(json){
                $.each(json.datas,function(n,value) {
                    debugger;
                    var dt = new Object();
                    dt.name = value[0];
                    dt.customFormID = value[1];
                    dt.customFormName = value[2];
                    dt.processModelName = value[3];
                    dt.tenantId = value[4];
                    dt.id = value[5];
                    datas.push(dt);

                });

                grid_publish.load();
                __hide_metar_loading();
            }
        });
    });
</script>


</html>
