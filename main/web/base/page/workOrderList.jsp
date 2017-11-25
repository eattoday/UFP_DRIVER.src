<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>
<%
    String tenantId = request.getParameter("tenantId");
    if(tenantId == null || "".equals(tenantId)){
        tenantId = "default";
    }
%>

<html>
<head>
    <title>起草工单</title>

</head>
<script>
    var datas = new Array();

</script>
<body>
<div class="big-title-metar">起草工单</div>
<div id="dtGridContainer_2_1_2" class="dt-grid-container"></div>
<div id="dtGridToolBarContainer_2_1_2" class="dt-grid-toolbar-container"></div>

</body>
<script>
    __show_metar_loading();
     //映射内容
    //name;    //发起工单名称
    //customFormName;    //自定义表单名称
    //processModelName;    //流程定义名称
    //tenantId;    //租户ID
    var dtGridColumns_2_1_2 = [
        {
            id: 'name', title: '发起工单名称', type: 'string', columnClass: 'text-center',
            resolution:function(value, record, column, grid, dataNo, columnNo)
            {
                return '<a href="<%=path%>/base/page/launch.jsp?formId='+record.customFormID+'&tenantId='+record.tenantId+'&processModelName='+record.processModelName+'">'+value+'</a>';
            }
        }, {
                id: 'customFormName', title: '自定义表单名称', type: 'string', columnClass: 'text-center'
            }, {
                id: 'processModelName', title: '流程定义名称', type: 'string', columnClass: 'text-center'
            }
        ];


    var dtGridOption_2_1_2 = {
        lang: 'zh-cn',
        ajaxLoad: false,
        datas: datas,
        exportFileName: '起草工单列表',
        columns: dtGridColumns_2_1_2,
        gridContainer: 'dtGridContainer_2_1_2',
        toolbarContainer: 'dtGridToolBarContainer_2_1_2',
        tools: '',
        pageSize: 10,
        pageSizeLimit: [10, 20, 50]

    };
    $.fn.DtGrid.lang['zh-cn']['pageInfo']['nothing'] = '无已发布表单' ;

    var grid_2_1_2 = $.fn.DtGrid.init(dtGridOption_2_1_2);

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
                    datas.push(dt);

                });

                grid_2_1_2.load();
                __hide_metar_loading();
            }
        });
    });
</script>


</html>
