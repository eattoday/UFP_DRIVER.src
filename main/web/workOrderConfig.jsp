<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>工单配置</title>
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
                <div id="model-title">工单配置</div>
                <ul id="menu-ul">
                    <li><a href="workflow/forward.jsp?url=/workspace/com.primeton.bps.web.composer.bizflowmgr.bizProcessCustomFrame.flow&globalUniqueID=${globalUniqueID}" target="__menu_body">流程定制</a></li>
                    <li><a href="workflow/forward.jsp?url=/workspace/workflow/wfmanager/instance/index.jsp&globalUniqueID=${globalUniqueID}" target="__menu_body">流程实例管理</a></li>
                    <li><a href="workflow/forward.jsp?url=/workspace/workflow/wfmanager/instance/processinst_frame.jsp&globalUniqueID=${globalUniqueID}" target="__menu_body">流程实例管理</a></li>
                    <%--<li><a href="/cform/jsp/cform/form/queryform.jsp?tenantId=default" target="__menu_body">表单管理</a></li>--%>
                    <li><a href="base/page/formManageSelect.jsp" target="__menu_body">表单管理</a></li>
                    <li><a href="base/page/flowButtonList.jsp?tenantId=" target="__menu_body">按钮管理</a></li>
                    <li><a href="/PMOS/processController.do?method=initProcess&globalUniqueID=${globalUniqueID}" target="__menu_body">流程管理</a></li>
                    <li><a href="/PMOS/nodeController.do?method=initNode&globalUniqueID=${globalUniqueID}" target="__menu_body">节点管理</a></li>
                    <%--<li><a href="publishhWorkOrderController.do?method=init" target="__menu_body">发布工单</a></li>--%>
                    <li><a href="base/page/workOrderListDev.jsp" target="__menu_body">发布工单</a></li>
                    <li><a href="base/page/busiFlowList.jsp?tenantId=" target="__menu_body">业务流程管理</a></li>
                </ul>
            </div>
        </td>
        <td id="expand-btn" class="expand-btn-left" style="vertical-align: middle;" onclick="expandMenu(this)"><img id="btn-collapse" src="base/_resources/collapse.png" /><img id="btn-expand" style="display: none;" src="base/_resources/expand.png" /></td>
        <td>
            <iframe frameborder="no" id="__menu_body" name="__menu_body" style="width:100%;overflow-x:hidden" src="">

            </iframe>
        </td>
    </tr>
    </tbody>
</table>
</body>
<script type="text/javascript" src="component/jquery.dtGrid.v1.1.9/dependents/jquery/jquery.min.js"></script>
<script type="text/javascript" src="component/jquery.dtGrid.v1.1.9/dependents/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="base/_js/require.js"></script>
<script>
    $(document).ready(function(){
        __page_init();
        document.getElementById('__menu_body').src = $('a')[0].href;
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
