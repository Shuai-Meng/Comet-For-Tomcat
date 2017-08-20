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
    <title>管理页面</title>
    <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/comet.js"></script>
    <script src="<%= request.getContextPath()%>/js/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/validatebox.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
</head>
<body class="easyui-layout" style="overflow:auto">
    <div region="west" style="left: 0px; width: 214px;" class="easyui-layout" split="true" title="菜单">
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

            <%--<strong style="font-size:110%;color:green;padding:2px">目标用户</strong>--%>
            <%--<input type="text" name="target"><br>--%>

            <strong style="font-size:110%;color:green;padding:2px">推送方式</strong>
            <input name="method"/>&nbsp;&nbsp;
            <input name='sendTime' style="width:160px; display:none"/>

            <textarea name="content" rows="20" cols="150"></textarea>
        </form>
        <div>
            <button>发布</button>
        </div>
    </div>

    <script>
    (function(obj) {
        $(function () {
            $("#auth").click(function() {
                var column = new Array({field:'id',title:'编号',width:100,align:'left'},
                        {field:'name',title:'名称',width:200},
                        {field:'role',title:'角色',width:120},
                        {field:'operation',title:'操作',width:160, align:'center',
                            formatter:function(value,row){
                                var s = "";
                                var pre = '<input type="button" class = "handle"';
                                if(row.role == 'ROLE_PUB')
                                    s = pre + 'name="degrade" value="降级"/>';
                                if(row.role == 'ROLE_SUB' && row.whetherApplying == '1') {
                                    s = pre + 'name="agree" value="同意"/>';
                                    s += ' ' + pre + 'name="refuse" value="拒绝"/>';
                                }
                                return s;
                            }
                        });
                generateTab("权限管理", "auth", column);
            });

            $("#type").click(function() {
                var column = new Array({field:'id',title:'编号',width:100,align:'left'},
                        {field:'name',title:'名称',width:200},
                        {field:'operation',title:'操作',width:160, align:'center',
                            formatter:function(value,row){
                                var s = '<input type="button" class = "handle" name="delete" value="删除"/>';
                                var p = '<input type="button" class = "handle" name="modify" value="修改"/>';
                                return s + " " + p;
                            }
                        });
                generateTab("消息类别管理", "type", column);
            });

            $("#message").click(function() {
               var column = new Array({field:'id',title:'编号',width:100,align:'left'},
                       {field:'title',title:'title',width:200},
                       {field:'type',title:'type',width:200},
                       {field:'valid',title:'valid',width:200},
                       {field:'operation',title:'操作',width:160, align:'center',
                           formatter:function(value,row){
                               var s = '<input type="button" class = "handle" name="delete" value="删除"/>';
                               var p = '<input type="button" class = "handle" name="modify" value="修改"/>';
                               return s + " " + p;
                           }
                       }
               );
                generateTab("消息管理", "message", column);
            });

            $("#subscribe").click(function() {
                var column = new Array({field:'id',title:'编号',width:100,align:'left'},
                        {field:'name',title:'名称',width:200},
                        {field:'operation',title:'操作',width:160, align:'center',
                            formatter:function(value,row){
                                if(value == "1")
                                    return '<input type="button" class = "handle" name="sub" value="订阅"/>';
                                else
                                    return '<input type="button" class = "handle" name="desub" value="取消订阅"/>';
                            }
                        });
                generateTab("订阅管理", "subscribe", column);
            });
        });

        function generateTab(name, id, column) {
            if (obj.$tabs.tabs('exists', id)) {
                alert("exist")
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
            var $menu = null;

            var url = obj.contextPath + "/getMessage";
            if(id == "auth") {
                url = obj.contextPath + "/getUsers";
                $menu = '#roleMenu';
            } else if(id == "type")
                url = obj.contextPath + "/getMessageType";
            else if(id == "subscribe")
                url = obj.contextPath + "/getSubscribeType";

            $div.find('input').searchbox({
                width:300,
                prompt:'请输入关键字',
                menu: $menu,
                searcher: function(key, value) {
                    json['key'] = key;
                    if(id == "auth")
                        json['role'] = value;

                    $table.datagrid('load', json);
                },
            });
            generateTable($table, url, json, column, id);
        }

        function generateTable($table, url, json, column, id) {
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
                toolbar:[ {
                    text:'add',
                    iconCls:'icon-add',
                    handler: function() {
                        //alert(id)
                        if(id == "type")
                            editType();
                        else if(id == "message")
                            editMessage(0, "new");
                    }
                }],
                onLoadSuccess: function(data) {
                    $(".handle").click(function() {
                        var row = $table.datagrid('getSelected');
                        if(row == null)
                            return;//interesting, auto selected

                        if(whetherApplying == "auth")
                            handleAuth(row, this.name);//name? field?
                        else if(whetherApplying == "type")
                            handleType(row, this.name);
                        else if(whetherApplying == "message")
                            handleMessage(row, this.name);
                        else
                            handleSubscribe(row, this.name);

                        $table.datagrid('load', json);
                    });
                },
                onDblClickRow: function(rowIndex, rowData) {
                    if(whetherApplying == "message") {

                    }
                }
            });
        }

        function editType() {}

        function handleAuth(row, whetherApplying) {
            var json = {};
            json["id"] = row.id;

            if(whetherApplying == "degrage")
                json["role"] = "ROLE_SUB";
            else if(whetherApplying == "agree")
                json["role"] = "ROLE_PUB";
            else {
                json["role"] = "ROLE_SUB";
                json["whetherApplying"] = "0";
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

        function handleType(row, whetherApplying) {
            //TODO
            $.ajax({
                url: obj.contextPath + "/modifyType",
                type: 'post',
                data: {"id": row.id, "name": row.name, "operation": whetherApplying},
                success: function (data) {
                    $.messager.alert('info', obj.message.actionSuccess);
                },
                error: function() {
                    $.messager.alert('info', obj.message.actionFail);
                }
            });
        }

        function handleMessage(row, whetherApplying) {
            $.ajax({
                url: obj.contextPath + "/modifyMessage",
                type: 'post',
                data: {"id": row.id, "name": row.name, "operation": whetherApplying},
                success: function (data) {
                    $.messager.alert('info', obj.message.actionSuccess);
                },
                error: function() {
                    $.messager.alert('info', obj.message.actionFail);
                }
            });
        }

        function handleSubscribe(row, whetherApplying) {
            $.ajax({
                url: obj.contextPath + "subscribe",
                type: 'post',
                data: {"typeId": row.id, "operation": whetherApplying},
                success: function(data) {

                },
                error: function(data) {

                }
            })
        }

        function editMessage(id, operation) {
            /*if (obj.$tabs.tabs('exists', id)) {
                obj.$tabs.tabs('select', id);
                return;
            }*/

            var $div = $("#msgEdit").clone(true);
            $div.attr("id", id).show();

            obj.$tabs.tabs('add', {
                title: id,
                content: $div[0],
                closable: true
            });

            $div.find('input[name="type"]').combobox({
                url: obj.contextPath + "/getMessageTypes",
                valueField: 'id',
                textField: 'name',
                onSelect: function(data) {
//                    $div.find('input[name="target"]').combobox('reload', obj.contextPath + "/getUser?type=" + data.id);
                },
                editable: false
            });

            $div.find('input[name="sendTime"]').datetimebox({
                required: true,
                showSeconds: false
            });

            $div.find('input[name="method"]').combobox({
                data: [{label: '立即推送', 'id': '1'},
                    {label: '定时推送', 'id': '2'},
                ],
                valueField: 'id',
                textField: 'label',
                onSelect: function(data) {
                    if(data.id == '2') {
                        $div.find('input[name="sendTime"]').show();
                    }
                },
                required: true
            });

            $div.find('button').click(function() {
                $.ajax({
                    url:obj.contextPath + "/addMessage",
                    type: 'post',
                    data: $div.find('form').serialize(),
                    success: function() {

                    },
                    error: function() {

                    }
                })
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
