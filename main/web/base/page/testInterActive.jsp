<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" >
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<jsp:include page="../basePage.jsp"/>
<%
    String iframeID = request.getParameter("iframeID");
    String globalUniqueID = request.getParameter("globalUniqueID");
%>
<html>
<head>
    <style>
        .table th , .table td { height:40px;}
        .table > thead > tr > th, .table > tbody > tr > td, .table > tfoot > tr > td { padding:8px;border-left:0;border-right:0; }

    </style>
</head>
<body>
<table class="table">
    <thead>
    <tr class="active">
        <th></th>
        <th>ID</th>
        <th>LABEL</th>
    </tr>

    </thead>
    <tbody>
    <%for(int i = 0 ; i < 10 ; i++){ %>
    <tr onclick="select(this)">
        <td><input type="checkbox" name="selectValue" value="<%=i%>"/></td>
        <td><span><%=i%></span></td>
        <td><%="显示名称" + i%></td>
    </tr>
    <% }%>
    </tbody>
</table>
<script>
    var iframeID = '<%=iframeID%>';
    var globalUniqueID = '<%=globalUniqueID%>';

    $(document).ready(function(){
        var button = {
            id : '',
            label : '选择',
            click : function(){

                var ids = '';
                var labels = '';
                var checkboxs = $(':checkbox');
                for(var i = 0 ; i < checkboxs.length; i++){
                    var checkbox = $(checkboxs[i]);
                    var checked = checkbox.attr('checked');
                    if(checked == 'checked'){
                        ids += checkbox.val() + ',';
                        labels += '显示名称' + checkbox.val() + ',';
                    }
                }

                if(ids != ''){
                    ids = ids.substr(0 , ids.length - 1);
                    labels = labels.substr(0 , labels.length - 1);
                }

                return { id : ids , label : labels };
            }
        }
        parent.addButton(iframeID , button);
        var iframeHeight = document.body.scrollHeight > document.documentElement.clientHeight ? document.body.scrollHeight : document.documentElement.clientHeight;
        parent.__setFrameHeight(iframeID , iframeHeight);
    });

    function select(tr){
        var checkbox = $(tr).find(':checkbox');
        var checked = checkbox.attr('checked');
        if(checked != 'checked'){
            checkbox.attr('checked' , 'true');
            $(tr).addClass('active');
        } else {
            checkbox.removeAttr('checked');
            $(tr).removeClass('active');
        }
    }

</script>
</body>
</html>
