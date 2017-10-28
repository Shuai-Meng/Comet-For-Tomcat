<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 17-5-4
  Time: 上午12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<html>
<head>
    <title>管理页面</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/validatebox.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
    <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/jquery.easyui.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/easyui-lang-zh_CN.js"></script>
    <script src="<%= request.getContextPath()%>/js/comet.js"></script>
    <script src="<%= request.getContextPath()%>/js/admin.js"></script>
</head>
<body class="easyui-layout" style="overflow:auto">
    <div region="west" style="left: 0px; width: 214px;" class="easyui-layout" split="true" title="菜单">
        <div class="easyui-accordion" fit="true" style="overflow:auto;width:193px">
            <button id="authbutton" class="mainmenu">权限管理</button><br>
            <button id="typebutton" class="mainmenu">消息类别管理</button><br>
            <button id="messagebutton" class="mainmenu">消息管理</button><br>
            <button id="subscribebutton" class="mainmenu">订阅管理</button><br>
        </div>
    </div>

    <div region="center">
        <div id="tabs"></div>
    </div>

    <span id="userName" style="display: none">${userName}</span>

    <!-- 选项卡模板 -->
    <div id="dirTab">
        <!-- 搜索框 --><br>
        <div class="searchBar" align="center">
            <strong style="color:green;font-size:19px;">搜索：</strong>
            <input name="key"/>
            <div id="roleMenu" style="display: none">
                <div data-options="name:'ROLE_PUB'">发布者</div>
                <div data-options="name:'ROLE_SUB'">申请者</div>
                <div data-options="name:'all'">全部</div>
            </div>
        </div><br>
        <!-- 列表 -->
        <table></table>
    </div>

    <div id="msgEdit" style="display: none">
        <form>
            <strong style="font-size:110%;color:green;padding:2px">标题</strong>
            <input type="text" name="title" style="width:300px;margin:3px"/><br>

            <strong style="font-size:110%;color:green;padding:2px">消息类别</strong>
            <input name="type">&nbsp;&nbsp;

            <strong style="font-size:110%;color:green;padding:2px">推送方式</strong>
            <input name="immediate"/>&nbsp;&nbsp;

            <span class = 'sendTime' style="font-size:110%;color:green;padding:2px;display:none">
                <strong>推送时间</strong>
                <input name='sendTime' style="width:160px; display:none"/>
            </span>

            <textarea name="content" rows="20" cols="150"></textarea><br>
        </form>
        <button name="submit">发布</button>
    </div>
</body>
</html>
