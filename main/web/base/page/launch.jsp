<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
//    String formId = request.getParameter("formId");
//    String tenantId = request.getParameter("tenantId");
//    String processModelName = request.getParameter("processModelName");
%>
<%@ include file="/base/basePage.jsp" %>
<head>
    <title>发起工单</title>
    <style>

    </style>
</head>
<body>
<iframe id="custom_form_container" style="width:100%;" frameborder="0" src=""></iframe>

<div id="foot-bar" style="text-align:center;padding:10px;">
    <%--<button id="btn-submit" type="button" class="btn btn-danger">提交</button>--%>
</div>
<script type="text/javascript" src="<%=path%>/base/js/common.js"></script>
<script type="text/javascript">
    $(document).ready(function() {

        var btnIDs = '${launchWorkOrderEntity.btnIDs}';
        var btnNames = '${launchWorkOrderEntity.btnNames}';
        var tenantId = '${launchWorkOrderEntity.tenantId}';
        var customFormID = '${launchWorkOrderEntity.customFormID}';
        var formType = '${launchWorkOrderEntity.formType}';

        var iframeURL = '';
        if(formType == 4){
            $.ajax({
                url: _PATH + '/componentFormFindById.do',
                type: 'POST',
                async: true,
                dataType: 'json',
                data: {id: customFormID},
                success: function (response) {
                    var iframeURL = response.pcEditURL;
                    if(iframeURL.indexOf("?") > -1){
                        iframeURL += "&";
                    } else {
                        iframeURL += "?";
                    }
                    iframeURL += "formId=" + response.id;
                    iframeURL += "&iframeID=custom_form_container&globalUniqueID=" + _globalUniqueID;
                    $('#custom_form_container')[0].src = iframeURL;
                }
            });
        } else {
            iframeURL = '/cform/jsp/cform/tasklist/list/add.jsp?disSaveBtn=0&formId='+
            customFormID+'&tenantId='+tenantId+'&globalUniqueID='+_globalUniqueID+'&iframeID=custom_form_container';
            $('#custom_form_container')[0].src = iframeURL;
        }

        if(btnIDs != ''){
            var btnIDArray = btnIDs.split(',');
            var btnNamesArray = btnNames.split(',');

            for(var i = 0 ; i < btnIDArray.length ; i++){

                var button = $('<button id="btn-submit" type="button" class="btn btn-danger" style="margin-left:5px;margin-right:5px;">'+btnNamesArray[i]+'</button>');
                $('#foot-bar').append(button);

                initButton(btnIDArray[i] , btnNamesArray[i] , button , tenantId , globalUniqueID);
            }
        } else {
            var button = $('<button id="btn-submit" type="button" class="btn btn-danger" style="margin-left:5px;margin-right:5px;">提交</button>');
            $('#foot-bar').append(button);
            button.click(function(){
                __submit_form();
            });
        }

    });

    function __submit_form(){

        $('#custom_form_container')[0].contentWindow.saveFormData(function(returnData){

            if(!returnData){
                alert('数据保存失败，请重试');
                return;
            }

            __show_metar_loading();

            $.ajax({
                url: _PATH + '/workOrderController.do?method=launch',
                type: 'POST',
                async: true,
                dataType: 'json',
                data: {formData : returnData , formId : '${launchWorkOrderEntity.customFormID}', tenantId : '${launchWorkOrderEntity.tenantId}', processModelName : '${launchWorkOrderEntity.processModelName}'},
                success: function (response) {
                    if(response.success){
                        alert('提交成功');
//                        location.href = '/uflow/task/businessCenter.do';
                        location.href = _PATH + '/base/page/launchList.jsp';
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


    /**
     * 设置iframe高度
     * @param iframeID
     * @param height
     * @private
     */
    function __setFrameHeight(iframeID , height){
//        alert(iframeID + ',' + height);
        $('#' + iframeID).height(height);
    }


    function __hideFrameSysBtn(){
        $('#foot-bar').remove();
    }


</script>
</body>
</html>
