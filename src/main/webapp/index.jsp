<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
            "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>comet</title>
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
</head>
<body>
	<input type="text" id="send">
	<button id='co'>提交</button>
<script>
	var root="<%=request.getContextPath()%>";
	$(function() {
		comet();
		$("#co").click(send);
	});
	
	function comet() {
		$.get(root+"/test", function(data) {
			console.log("data: "+data);
			comet();
		});
	}
	
	function send() {
		$.post(root+"/manage/add", {'msg':$('#send').val()});
	}
</script>
</body>
</html>