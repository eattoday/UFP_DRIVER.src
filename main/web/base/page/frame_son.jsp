<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>


<h2>子页面</h2>



<p>
    <button id="btn">点击传递数据给孙页面</button>
</p>
<!--这个firame实际上可以隐藏 display:none -->
<iframe id="ifr" style="display: none"  width="200" height="100"
        src="http://localhost:9087/UFP_DRIVER/base/page/frame_proxy.jsp"></iframe>

<script type="text/javascript">
    var btn = document.getElementById('btn')
    var ifr = document.getElementById('ifr')

    // 通过改变 ？后面的search，来使最下面的页面触发
    btn.onclick = function() {
        ifr.src = 'http://localhost:9087/UFP_DRIVER/base/page/frame_proxy.jsp?data=1'
    }
</script>


</body>
</html>
