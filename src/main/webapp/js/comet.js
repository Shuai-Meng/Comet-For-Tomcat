/**
 * Created by shuaimeng2 on 2017/5/31.
 */

$(function() {
    generateWindow("new", "<p>hehehehehehehe</p>");
    comet();
});

function comet() {
    console.log(new Date());
    $.ajax({
        type: 'get',
        url: "/comet/test",
        dataType: 'json',
        success: function (data) {
            console.log("[" + data.title + "]");
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

function generateWindow(title, content) {
   $.messager.show({
        width:300,
        height:200,
        title:title,
        msg:content,
        showType:'slide'
    });
}