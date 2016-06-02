
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">


    <link rel="stylesheet" type="text/css" href="css/Base.css">
    <script src="js/scripts.js"></script>
    <title>My chat</title>
</head>
<body onload="run();">


<div class="main">
    <div class="container">
        <div class="row main-block">
            <form class="cols col-12 enter_block" action="/logged" method="post">
                <div class="container">
                    <div class="row">
                        <div class="cols col-12">
                            <h1 class="full-name">Enter your username:</h1>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="cols col-12">
                            <input class="enter_name" type="text" name="username" required="required" placeholder="Enter your username...">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="cols col-12">
                            <input class="enter_password" type="password" name="password" required="required" placeholder="Enter your password...">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="cols col-12">
                            <button class="button" name="Enter Chat" onclick = "enterNameFunction();" >Enter Chat</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="row">
            <div class="cols col-12">
                <div class="name_creator_start">
                    <em>Molchan Svetlana</em>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>