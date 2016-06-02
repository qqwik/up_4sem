
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
    <link type="text/css" rel="stylesheet" href ="css/style.css"/>
</head>
<body>
<%--<h1><%= request.getAttribute("result") %></h1>--%>
<h1 id = "login_msg"><%= request.getAttribute("result") ==  null? " ": (String)request.getAttribute("result") %></h1>
    <form id = "login-form" action= "/login" method = "post">

        <p>
            login: <input type="text" name="user-login"><br>
            password: <input type="password" name="user-password"><br>
            <input name="register" type="submit" value="Register">
            <input name="enter" type="submit" value="Enter">
        </p>
    </form>

</body>
</html>
