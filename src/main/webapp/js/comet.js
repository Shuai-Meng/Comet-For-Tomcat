/**
 * Created by shuaimeng2 on 2017/5/31.
 */

$(function() {
    generateWindow();
    comet();
});

function comet() {
    console.log("start linking-" + new Date());
    $.ajax({
        type: 'get',
        url: "/comet/test",
        dataType: 'json',
        success: function (data) {
            console.log("message received-" + new Date());
            var len = data.length;
            while (len--) {
                showMessage(data[len].title, data[len].content);
            }
            comet();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status);
            if (XMLHttpRequest.status == 408) {
                comet();
            } else {
                console.log(errorThrown);
            }
        }
    });
}

function showMessage(title, content) {
   $.messager.show({
        width:300,
        height:200,
        title:title,
        msg:content,
        resizable:true,
        timeout:0,
        showType:'slide'
    });
}

function generateWindow() {
    var msgWin = $('<div/>', {id:"msgWin"});
    var list = $('<div/>');
    list.appendTo(msgWin);

    msgWin.appendTo("body").window({
        width:300,
        height:200,
        title:"未读消息",
        closable:false,
        draggable:true,
        collapsible:true,
        collapsed:true,
        minimizable:false,
        maximizable:false,
        shadow:false,
        href:"/comet/manage/getUnreadMessages",//TODO relative url
        extractor: function (data) {
            if (data == "[]") {
                return "no unread messages"
            } else {
                return generateList(data);
            }
        },
        tools: [
            {
                iconCls:'icon-undo',
                handler: function () {
                    msgWin.window('refresh');
                }
            },
            {
                iconCls:'icon-more',
                handler: function () {

                }
            }
        ],
    });

    msgWin.window('window').css({
        left: '',
        top: '',
        right: 0,
        bottom: -document.body.scrollTop-document.documentElement.scrollTop
    });
}

function generateList(data) {
    var result = $("<ul/>");
    JSON.parse(data).forEach(function (obj) {
        var li = $("<li/>");
        li.text(obj.title).click(function() {
            showContent(obj)
        });
        result.append(li);
    });

    return result;
}

function showContent(obj) {
    $("#msgWin").window('body').html(obj.content);
    //after reading, we delete this message form unread list
    $.post("/comet/manage/removeUnreadMessage", {messageId: obj.id}, function () {
       console.log("message deleted")
    });
}
