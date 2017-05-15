<%--
  Created by IntelliJ IDEA.
  User: m
  Date: 17-5-14
  Time: 下午12:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div class="error  ${param.error == true ? '' : 'hide'}">
    <%--${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}--%>
</div>
<form action="/j_spring_security_check" method="post">
    <fieldset>
        <legend>登陆</legend>
        用户： <input type="text" name="j_username" style="width:150px;"/><br />
        密码： <input type="password" name="j_password" style="width:150px;" /><br />
        <input type="submit" value="登陆"/>
        <input type="reset" value="重置"/>
    </fieldset>
</form>
</body>
</html>
