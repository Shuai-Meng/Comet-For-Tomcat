<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 17-5-4
  Time: 上午12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/validatebox.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
</head>
<body class="easyui-layout" style="overflow:auto">
    <div region="west" style="left: 0px; width: 214px;" class="easyui-layout" split="true" title="menu">
        <div class="easyui-accordion" fit="true" style="overflow:auto;width:193px">
            <button id="auth">权限管理</button><br>
            <button id="type">消息类别管理</button><br>
            <button id="message">消息管理</button><br>
            <button id="subscribe">订阅管理</button>
        </div>
    </div>

    <div region="center">
        <div id="tabs" class="easyui-tabs" data-options="fit:true">
        </div>
    </div>

    <!-- 选项卡模板 -->
    <div id="dirTab">
        <!-- 搜索框 --><br>
        <div class="searchBar" align="center">
            <strong style="color:green;font-size:19px;">搜索：</strong>
            <input name="key"/>
            <div class="searchMenu">
                <div data-options="name:'ROLE_PUB'">发布者</div>
                <div data-options="name:'ROLE_SUB'">申请者</div>
                <div data-options="name:'all'">全部</div>
            </div>
        </div><br>
        <!-- 列表 -->
        <table></table>
    </div>


    <script>
    (function(obj) {
        $(function () {
            $("#auth").click(generateTabForAuth);
            $("#type").click(generateTab("type", "type"));
        });

        function generateTabForAuth() {
            if (obj.$tabs.tabs('exists', "auth")) {
                obj.$tabs.tabs('select', "auth");
                return;
            }

            var $div = $("#dirTab").clone(true);
            $div.attr("id", "auth");

            obj.$tabs.tabs('add', {
                title: '权限管理',
                content: $div[0],
                closable: true
            });

            var json = {};
            var $table = $div.find('table');
            $div.find('input').searchbox({
                width:300,
                prompt:'请输入标题关键字',
                menu: $div.find('.searchMenu'),
                searcher: function(key, value) {
                    json['key'] = key;
                    json['type'] = value;
                    $table.datagrid('load', json);
                },
            });


            var column = new Array({field:'id',title:'编号',width:100,align:'left'},
                    {field:'name',title:'名称',width:200},
                    {field:'role',title:'角色',width:120},
                    {field:'operation',title:'操作',width:160, align:'center',
                        formatter:function(value,row){
                            var s = "";
                            var pre = '<input type="button" class = "handle"';
                            if(row.role == 'ROLE_PUB')
                                s = pre + 'name="degrade" value="降级"/>';
                            if(row.role == 'ROLE_SUB' && row.flag == '1') {
                                s = pre + 'name="agree" value="同意"/>';
                                s += ' ' + pre + 'name="refuse" value="拒绝"/>';
                            }
                            return s;
                        }
                    });
            generateTable($table, json, column, handleAuth);
        }

        function generateTable($table, url, json, column, handle) {
            $table.datagrid({
                width: 'auto',
                height: 'auto',
                fitColumns: true,
                striped: true,
                pageSize: 10,
                pagination: true,
                pageList: [10,15],
                singleSelect: true,
                url: url,
                queryParams: json,
                columns:[column],
                onLoadSuccess: function(data) {
                    $(".handle").click(function() {
                        var row = $table.datagrid('getSelected');
                        if(row == null)
                            return;//interesting, auto selected

                        handle(row, this.name);//name? field?
                        $table.datagrid('load', json);
                    });
                }
            });
        }

        function handleAuth(row, flag) {
            var json = {};
            json["id"] = row.id;

            if(flag == "degrage")
                json["role"] = "ROLE_SUB";
            else if(flag == "agree")
                json["role"] = "ROLE_PUB";
            else {
                json["role"] = "ROLE_SUB";
                json["flag"] = "0";
            }

            $.ajax({
                url: obj.contextPath + "/modifyAuth",
                type: 'post',
                data: json,
                success: function (data) {
                    $.messager.alert('info', obj.message.actionSuccess);
                },
                error: function() {
                    $.messager.alert('info', obj.message.actionFail);
                }
            });
        }

        function generateTab(name, id) {
            if (obj.$tabs.tabs('exists', id)) {
                obj.$tabs.tabs('select', id);
                return;
            }

            var $div = $("#dirTab").clone(true);
            $div.attr("id", id);

            obj.$tabs.tabs('add', {
                title: name,
                content: $div[0],
                closable: true
            });

            var json = {};
            var $table = $div.find('table');
            $div.find('input').searchbox({
                width:300,
                prompt:'请输入关键字',
                menu: $div.find('.searchMenu'),
                searcher: function(name, value) {
                    json['name'] = name;
                    json['role'] = value;
                    $table.datagrid('load', json);
                },
            });

            if(id != 'auth')
                $div.remove(".searchMenu");

            var column = new Array({field:'id',title:'编号',width:100,align:'left'},
                    {field:'name',title:'名称',width:200},
                    {field:'operation',title:'操作',width:160, align:'center',
                        formatter:function(value,row){
                            var s = '<input type="button" class = "handle" name="delete" value="delete"/>';
                            var p = '<input type="button" class = "handle" name="modify" value="modify"/>';
                            return s + " " + p;
                        }
                    });

            generateTable($table, obj.contextPath + "/getMessageType",json, column, handleType);
        }

        function handleType(row, flag) {
            //TODO
            $.ajax({
                url: obj.contextPath + "/modifyType",
                type: 'post',
                data: {"id": row.id, "name": row.name, "operation": flag},
                success: function (data) {
                    $.messager.alert('info', obj.message.actionSuccess);
                },
                error: function() {
                    $.messager.alert('info', obj.message.actionFail);
                }
            });
        }

    })({
        contextPath: "<%=request.getContextPath()%>/manage",
        $tabs: $("#tabs"),
        message: {
            actionFail: "操作失败，请重试！",
            actionSuccess: "操作成功！",
            remove: "确定删除？",
        }
    });
    </script>
</body>
</html>
