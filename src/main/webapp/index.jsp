<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>主界面</title>
</head>
<body>
欢迎:xxxxxx
	
	<textarea rows="5" cols="6" id="val">
		输入消息
	</textarea>
<input type="button" value="Send" onclick="send();"/>
<input type="button" value="Close" onclick="closed();"/><br/>

接受消息框
	<textarea rows="10" cols="15" id="receive">
		
	</textarea>


<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.11.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/websocket_util.js"></script>
<script type="text/javascript">

    var url = "ws://localhost:8080/${pageContext.request.contextPath}/webSocketServer";
    var ws = WebSocketUtil.getWs(url);
    WebSocketUtil.onopen(ws);
    ws.onmessage = function (event) {
        var receiveMsg = $("#receive").val();
        $("#receive")[0].value = receiveMsg + event.data;
    };

    function send() {
        var value = $("#val").val();
        WebSocketUtil.sendMsg(ws, value);
    }
    function closed() {
        console.log("close ws...");
        WebSocketUtil.onclose(ws);
    }
</script>
</body>
</html>