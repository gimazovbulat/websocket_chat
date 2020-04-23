<html>
<body>
<form method="post" action="/signUp">
    <input type="text" placeholder="email" name="email" id="email">
    <input type="password" placeholder="password" name="password" id="password">
    <input type="submit">
    <#if status??>
        ${status}
    </#if>
</body>
</html>