<%@ page import="com.metarnet.driver.Constant" %>
<!DOCTYPE HTML>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ include file="/base/basePage.jsp" %>--%>
<%
    String tenantId = request.getParameter("tenantId");
    if(tenantId == null || "".equals(tenantId)){
        tenantId = "t1";
    }

%>

<html>
<head>
    <title>自定义表单</title>
</head>
<body>
<div class="big-title-metar">云表单选择</div>
<div id="dtGridContainer_cloudForm" class="dt-grid-container" style=""></div>
<div id="dtGridToolBarContainer_cloudForm" class="dt-grid-toolbar-container"></div>

</body>
<script>
    __show_metar_loading();
    var seletedValue = {};
    //映射内容
    var dtGridColumns_cloudForm = [
        { id: 'formId', title: '表单编号', type: 'string',headerStyle : 'text-align:left;', columnClass: 'text-center' },
        { id: 'formName', title: '表单名称', type: 'string',headerStyle : 'text-align:left;', columnClass: 'text-center' }
    ];

    var datas = new Array();

    var dtGridOption_cloudForm = {
        checkColumnStyle:'width:50px;text-align:center;padding-right:0',
        lang: 'zh-cn',
        ajaxLoad: false,
        check : true,
        datas: datas,
        exportFileName: '表单列表',
        columns: dtGridColumns_cloudForm,
        gridContainer: 'dtGridContainer_cloudForm',
        toolbarContainer: 'dtGridToolBarContainer_cloudForm',
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
        afterRender : function(){
            $('#dt_grid_'+grid_cloudForm.option.id+'_check').hide();
        },
        onCheck : function(isChecked, record, grid, dataNo, row, extraCell, e) {
            var gridId = grid_cloudForm.option.id;
            $('#dt_grid_'+gridId+'_check').attr('checked', false);
            $('.dt-grid-check').attr('checked', false);
            if(isChecked){
                seletedValue.formId  = record.formId;
                seletedValue.formName = record.formName;
            } else {
                seletedValue = {};
            }
            $('#dt_grid_'+gridId+'_check_'+dataNo)[0].checked = isChecked;
        }

    };
    var grid_cloudForm = $.fn.DtGrid.init(dtGridOption_cloudForm);


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
                grid_cloudForm.load(function(){
                    var btnSelect = $('<button class="btn btn-danger" style="float: left;margin:5px;">选择</button>');
                    $('#dtGridToolBarContainer_cloudForm').prepend(btnSelect);
                    btnSelect.click(function(){
                        if(!seletedValue.formId){
                            __show_bootstrap_msg('' , '请选择表单');
                            return;
                        }
                        if($('#cusDevFromID').length == 0){
                            $('#formType').val(1);
                            $('#customFormID').val(seletedValue.formId);
                            $('#customFormName').val('云表单：' + seletedValue.formName)
                            $('#name').val(seletedValue.formName);
                        } else {
                            $('#cloudFormID').val(seletedValue.formId);
                            $('#cloudFormName').val(seletedValue.formName);
                        }

                        $('#selectFormWin').modal('hide');
                    });
                });

                __hide_metar_loading();
            }
        });
    });

    function checkedHide(){
        $('#dt_grid_'+grid_cloudForm.option.id+'_check').hide();
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
