<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="component/jquery.dtGrid.v1.1.9/dependents/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="base/_css/menu.css"/>
    <style>
        body{
            margin: 0px;
        }
    </style>
</head>
<body onresize="__page_init()">
<table id="container-table">
    <tbody>
    <tr>
        <td id="model-title-td" style="width:200px;">
            <div id="model-title-container">
                <div id="model-title">业务驱动模块</div>
                <ul id="menu-ul">
                    <li><a href="base/page/todo_old.jsp" target="__menu_body" >待办</a></li>
                    <li><a href="base/page/todo.jsp" target="__menu_body" >待办xin</a></li>
                    <li><a href="base/page/cusDevFormList.jsp" target="__menu_body" >自定义表单</a></li>
                    <li><a href="base/page/already_old.jsp" target="__menu_body">已办</a></li>
                    <li><a href="base/page/already.jsp" target="__menu_body">已办xin</a></li>
                    <li><a href="base/page/flowButtonList.jsp" target="__menu_body">按钮列表</a></li>
                    <li><a href="base/page/applyDraftManage.jsp" target="__menu_body">申请草稿管理</a></li>
                    <li><a href="base/page/dispatchDraftManage.jsp" target="__menu_body">调度草稿管理</a></li>
                    <%--<li><a href="dataApplyController.do?method=initApply&processCode=1&businessCode=E161" target="__menu_body">申请单拟稿</a></li>--%>
                    <%--<li><a href="dataDispatchController.do?method=initDispatch&processCode=1&businessCode=E161" target="__menu_body">调度单拟稿</a></li>--%>
                    <li><a href="base/frame/workorderList.jsp" target="__menu_body">工单查询</a></li>
                    <li><a href="base/page/launch.jsp" target="__menu_body">发起工单</a></li>
                    <%--<li><a href="publishhWorkOrderController.do?method=init&tenantId=uflowplatform" target="__menu_body">发布工单</a></li>--%>
                    <li><a href="base/page/workOrderList.jsp" target="__menu_body">起草工单</a></li>
                    <li><a href="base/page/workOrderListDev.jsp" target="__menu_body">发布工单列表</a></li>



                </ul>
            </div>
        </td>
        <td id="expand-btn" class="expand-btn-left" style="vertical-align: middle;" onclick="expandMenu(this)"><img id="btn-collapse" src="base/_resources/collapse.png" /><img id="btn-expand" style="display: none;" src="base/_resources/expand.png" /></td>
        <td>
            <iframe frameborder="no" id="__menu_body" name="__menu_body" style="width:100%;overflow-x:hidden" src="base/page/todo_old.jsp">

            </iframe>
        </td>
    </tr>
    </tbody>
</table>
</body>
<script type="text/javascript" src="component/jquery.dtGrid.v1.1.9/dependents/jquery/jquery.min.js"></script>
<script>
    $(document).ready(function(){
        __page_init();
    });
    function __page_init() {
        document.getElementById('__menu_body').style.height = document.documentElement.clientHeight - 10 + 'px';
        document.getElementById('menu-ul').style.height = document.documentElement.clientHeight - 50 + 'px';
    }
    function expandMenu(expandBtn) {
        debugger;

        var modelTitleTd = $('#model-title-td');
        if (modelTitleTd.css('display') == 'none') {
            modelTitleTd.show();
            $('#btn-collapse').show();
            $('#btn-expand').hide();
            $(expandBtn).addClass('expand-btn-left');
        } else {
            modelTitleTd.hide();
            $('#btn-collapse').hide();
            $('#btn-expand').show();
            $(expandBtn).removeClass('expand-btn-left');
        }
    }
    $('#menu-ul li a').click(function () {
        $('#menu-ul li a').removeClass('menu-active');
        $(this).addClass('menu-active');
    });
    $('#menu-ul li a').first().addClass('menu-active');
</script>
</html>
