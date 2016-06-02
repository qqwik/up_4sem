
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

<div id="message"></div>
<div class="main">
    <div class="container">
        <div class="row">
            <div class="cols col-6 buttons_block">
                <div class="left_block">
                    <div class="header">Welcome to Chat!
                    </div>
                    <div class="left_block_form_top">
                        <button class="left_block_buttons_edit_name" name = "Edit user name">Edit user name</button>
                    </div>
                </div>
            </div>
            <div class="cols col-6">
                <div class="right_block">
                    <div class="message_block">
                        <div class="read-message">
                        </div>
                        <div class="label_network_fail"></div>
                    </div>
                    <div class="send_area">
                        <textarea id="write-message" name="text" placeholder="Enter your message..."></textarea>
                        <button class = "right_block_button_enter" name = "Send a message"  >Send</button>
                    </div>
                </div>

            </div>
            <div class="row">
                <div class="cols col-12">
                    <div class="name_creator">
                        <em>Molchan Svetlana</em>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>