<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
            "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>comet</title>
    <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/jquery.easyui.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/comet.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/validatebox.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
</head>
<body>
	<input type="text" id="send">
	<button id='co'>提交</button>
<script>
	$(function() {
		$("#co").click(function () {
            console.log("clicked");
        });
	});
</script>
</body>
</html>