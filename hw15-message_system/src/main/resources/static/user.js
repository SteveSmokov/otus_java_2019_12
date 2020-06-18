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
        stompClient.subscribe('/topic/authresult', (message) => prepareResult(JSON.parse(message.body)));
        stompClient.subscribe('/topic/user/list', (message) => showUsers(JSON.parse(message.body)));
        stompClient.subscribe('/topic/user/save', (message) => prepareResult(JSON.parse(message.body)));
    });
};

const prepareResult = (result) => {
    console.log("Result "+result);
    if (result.message == "OK"){
        getUsers();
    }
}

const showUsers = (users) => {
    console.log("Users list size "+users.length);
    $("#userList").empty();
    for(var i=0; i<users.length;i++) {
       $("#userList").append("<tr><td>" + users[i].id + "</td><td>" + users[i].name + "</td><td>" +
            users[i].login + "</td><td>" + users[i].password + "</td>"+
        "<td><button type=\"submit\" onclick=\"editUser("+users[i].id+")\">Редактировать</button></td></tr>");
    }
}

const showEditUser = (user) => {
    console.log("User "+user);
    $("#user_id").val(user.id);
    $("#user_name").val(user.name);
    $("#user_login").val(user.login);
    $("#user_password").val(user.password);
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
    $("#edit-user-form").hide();
    $("#user-list-form").show();
    console.log("Get users...");
    stompClient.send("/app/user/list", {}, {});
};

const editUser = (userID) => {
    $("#user-list-form").hide();
    $("#edit-user-form").show();
    console.log("Edit user id="+userID);
    if(userID>0) {
        stompClient.subscribe('/topic/user/edit.'+userID, (message) => showEditUser(JSON.parse(message.body)));
        stompClient.send("/app/user/edit." + userID, {}, {});
    } else {
        showEditUser(JSON.stringify({
            'id':0,
            'name':'',
            'login':'',
            'password':''
        }));
    }
}

const createOrUpdateUser = () => {
    console.log("Create or update user...");
    stompClient.send("/app/user/save", {}, JSON.stringify({
        'id':$("#user_id").val(),
        'name':$("#user_name").val(),
        'login':$("#user_login").val(),
        'password':$("#user_password").val()
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