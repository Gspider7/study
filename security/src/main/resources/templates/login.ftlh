<!DOCTYPE html>
<html lang="en">
<#assign ctx=springMacroRequestContext.contextPath />

<head>
    <meta charset="UTF-8">
    <title>登录</title>
</head>
<body>
<h1>登录</h1>
<#-- 注意这个/login请求是由spring security处理的，后台并没写这个接口 -->
<form method="post" action="${ctx}/login">
    <div>
        用户名：<input type="text" name="username">
    </div>
    <div>
        密码：<input type="password" name="password">
    </div>
    <div>
        <button type="submit">立即登录</button>
    </div>
</form>
</body>
</html>