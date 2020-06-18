let stompClient = null;

const setConnected = (connected) => {
    if (connected) {
        $("#login-form").show();
    } else {
        $("#login-form").hide();
    }
    $("#user-list-form").hide();
    $("#edit-user-form").hide();
}

function initial() {
    connect();
}

const connect = () => {
    stompClient = Stomp.over(new SockJS("/gs-guide-websocket"));

    stompClient.connect({}, (frame) => {
        console.log('Connected to ' + frame);
        setConnected(true);
        stompClient.subscribe('/topic/authresult', (message) => prepAuthResult(JSON.parse(message.body)));
    });
};

const prepAuthResult = (result) => {
    console.log("Auth result "+result);
    if (result.message == "OK"){
        getUsers();
    }
}

const showUsers = (users) => {
    console.log("Users list size "+users.length);
    for(var i=0; i<users.length;i++) {
       $("#userList").append("<tr><td>" + users[i].id + "</td><td>" + users[i].name + "</td><td>" +
            users[i].login + "</td><td>" + users[i].password + "</td>"+
        "<td><button type=\"submit\" value=\""+users[i].id+
        "\" id=\"editSubmit\">Редактировать</button></td></tr>");
    }
}

const authUser = () => {
    console.log("Log in user "+$("#login").val());
    stompClient.send("/app/login", {}, JSON.stringify({
        'login':$("#login").val(),
        'password':$("#password").val()
    }));
};

const getUsers = () => {
    $("#login-form").hide();
    $("#user-list-form").show();
    console.log("Get users...");
    stompClient.subscribe('/topic/user/list.'+$("#login").val(), (message) => showUsers(JSON.parse(message.body)));
    stompClient.send("/app/user/list."+$("#login").val(), {}, {});
};

const editUser = () => {
    stompClient.subscribe('/topic/user/edit.'+$("#editSubmit").val(), (message) => showUsers(JSON.parse(message.body)));
    stompClient.send("/app/user/edit."+$("#editSubmit").val(), {}, {});
}

const createOrUpdateUser = () => {
    console.log("Create or update user...");
    stompClient.send("/user/save", {}, JSON.stringify({
        'name':$("#name").val(),
        'login':$("#login").val(),
        'password':$("#password").val()
    }));
};

$(function () {
    $("form").on('submit', event => {
        console.log("Submit " + event);
        event.preventDefault();
    });
    $("#authSubmit").click(authUser);
    $("#editSubmit").click(editUser);
    $("#saveSubmit").click(createOrUpdateUser);
    $("#getUsers").click(getUsers);
});