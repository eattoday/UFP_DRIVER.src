<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>工单管理</title>
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
                <div id="model-title">工单管理</div>
                <ul id="menu-ul">
                    <li><a href="base/page/launchList.jsp" target="__menu_body">发起工单</a></li>
                    <li><a href="base/page/todo.jsp" target="__menu_body">待办</a></li>
                    <li><a href="base/page/already.jsp" target="__menu_body">已办</a></li>
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
<script type="text/javascript"
        src="component/jquery.dtGrid.v1.1.9/dependents/jquery/jquery.min.js"></script>
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
