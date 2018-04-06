/**
 * Created by mengshuai on 2017/10/23.
 */
(function(obj) {
    $(function () {
        $("#tabs").tabs({
            fit:true,
            toolPosition: 'right',
            tools: [
                {
                    iconCls: 'icon-man',
                    text: 'welcome: ' + username
                },
                {
                    iconCls: 'icon-no',
                    text: 'logout',
                    handler: function () {
                        window.location = "/comet/logout";
                    }
                }
            ]
        });

        if (userRole == "ROLE_PUB" || userRole == "ROLE_SUB") {
            $("#authbutton").remove();
            $("#typebutton").remove();
        }

        $(".mainmenu").click(function () {
            var id = $(this).attr("id").replace("button", "");
            var column = null;
            var url = "";

            switch (id) {
                case "auth":
                    column = obj.authColumn;
                    url = obj.contextPath + "/getUsers";
                    break;
                case "type":
                    column = obj.typeColumn;
                    url = obj.contextPath + "/getMessageType";
                    break;
                case "message":
                    column = obj.messageColumn;
                    url = obj.contextPath + "/getMessage";
                    break;
                case "subscribe":
                    column = obj.subscribeColumn;
                    url = obj.contextPath + "/getSubscribeType";
                    break;
                default:
            }

            generateTab($(this).text(), id, column, url);
        });
    });

    function addToPanel(title, content) {
        var $tabs = $("#tabs");
        if ($tabs.tabs('exists', title)) {
            $tabs.tabs('select', title);
            return;
        }

        $tabs.tabs('add', {
            title: title,
            content: content,
            closable: true
        });
    }

    function generateTab(name, id, column, url) {
        var $div = $("#dirTab").clone(true).attr("id", id);
        addToPanel(name, $div[0]);

        var $table = $div.find('table');
        var json = generateSearchBar($div, id);
        generateTable($table, json, column, id, url);
    }

    function generateSearchBar($div, id) {
        var $menu = null;
        var json = {};

        if (id == "auth") {
            $menu = '#menu_auth';
        } else if (id == "message") {
            json['type'] = 'sub';
            if (userRole == "ROLE_PUB") {
                $menu = '#menu_message';
            }
        }

        $div.find('input').searchbox({
            width:300,
            prompt:'请输入关键字',
            menu: $menu,
            searcher: function(key, value) {
                //兼容MyUser实体类，必须叫name
                json['name'] = key;
                if (id == "auth") {
                    json['role'] = value;
                } else if (id == "message") {
                    json['type'] = value;
                }

                $div.find('table.datagrid-f').datagrid('load', json);
            },
        });

        return json;
    }

    function generateTable($table, json, column, id, url) {
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
            toolbar: generateTableTools($table, id),
            onDblClickRow: function(index, row) {
                showMsgContent(id, row);
            },
            onClickRow: function (index, row) {
                if (obj.editing == true) {
                    $table.datagrid('selectRow', obj.rowIndex);
                    return;
                }
                row.editing = false;
                manageToolVisual(id, row);
            },
            onBeforeEdit:function(index,row){
                obj.editing = true;
                obj.rowIndex = index;
                row.editing = true;
                manageToolVisual(id, row);
                $table.datagrid('selectRow', index);
            },
            onAfterEdit:function(index,row){
                obj.editing = false;
                row.editing = false;
                manageToolVisual(id, row);
                $table.datagrid('refreshRow', index);
            },
            onCancelEdit:function(index,row){
                row.editing = false;
                obj.editing = false;
                manageToolVisual(id, row);
                row.id == '-1' ? $table.datagrid('deleteRow', index)
                    : $table.datagrid('refreshRow', index);
            }
        });

        var tools = $("#" + id).find('div.datagrid div.datagrid-toolbar a');
        tools.hide();
        if (id == "type") {
            tools.eq(0).show();
        } else if (id == "message" && userRole == "ROLE_PUB") {
            tools.eq(0).show();
        }
    }

    function generateTableTools($table, id) {
        var tools = new Array();

        if (id == "subscribe") {
            tools.push(generateToolElement('订阅', 'icon-add', function(){
                commonToolHandler(id, 'sub', $table);
            }));
            tools.push(generateToolElement('取消订阅', 'icon-remove', function(){
                commonToolHandler(id, 'desub', $table);
            }));
        }

        if (id == 'auth' && userRole == "ROLE_ADMIN") {
            tools.push(generateToolElement('升级', 'icon-add', function(){
                commonToolHandler(id, 'upgrade', $table);
            }));
            tools.push(generateToolElement('降级', 'icon-remove', function(){
                commonToolHandler(id, 'downgrade', $table);
            }));
        }

        if (id == "message") {
            if (userRole == "ROLE_PUB") {
                tools.push(generateToolElement('新增', 'icon-add', function () {
                    editMessage($table, {'id':'New'}, 'add');
                }));
                tools.push(generateToolElement('修改', 'icon-edit', function () {
                    var row = $table.datagrid('getSelected');
                    if(row == null) {
                        $.messager.alert('alert', "please select a row first");
                        return;
                    }
                    editMessage($table, row, 'update');
                }));
            }

            if (userRole == "ROLE_PUB" || userRole == "ROLE_ADMIN") {
                tools.push(generateToolElement('删除', 'icon-remove', function(){
                    deleteHandler(id, 'delete', $table);
                }));
            }

        }

        if (id == "type" && userRole == "ROLE_ADMIN") {
            tools.push(generateToolElement('新增', 'icon-add', function () {
                editType($table, 'add');
            }));
            tools.push(generateToolElement('删除', 'icon-remove', function(){
                deleteHandler(id, 'delete', $table);
            }));
            tools.push(generateToolElement('修改', 'icon-edit', function () {
                var row = $table.datagrid('getSelected');
                if(row == null) {
                    $.messager.alert('alert', "please select a row first");
                    return;
                }
                editType($table, 'update', row);
            }));
            tools.push(generateToolElement('保存', 'icon-save', function(){
                var row = $table.datagrid('getSelected');
                var index = $table.datagrid('getRowIndex', row);
                $table.datagrid('endEdit', index);
                if (row.id == '-1') {
                    commonAjax(obj.contextPath + '/addType', {'name':row.name});
                    $table.datagrid('load', {});
                }
            }));
            tools.push(generateToolElement('取消', 'icon-cancel', function(){
                var row = $table.datagrid('getSelected');
                var index = $table.datagrid('getRowIndex', row);
                $table.datagrid('cancelEdit', index);
            }));
        }

        return tools;
    }

    function deleteHandler(id, operation, $table) {
        $.messager.confirm('alert', '确定删除？', function (r) {
            if (r) {
                commonToolHandler(id, operation, $table);
            }
        })
    }

    function generateToolElement(name, icon, handler) {
        var element = {};
        element['text'] = name;
        element['iconCls'] = icon;
        element['handler'] = handler;
        return element;
    }

    function commonToolHandler(id, operation, $table) {
        var row = $table.datagrid('getSelected');
        if(row == null) {
            $.messager.alert('alert', "please select a row first");
            return;
        }

        var param = {};
        param["id"] = row.id;
        param["operation"] = operation;
        if (id == 'auth') {
            param['role'] = operation == "downgrade" ? "ROLE_SUB" : "ROLE_PUB";
        } else if (id == 'type') {
            param['name'] = row.name;
        }
        commonAjax(obj.contextPath + "/modify" + firstLetterUpCase(id), param);
        $table.datagrid('reload', {});
    }

    function firstLetterUpCase(word) {
        var res = word.charAt(0).toUpperCase();
        return res + word.substr(1);
    }

    function commonAjax(url, param) {
        $.ajax({
            url: url,
            type: 'post',
            data: param,
            success: function () {
                $.messager.alert('info', obj.message.actionSuccess);
            },
            error: function() {
                $.messager.alert('info', obj.message.actionFail);//TODO
            }
        });
    }

    function manageToolVisual(id, row) {
        var tools = $("#" + id).find('div.datagrid div.datagrid-toolbar a');

        if (id == "auth") {
            switch (row.role) {
                case 'ROLE_SUB':
                    tools.eq(0).show();
                    tools.eq(1).hide();
                    break;
                case 'ROLE_PUB':
                    tools.eq(0).hide();
                    tools.eq(1).show();
                    break;
                case 'ROLE_ADMIN':
                    tools.eq(0).hide();
                    tools.eq(1).hide();
                    break;
                default:
                    console.log("wrong user role")
            }
        } else if (id == 'subscribe') {
            switch (row.operation) {
                case '0':
                    tools.eq(0).hide();
                    tools.eq(1).show();
                    break;
                case '1':
                    tools.eq(0).show();
                    tools.eq(1).hide();
                    break;
                default:
                    console.log("wrong param for subscribe type")
            }
        } else if (id == 'type') {
            if (row.editing) {
                tools.slice(0, 3).hide();
                tools.slice(3).show();
            } else {
                tools.slice(0, 3).show();
                tools.slice(3).hide();
            }
        } else if (id == 'message') {
            if (userRole == "ROLE_PUB") {
                if (row.sended) {
                    tools.slice(1).hide();
                } else {
                    tools.slice(1).show();
                }
            } else if (userRole == "ROLE_ADMIN") {
                tools.eq(0).show();
            }
        }
    }

    function editType($table, operation, row) {
        if (operation == 'add') {
            row = {'id':'-1'}
            $table.datagrid('appendRow', row);
        }
        var index = $table.datagrid('getRowIndex', row);
        $table.datagrid('beginEdit', index);
    }

    function showMsgContent(id, row) {
        if(id == "message") {
            $.messager.show({
                width:300,
                height:200,
                title:row.title,
                msg:row.content,
                resizable:true,
                timeout:0,
                showType:'show',
                style: {
                    right:'',
                    bottom:''
                }
            });
        }
    }

    function editMessage($table, row, operation) {
        var $div = $("#msgEdit").clone(true).show();
        addToPanel('msg' + row.id, $div[0]);

        // var $type = setTypeComponent($div, operation, row);
        var $time = setTimeComponent($div, operation, row);
        var $im = setImComponent($div, operation, row);
        var $title = $div.find('input[name="title"]');
        var $content = $div.find('textarea');

        if (operation == 'update') {
            $title.val(row.title);
            $content.val(row.content);
        }

        $div.find('button[name="submit"]').click(function() {
            var sendTime = $time.datetimebox('getText');
            if ($im.val() == 0 && !validateTime(sendTime)) return;

            var param = {
                'title': $title.val(),
                'content':  $content.val(),
                'immediate': $im.val(),
                'sendTime': sendTime,
                // 'type': $type.combobox('getValue')
            };

            var url = obj.contextPath + "/addMessage";
            if (operation == 'update') {
                url = obj.contextPath + "/modifyMessage";
                param.operation = operation;
                param.id = row.id;
            }
            commonAjax(url, param);
            $("#tabs").tabs('close', 'msg' + row.id);
            $table.datagrid('reload');
        });
    }

    function setTypeComponent($div, operation, row) {
        var $type = $div.find('input[name="type"]');
        $type.combobox({
            url: obj.contextPath + "/getMessageTypes",
            valueField: 'id',
            textField: 'name',
            editable: false
        });

        if (operation == 'update') {
            $type.combobox('setValue', row.type);
        }
        return $type;
    }

    function setTimeComponent($div, operation, row) {
        var $time = $div.find('input[name="sendTime"]');
        $time.datetimebox({
            value: '0',
            showSeconds: false,
            required: true
        });

        if (operation == "update") {
           $time.datetimebox('setText', row.sendTime);
        }
        return $time;
    }

    function setImComponent($div, operation, row) {
        var $imme = $div.find('input[name="immediate"]');
        $imme.combobox({
            data: [{label: '立即推送', 'id': '1'}, {label: '定时推送', 'id': '0'}],
            valueField: 'id',
            textField: 'label',
            onSelect: function(data) {
                var $timecompo = $div.find(".sendTime");
                data.id == '1' ? $timecompo.hide() : $timecompo.show();
            },
            required: true
        });

        if (operation == 'update') {
            $imme.combobox('setValue', row.immediate);
        }
        return $imme;
    }

    function validateTime(time) {
        var gap = new Date(time) - new Date();
        if (gap <= 0) {
            $.messager.alert("warn", "发送时间必须晚于当前时间！");
            return false;
        } else if (gap / 1000 < 60) {
            $.messager.alert("warn", "不接受一分钟以内的定时消息！");
            return false;
        }
        return true;
    }
})({
    contextPath: "/comet/manage",//TODO
    editing: false,
    rowIndex: null,
    message: {
        actionFail: "操作失败，请重试！",
        actionSuccess: "操作成功！",
        remove: "确定删除？",
    },
    authColumn: [
        {field:'id',title:'编号',width:100,align:'left'},
        {field:'name',title:'名称',width:200},
        {field:'role',title:'角色',width:120}
    ],
    typeColumn: [
        {field:'id',title:'编号',width:100,align:'left'},
        {field:'name',title:'名称',width:200, editor:'text'}
    ],
    messageColumn: [
        {field:'id',title:'编号',width:100,align:'left'},
        {field:'title',title:'标题',width:200},
        {field:'type',title:'类别',width:200},
        {field:'publisher',title:'发布者',width:200},
        {field:'sendTime',title:'发送时间',width:200},
        {field:'sended',title:'发送状态',width:150, formatter: function(value,row,index){
            if (value == true) return "已发送";
            else return "未发送";
        }}
    ],
    subscribeColumn: [
        {field:'id',title:'编号',width:100,align:'left'},
        {field:'name',title:'类别名称',width:200}
    ]
});