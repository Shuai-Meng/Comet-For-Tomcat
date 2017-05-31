/**
 * Created by shuaimeng2 on 2017/5/31.
 */

$(function() {
    comet();
});

function comet() {
    $.get("/comet/test", function(data) {
        if(data != "exist") {
            window.alert(data.title + data.content);
        }
    });
    comet();
}