<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" >
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:include page="../basePage.jsp"/>
<%
    String __fromPage = request.getParameter("fromPage");
    String __area = request.getParameter("__area");
    if(__area != null && !"".equals(__area)){
        String __taskInstance = "{}";
        request.setAttribute("__buildJSON" , java.net.URLDecoder.decode(__area , "UTF8"));
        request.setAttribute("__taskInstance" , __taskInstance);
    }
    String path = request.getContextPath();
    String staticPath = request.getContextPath();

%>
<html>
<head>
    <style>
        .btn-conponentA { height:40px;overflow:hidden;text-overflow:ellipsis;padding-left:10px;padding-right:10px;line-height: 40px;border-bottom:1px solid #f8f8f8;}
        /*.btn-conponentA:hover { cursor: pointer;background-color: #f8f8f8;font-weight: bold}*/
        .componentDiv iframe { padding-left:10px;padding-right:10px;padding-bottom: 20px;}
        .componentDiv:hover { cursor: pointer;background-color: #f8f8f8;font-weight: bold}
        .btn-conponentA-active { font-weight: bold}
        .componentDiv { padding-top:0px;}
        .component-frame { width:100%;}
        #__page_big_title , #__component_container{ margin-top: 0;}
        #__link_bar { display:none; }
        .glyphicon { padding-left: 0 !important; padding-right: 5px !important;}
        html,body{ height: 99%; }

    </style>
    <link rel="stylesheet" type="text/css" href="<%=staticPath%>/component/layui-v1.0.9/css/layui.css"/>
    <script type="text/javascript" src="<%=staticPath%>/component/layui-v1.0.9/layui.js"></script>

</head>
<body>
<div id="__link_bar"><a onclick="__showOldMonitor()">流程监控</a></div>
<div id="__page_big_title" class="big-title-metar"></div>
<div id="__areaName_list"></div>
<div id="__component_container"></div>
<%--<div style="height:40px;"></div>--%>
<div id="foot-bar" style="width:100%;text-align:right;padding-right:10px;position:fixed;z-index:999;bottom:0;"></div>
<div id="__link_container"></div>
<%--<jsp:include page="../page/receiveForm.jsp"></jsp:include>--%>
<%--${__buildJSON}--%>
<%--${__taskInstance}--%>
<div class="modal fade" id="workMonitorWin" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width:90%;height:90%;left:5%;position:absolute">
        <div class="modal-content" style="position:absolute;height:100%;width:100%">
            <div class="modal-header" style="position:absolute;z-index:1;;width:100%">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="workMonitorWinTitle">流程监控</h4>
            </div>
            <div class="modal-body" id="workMonitorWinBody" style="position:absolute;height:100%;;width:100%">
                <iframe id="workMonitorWinBodyFrame" src="" style="border:none;width:100%;height:100%;"></iframe>
            </div>
        </div>
    </div>
</div>
<%--<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/common.js"></script>--%>
<script>
    $(document).ready(function () {
        layui.use(['layer'], function () {
            alert(123);

        })
    });
var _winParams={};
_winParams.__returnUrl = '<%=request.getParameter("__returnUrl")%>';

if('${__taskInstance}' != '{}'){
    _winParams.taskInst=${__taskInstance};
    _winParams.activityInstName=_winParams.taskInst.activityInstName||null;
    _winParams.activityInstID=_winParams.taskInst.activityInstID;
    _winParams.activityDefID=_winParams.taskInst.activityDefID||null;
    _winParams.processingChainId=_winParams.taskInst.processingChainId||null;
    _winParams.processModelId=_winParams.taskInst.processModelId||null;
    _winParams.processModelName=_winParams.taskInst.processModelName;
    _winParams.processInstID=_winParams.taskInst.processInstID||null;
    _winParams.taskInstID=_winParams.taskInst.taskInstID||'';
    _winParams.shard=_winParams.taskInst.shard||null;
//新增
    _winParams.jobCode=_winParams.taskInst.jobCode||'';
    _winParams.jobID=_winParams.taskInst.jobID||'';
    _winParams.numColumn1=_winParams.taskInst.numColumn1||0;
    _winParams.productcode=_winParams.taskInst.productcode||null;
    _winParams.majorcode=_winParams.taskInst.majorcode||null;
    _winParams.businessId=_winParams.taskInst.businessId||null;
    _winParams.businessId=_winParams.taskInst.businessId||null;
    _winParams.currentState=_winParams.taskInst.currentState||null;
    _winParams.rootProcessInstId=_winParams.taskInst.rootProcessInstId||null;
    _winParams.businessCode=_winParams.businessCode||null;
    _winParams.createDate=_winParams.taskInst.createDate||null;
    //用于存储汇总组编号
    _winParams.strColumn4=_winParams.taskInst.strColumn4||null;
    delete _winParams.taskInst;

}


var __$__processingObjectId = 0;
var __$__processingObjectTable = 0;

var TASKLIST;

var __$__last_processingObjectId = 0;
var __$__last_processingObjectTable = 0;

var __buildJSON = eval('(${__buildJSON})');

var __btn_workMonitor = $('<button class="btn btn-danger">流程监控</button>');
__btn_workMonitor.click(function(){
    __showWorkFlowMonitor();
});
$('#foot-bar').append(__btn_workMonitor);

var __componentModels = __buildJSON.componentModels;
if(__componentModels){

    var expandFirst = true;

    for(var __i = 0 ; __i < __componentModels.length ; __i++){
        var componentModel = __componentModels[__i];

        var __componentDiv = $('<div class="componentDiv"></div>');
        $('#__component_container').append(__componentDiv);
        var iframeID = new Date().getTime() + __i;

        var areaName = componentModel.areaName;
        if(__i == 0){
            if(!areaName){
                areaName = componentModel.activityInstName;
            }
            $('#__page_big_title').html(areaName);
        }

        if(componentModel.model == 'edit'){
            if(componentModel.componentModelURL){
                //若URL为空，不显示area
                expandFirst = false;

                if(!areaName){
                    areaName = componentModel.activityInstName;
                }

                __componentDiv.append('<div class="btn-conponentA btn-conponentA-active">' + areaName + '</div>');
                iframeID = '__edit_iframe_id';
                var url = componentModel.componentModelURL;
                if(url.indexOf('?') > -1){
                    url += '&';
                } else {
                    url += '?';
                }
                url += 'iframeID='+iframeID;
                for(var pro in _winParams){
                    url += "&" + pro + "=" + _winParams[pro];
                }
                url += '&formId='+componentModel.formId;
                __componentDiv.append('<iframe id="'+iframeID+'" style="width:100%;" frameborder="0" src="'+url+'"></iframe>');
            }
            if(componentModel.btnIDs != ''){
                var btnIDArray = componentModel.btnIDs.split(',');
                var btnNamesArray = componentModel.btnNames.split(',');

                for(var i = 0 ; i < btnIDArray.length ; i++){

                    var button = $('<button id="btn-submit" type="button" class="btn btn-danger" style="margin-left:5px;margin-right:5px;">'+btnNamesArray[i]+'</button>');
                    $('#foot-bar').prepend(button);

                    initButton(btnIDArray[i] , btnNamesArray[i] , button , componentModel.tenantId , '${globalUniqueID}');
                }
            } else {
                var button = $('<button id="btn-submit" type="button" class="btn btn-danger" style="margin-left:5px;margin-right:5px;">提交</button>');
                $('#foot-bar').prepend(button);
                button.click(function(){
                    __submit_form(iframeID , componentModel.formId , componentModel.tenantId);
                });
            }
        } else if(componentModel.activityInstName){
            if(componentModel.componentModelURL){
                //若URL为空，不显示area
                if(!areaName){
                    areaName = componentModel.activityInstName + ' , ' + componentModel.operUser + ' , ' + componentModel.operTime + ' , ' + (componentModel.operDesc ? decodeURI(componentModel.operDesc) : '...');
                }
                __componentDiv.append('<div class="btn-conponentA" onclick="__openComponent(this , '+iframeID+')" formDataId="'+componentModel.formDataId+'" componentModelURL="'+componentModel.componentModelURL+'"><span class="glyphicon glyphicon-plus"></span>' + areaName + '</div>');
            }
        }
    }

    //展开第一个area
    var firstCompont = '';
    if(expandFirst && (firstCompont = $($('.btn-conponentA')[0]))){
        firstCompont.trigger('click');
    }

}



function __submit_form(iframeID , formId , tenantId){
    $('#' + iframeID)[0].contentWindow.saveFormData(function(returnData){
        console.info(returnData);
        _winParams.formData = returnData;
        _winParams.formId = formId;
        _winParams.tenantId = tenantId;
        _winParams.globalUniqueID = '${globalUniqueID}';

        __show_metar_loading();

        $.ajax({
            url: _PATH + '/workOrderController.do?method=submit',
            type: 'POST',
            async: true,
            dataType: 'json',
            data: _winParams,
            success: function (response) {
                if(response.success){
                    alert('提交成功');
//                    __exitFromFrame();
//                    location.href = '/uflow/task/businessCenter.do';
                    location.href = _PATH + '/base/page/todo.jsp';
                } else {
                    alert('提交失败，请重试');
                    __hide_metar_loading();
                }

            },
            error: function(){
                alert('提交失败，请重试');
                __hide_metar_loading();
            }
        })
    });
}

function __setFrameHeight(iframeID , height){
    if(iframeID == '__edit_iframe_id'){
//        height += 50;
    }
//    alert(iframeID + ',' + height);
//    var maxHeight = document.documentElement.clientHeight - 170;
//    if(height > maxHeight){
//        height = maxHeight;
//    }
    $('#' + iframeID).height(height);
    __hide_metar_loading();
}

function __hideFrameSysBtn(){
    $('#foot-bar').remove();
    $('#__link_bar').show();
    $('#__page_big_title').css({marginTop : '40px'});
}

function __openComponent(componentA , iframeID){
    var componentModelURL = $(componentA).attr('componentModelURL');
    var componentModel = $('#' + iframeID);
    if(componentModel.length > 0){
//        alert(componentModel.css('display'));
        if(componentModel.css('display') == 'none'){
            $(componentA).css('fontWeight' , 'bold');
            $(componentA).find('span').removeClass('glyphicon-plus').addClass('glyphicon-minus');
        } else {
            $(componentA).css('fontWeight' , '');
            $(componentA).find('span').removeClass('glyphicon-minus').addClass('glyphicon-plus');
        }
        componentModel.slideToggle();
        return;
    }
    __show_metar_loading();
    if(componentModelURL.indexOf('?') > -1){
        componentModelURL += '&';
    } else {
        componentModelURL += '?';
    }
    componentModelURL += 'formDataId='+$(componentA).attr('formDataId');

    componentModelURL += '&iframeID='+iframeID;
    $(componentA).parent().append('<iframe id="'+iframeID+'" style="height:0;" class="component-frame" frameborder="0" src="'+componentModelURL+'"></iframe>');
    $(componentA).css('fontWeight' , 'bold');
    $(componentA).find('span').removeClass('glyphicon-plus').addClass('glyphicon-minus');
}

function __showWorkFlowMonitor(){
    $('#workMonitorWin').modal('show');
    $('#workMonitorWinBodyFrame')[0].src = _PATH + '/commGrapMonitor.do?rootProcessInstId=' + _winParams.rootProcessInstId + '&jobID=' + _winParams.jobID;
}

function __showOldMonitor(){
    $('#workMonitorWin').modal('show');
    $('#workMonitorWinBody').css({paddingTop : '30px'});
    $('#workMonitorWinBody').load('/EOM_TW/base/page/workFlowMonitor.jsp?rootProcessInstId=' + _winParams.rootProcessInstId + '&jobID=' + _winParams.jobID);
}

</script>
</body>
</html>
