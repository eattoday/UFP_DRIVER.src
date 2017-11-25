<%@ page import="com.metarnet.driver.Constant" %>
<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<%
    String tenantId = request.getParameter("tenantId");
%>
<html>
<head>
    <title>选择表单</title>

</head>
<script>
    var datas = new Array();

</script>
<body>
<div class="big-title-metar">选择表单</div>
<div style="margin-left: auto;margin-right: 40px;width: 20px;">
    <input type="button" value="选择" onclick="selectRow()">
</div>
<div id="dtGridContainer_2_1_2" class="dt-grid-container"></div>
<div id="dtGridToolBarContainer_2_1_2" class="dt-grid-toolbar-container"></div>

</body>
<script>
    __show_metar_loading();
    var seletedValue;
    //映射内容
    var dtGridColumns_2_1_2 = [
        {
            id: 'formId', title: '表单编号', type: 'string', columnClass: 'text-center'
        }, {
            id: 'formName', title: '表单名称', type: 'string', columnClass: 'text-center'
        }
    ];


    var dtGridOption_2_1_2 = {
        lang: 'zh-cn',
        ajaxLoad: false,
        check : true,
        datas: datas,
        exportFileName: '表单列表',
        columns: dtGridColumns_2_1_2,
        gridContainer: 'dtGridContainer_2_1_2',
        toolbarContainer: 'dtGridToolBarContainer_2_1_2',
        tools: '',
        pageSize: 10,
        pageSizeLimit: [10, 20, 50],
//        onRowClick : function(value, record, column, grid, dataNo, columnNo, cell, row, extraCell, e) {
//
//            var log = '<p>行事件触发。事件类型：' + e.type + '；触发行内单元格坐标：(' + columnNo + ',' + dataNo + ')；行内单元格内容：' + value + '。</p>';
//            seletedValue=JSON.stringify({
//                formId : record.formId,
//                formName : record.formName
//            });
//            console.log(log);
//        }
        onCheck : function(isChecked, record, grid, dataNo, row, extraCell, e) {

            var gridId = grid_2_1_2.option.id;
            $('#dt_grid_'+gridId+'_check').attr('checked', false);
            for (var i=0;i<grid.option.datas.length;i++)
            {

                if(i != dataNo || e.target.id=='dt_grid_'+gridId+'_check')
                    $('#dt_grid_'+gridId+'_check_'+i).attr('checked', false);

            }

//            var log = '<p>复选事件触发。是否复选：' + isChecked + '；触发行坐标：' + dataNo + '。</p>';
//            seletedValue=JSON.stringify({
//                formId : record.formId,
//                formName : record.formName
//            });
//            console.log(log);
            seletedValue={
                formId : record.formId,
                formName : record.formName
            };
        }

    };
    var grid_2_1_2 = $.fn.DtGrid.init(dtGridOption_2_1_2);


    $(document).ready(function(){
        var url="<%=Constant.Cform_Url%>/command/ajax/org.loushang.cform.form.cmd.FormQueryCmd/queryFormsWithBizModel";
        var data='{"params": {"javaClass": "ParameterSet","map": {"tenantId": "<%=tenantId%>","start": 0,"limit": 10000000,"isRelease": "1"},"length": 5}}';
        $.ajax({
            url:url,
            type:"POST",
            data:data,
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            success: function(json){
                $.each(json.rows,function(n,value) {
                    var dt = new Object();
                    dt.formId = value.formId;
                    dt.formName = value.formName;
                    datas.push(dt);

                });
                grid_2_1_2.load(function(){

                });

                __hide_metar_loading();

                setTimeout('checkedHide()', 400);//延迟

            }
        });
    });

    function checkedHide(){
        $('#dt_grid_'+grid_2_1_2.option.id+'_check').hide();
    }

</script>

<script language="javascript" type="text/javascript">

    function selectRow() {

//        opener.setValue(seletedValue);
        window.returnValue=seletedValue;
        window.close();

    }

</script>


</html>
