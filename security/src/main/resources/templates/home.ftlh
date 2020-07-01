<!DOCTYPE html>
<html lang="en">
<#assign ctx=springMacroRequestContext.contextPath />

<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>欢迎！${username}</h1>
<a href="${ctx}/user/admin">检测ROLE_ADMIN角色</a>
<a href="${ctx}/user/normal">检测ROLE_NORMAL角色</a>
<button onclick="window.location.href='${ctx}/logout'">退出登录</button>
</body>
</html>