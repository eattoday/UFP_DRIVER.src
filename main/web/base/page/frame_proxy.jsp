<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/base/basePage.jsp" %>

<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
<h2>代理孙页面</h2>

<script type="text/javascript">
    // 这里需要轮询，当检测到search改变的时候，也就是有search的时候，触发事件，并结束轮询
    var timer = setInterval(function() {
        // 这个判断是关键，可以做的更有细节一点 ；目前查询到的 search 是 '?data=1'
        if(window.location.search) {
            clearInterval(timer)
            timer = null
            console.log('search',window.location.search)
            console.log('grandparent',window.parent)
            console.log('grandparent',window.parent.parent)
            console.log('grandparent',window.parent.parent.parent)
            console.log('grandparent',window.parent.parent.parent.parent)
            console.log('grandparent',window.parent.parent.parent.parent.parent)
            //通过 window.top 获取到主页面，然后拿获取主页面的元素，之后可以用类似 pph2.click() 来触发主页面写好的事件
            var return2Todo = window.parent.parent.document.getElementById('return2Todo')
            return2Todo.click()
        }
    },50)
</script>
</body>
</html>
