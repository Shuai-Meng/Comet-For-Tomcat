<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 17-5-4
  Time: 上午12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="<%request.getContextPath()%>/js/jquery.min.js"/>
    <script src="<%request.getContextPath()%>/js/jquery.easyui.min.js"/>
</head>
<body class="easyui-layout" style="overflow:auto">
    <div region="west" style="left: 0px; width: 214px;" class="easyui-layout" split="true" title="menu">
        <div class="easyui-accordion" fit="true" style="overflow:auto;width:193px">

        </div>
    </div>

    <div region="center">
        <div id="tabs" class="easyui-tabs" data-options="fit:true">
        </div>
    </div>
</body>
</html>
