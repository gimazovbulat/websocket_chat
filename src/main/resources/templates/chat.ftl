<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-16">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Chat</title>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script>
        var webSocket;

        function connect() {
            // webSocket = new WebSocket('ws://localhost:8080/chat');
            webSocket = new SockJS("http://localhost:8080/chat");
            webSocket.onmessage = function receiveMessage(response) {
                let data = response['data'];
                let json = JSON.parse(data);
                $('#messagesList').append(
                    "<li>" +
                    "from: " + json['from'] +
                    "<br>" +
                    "text: " + json['text'] +
                    "</li>")
            }
            getChat(${room.id})
        }

        function sendMessage(text, roomId, userId) {
            let message = {
                "text": text,
                "roomId": roomId,
                "userId": userId
            };
            console.log(text);
            console.log(roomId);
            console.log(userId)
            webSocket.send(JSON.stringify(message));
        }
    </script>

    <script>
        function getChat(roomId) {
            $.ajax({
                url: "/messages",
                method: "GET",
                dataType: "json",
                data: {
                    "roomId": roomId
                },
                contentType: "application/json",
                success: function (response) {
                    for (let i in response) {
                        $('#messagesList').append(
                            '<li>' +
                            "from: " + response[i]['sender']['email'] +
                            "<br>" +
                            "text: " + response[i]['text'] +
                            '</li>');
                    }
                    getUsers(roomId);
                }
            })
        }
    </script>
    <script>
        function getUsers(roomId) {
            $.ajax({
                url: "/api/chat/users",
                method: "GET",
                dataType: "json",
                data: {
                    "roomId": roomId,
                    "pageId": '${pageId}'
                },
                contentType: "application/json",
                success: function (response) {
                    for (let i in response) {
                        $('#chatUsers').append(
                            '<li>' +
                            response[i]['email'] +
                            '</li>');
                    }
                    getUsers(roomId);
                }
            })
        }
    </script>
</head>
<body onload="connect(${room.id})">
Room creator: ${room.creator.email}<br>
Users in room:
<ul id="chatUsers">
    <#list room.chatters as chatter>
        ${chatter.email} <br>
    </#list>
</ul>
<div>
    <label for="message">Text</label>
    <input name="message" id="message" placeholder="Message">
    <button onclick="sendMessage($('#message').val(), '${room.id}', '${user.id}')">Send</button>
    <h3>Messages</h3>
    <ul id="messagesList">

    </ul>
</div>
</body>
</html>