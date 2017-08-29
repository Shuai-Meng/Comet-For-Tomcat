/**
 * Created by shuaimeng2 on 2017/5/31.
 */

$(function() {
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
    })
}