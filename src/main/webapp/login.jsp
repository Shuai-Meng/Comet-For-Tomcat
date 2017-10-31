<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Eagle Login</title>
</head>
<body text-align="center">
    <br>
    <h2 align="center">Welecome to Eagle System</h2>
    <div style="MARGIN-RIGHT: auto; MARGIN-LEFT: auto; width: 300px; height: 200px;
                margin-top: 200px; margin-bottom: auto; vertical-align:middle;">
        <div>${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</div>
        <form action="/comet/login" method="post">
            <fieldset>
                <legend>登陆</legend>
                用户： <input type="text" name="j_username" style="width:150px;"/><br>
                密码： <input type="password" name="j_password" style="width:150px;" /><br>
                <input type="submit" value="登陆"/>
                <input type="reset" value="重置"/>
            </fieldset>
        </form>
    </div>
</body>
</html>
