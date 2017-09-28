/**
 * Created by shuaimeng2 on 2017/5/31.
 */

$(function() {
    generateWindow();
    comet();
});

function comet() {
    console.log(new Date());
    $.ajax({
        type: 'get',
        url: "/comet/test",
        dataType: 'json',
        success: function (data) {
            showMessage(data.title, data.content);
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
    $('<div></div>', {id:'unread'}).appendTo("body").window({
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
        content: createList(),
        tools: [
            {
                iconCls:'icon-more',
                handler: function () {
                    
                }

            }
        ],
    });

    $("#unread").window('window').css({
        left: '',
        top: '',
        right: 0,
        bottom: -document.body.scrollTop-document.documentElement.scrollTop
    });
}

function createList() {
    var div = $('<div></div>').appendTo("#unread");
    div.datalist({
        url: "getUnreadMessages",
        lines: true,
        checkbox: false,
        fit: true,
        valueField: 'title',
        textField: 'title',
    });
    return div.html();
}
