/**
 * 初始化当前页面按钮
 * @param btnID
 * @param btnName
 * @param button
 * @param tenantId
 * @param globalUniqueID
 */
function initButton(btnID , btnName , button , tenantId , globalUniqueID){

    $.ajax({
        url: _PATH + '/flowButtonFindById.do',
        type: 'POST',
        async: true,
        dataType: 'json',
        data: { id : btnID },
        success: function (response) {
            if(response.id == btnID){
//                    var button = $('<button id="btn-submit" type="button" class="btn btn-danger" style="margin-left:5px;margin-right:5px;">'+btnName+'</button>');
//                    $('#foot-bar').append(button);
                if(response.buttonType == 1){
                    //后台按钮
                    button.click(function(){
                        __submit_form();
                    });
                } else if(response.buttonType == 2){
                    //弹出云表单
                    button.click(function(){
                        showWindow(btnID + '_' + response.cloudFormID , btnName , function(modal_body , modal_footer){
                            modal_body.empty().append('<iframe id="custom_form_container_'+btnID+'_'+response.cloudFormID+'" style="width:100%;" frameborder="0" src="/cform/jsp/cform/tasklist/list/add.jsp?disSaveBtn=0&formId='+response.cloudFormID+'&tenantId='+tenantId+'&globalUniqueID='+globalUniqueID+'&iframeID=custom_form_container_'+btnID+'_'+response.cloudFormID+'"></iframe>');
                            var button = $('<button id="btn-submit" type="button" class="btn btn-danger" style="margin-left:5px;margin-right:5px;">提交</button>');
                            modal_footer.prepend(button);
                            button.click(function(){
                                __submit_form('custom_form_container_'+btnID+'_'+response.cloudFormID , response.cloudFormID , tenantId);
                            });
                        });
                    });
                } else if(response.buttonType == 3){
                    //弹出自行开发表单
                    $.ajax({
                        url: _PATH + '/componentFormFindById.do',
                        type: 'POST',
                        async: true,
                        dataType: 'json',
                        data: {id: response.cusDevFromID},
                        success: function (response) {
                            var reqURL = response.pcEditURL;
                            if(reqURL.indexOf("?") > -1){
                                reqURL += "&";
                            } else {
                                reqURL += "?";
                            }
                            reqURL += "formId=" + response.id;
                            for(var pro in _winParams){
                                reqURL += "&" + pro + "=" + _winParams[pro];
                            }
                            reqURL += "&iframeID=custom_form_container_" + btnID + '_' +  response.id;
                            reqURL += "&globalUniqueID=" + _globalUniqueID;

                            button.click(function(){
                                showWindow(btnID + '_' + response.cloudFormID , btnName , function(modal_body){
                                    modal_body.empty().append('<iframe id="custom_form_container_'+btnID+'_'+response.id+'" style="width:100%;" frameborder="0" src="'+reqURL+'"></iframe>');
                                });
                            });
                        }
                    });
                }
            }
        },
        error: function(){ }
    })
}


/**
 *
 * @param elementId 触发交互元素的ID
 * @param title 交互弹窗标题
 * @param iframeID  交互来源iframeID
 * @param globalUniqueID    交互来源globalUniqueID
 * @param type  交互类型：1、orgtree,组织树；2、pertree,人员树；
 */
function showInterActiveWindow(elementId , title , iframeID , globalUniqueID , type){

    var iframeDocument = $($('#' + iframeID)[0].contentDocument);

    var elementID = $('#' + elementId + '_ID' , iframeDocument);
    var element = $('#' + elementId , iframeDocument);

    var treeType = '';
    if(type == 1){
        treeType = 1;
    } else if(type == 2){
        treeType = 2;
    }

    if(treeType != ''){

        __open_tree(iframeID + elementId , treeType, title, function(values){

            var ids = '';
            var labels = '';

            for(var i = 0 ; i < values.length ; i++){
                var value = values[i];
                ids += value.id + ',';
                labels += value.label + ',';
            }

            if(ids != ''){
                ids = ids.substr(0 , ids.length - 1);
                labels = labels.substr(0 , labels.length - 1);
            }

            elementID.val(ids);
            element.val(labels);
        }, '', '', '', '');
    } else {
        showWindow(iframeID + '-' + elementId , title , function(modal_body){

            var parameters = 'iframeID=iframe-' + iframeID + '-' + elementId + '&globalUniqueID=' + globalUniqueID;
            var openURL = element.attr('openurl');
            if(openURL.indexOf('?') == -1){
                openURL += '?';
            } else {
                openURL += '&';
            }
            openURL += parameters;

            modal_body.empty().append('<iframe id="iframe-' + iframeID + '-' + elementId +'" style="width:100%;" frameborder="0" src="' + openURL + '"></iframe>');
        });
    }
}

/**
 * 弹出模态窗口
 * @param id
 * @param title
 * @param callback
 */
function showWindow(id , title , callback){

    if($('#' + id + 'modal').length > 0){
        $('#' + id + 'modal').modal('show');
        return;
    }

    var modal = $('<div class="modal fade" id="'+id+'modal" tabindex="-1" role="dialog" aria-labelledby="'+id+'label" aria-hidden="true"></div>');
    var modal_dialog = $('<div class="modal-dialog" style=""></div>');
    var modal_content = $('<div class="modal-content"></div>');
    var modal_header = $('<div class="modal-header"></div>');
    var modal_close = $('<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>');
    var modal_title = $('<h4 class="modal-title" id="'+id+'label">'+title+'</h4>');
    var modal_body = $('<div class="modal-body"></div>');
    var modal_footer = $('<div class="modal-footer"><button type="button" class="btn btn-default" data-dismiss="modal">关闭</button></div>');

    modal_header.append(modal_close).append(modal_title);
    modal_content.append(modal_header).append(modal_body).append(modal_footer);
    modal_dialog.append(modal_content);
    modal.append(modal_dialog);
    modal.modal('show');
    if(callback){
        callback(modal_body , modal_footer);
    }
}

/**
 * 向iframe所在的模态窗口下方添加按钮
 * @param iframeID
 * @param button
 */
function addButton(iframeID , button){

    iframeID = iframeID.replace('iframe-' , '');

    var modelWindow = $('#' + iframeID + 'modal');
    var footer = modelWindow.find('.modal-footer');
    var btn = $('<button type="button" class="btn btn-danger" id="'+button.id+'">'+button.label+'</button>');
    footer.prepend(btn);
    btn.click(function(){
        var value = button.click();
        if(value.label){

            var splitIndex = iframeID.indexOf('-');
            var iframeId = iframeID.substr(0 , splitIndex);
            var elementId = iframeID.substr(splitIndex + 1 , iframeID.length);

            var iframeDocument = $($('#' + iframeId)[0].contentDocument);

            var elementID = $('#' + elementId + '_ID' , iframeDocument);
            var element = $('#' + elementId , iframeDocument);

            elementID.val(value.id);
            element.val(value.label);

            modelWindow.modal('hide');

        }

    })
}

function showModalWindow(){
    layer.open({
        type: 2,
        content: 'http://baidu.com', //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        btn: ['按钮一', '按钮二', '按钮三'],
        maxmin:true
    });
}