<head>
    <script
            src="https://code.jquery.com/jquery-3.4.1.min.js"
            integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
            crossorigin="anonymous"></script>
    <script>
        function getAllChatRooms() {
            $.ajax({
                url: "/api/rooms",
                method: "GET",
                dataType: "json",
                contentType: "application/json",
                success: function (response) {
                    $('#rooms').html("");
                    for (let i in response) {
                        $('#rooms').append('<li><a href=/rooms/' + response[i]['id'] + '>'
                            + response[i]['name'] + '</a></li>');
                    }
                }
            })
        }
    </script>
    <script>
        function getUsersChatRooms(userId) {
            $.ajax({
                url: "/api/rooms",
                method: "GET",
                dataType: "json",
                data: {
                    "userId": userId
                },
                contentType: "application/json",
                success: function (response) {
                    $('#rooms').html("");
                    for (let i in response) {
                        $('#rooms').append('<li><a href=/rooms/' + response[i]['id'] + '>'
                            + response[i]['name'] + '</a></li>');
                    }
                }
            })
        }
    </script>
    <script>
        function createRoom() {
            $('#error').html("");
            let roomName = $('#roomName').val();
            $.ajax({
                    url: "/api/room?name=" + roomName,
                    method: "POST",
                    dataType: "json",
                    contentType: "application/json",
                    complete: function (response) {
                        console.log(response)
                        if (response['responseText'] !== "") {
                            let err = JSON.parse(response['responseText'])['errorMessage'];
                            $('#error').html(err);
                        }
                        getUsersChatRooms();
                    }
                }
            )
        }
    </script>
</head>
<body onload="getAllChatRooms()">
<button onclick="createRoom()">create new room</button>
<ul id="rooms">

</ul>
<br>
<span style="color: red" id="error"></span>
<br>
<input type="text" placeholder="room name" id="roomName"><br>
<button onclick="getUsersChatRooms(${user.id})">get my rooms</button>
</body>
