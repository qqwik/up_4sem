'use strict';

var Application = {
    messageList : [],
    mainUrl : 'http://localhost:8080/chat',
    token : 'TN11EN',
    currentUser : "qqwik",
    editMode : false,
    editId : -1,
	isConnected : void 0
};

function run() {
	logAs(Application.currentUser);
	
	var sendButton = document.getElementsByClassName("sendButton")[0];
	sendButton.addEventListener("click", sendMessage);
	
	var sendButton = document.getElementsByClassName("cancelButton")[0];
	sendButton.addEventListener("click", onCancelButton);
	
	var changeNicknameButton = document.getElementById("changenick");
	changeNicknameButton.addEventListener("click", changeNickname);
	
	connect();
}

function render(listToRender) {
	for(var i = 0; i < listToRender.length; i++) {
		renderMessage(listToRender[i]);
	}
}

function logAs(name) {
	var usernameField = document.getElementsByClassName("usernameField")[0];
	usernameField.innerHTML = name;
}

function newMessage(user, messageText, mark) {
	var date = new Date();
	return {
		timestamp: date.getTime(),
		author: user,
		text: messageText,
		messageMark: mark,
		id: uniqueId(),
		isDeleted: false
	};
}

function renderMessage(message) {
	var list = document.getElementsByClassName('messageList')[0];
	
	var element;
	if(message.author === Application.currentUser) {
		if(message.text === "")
			return;
		element = elementFromTemplate(1);
		element.addEventListener("mouseover", showOptions);
		element.addEventListener("mouseout", hideOptions);
		var fields = element.children;
		fields[0].textContent = message.timestamp;
		fields[1].textContent = message.author;
		fields[2].textContent = message.text;
		fields[3].textContent = message.messageMark;
		fields[4].addEventListener("click", deleteMessage);
		fields[5].addEventListener("click", editMessage);
	}
	else {
		element = elementFromTemplate(0);
		var fields = element.children;
		fields[0].textContent = message.timestamp;
		fields[1].textContent = message.author;
		fields[2].textContent = message.text;
		fields[3].textContent = message.messageMark;
	}
	element.removeAttribute("id");
	element.removeAttribute("style");
	element.setAttribute('data-id', message.id);
	element.classList.add("message");
	
	list.appendChild(element);
}

function elementFromTemplate(cond) {
	var template;
	if(cond === 0)
		template = document.getElementById("messageTemplate");
	else
		template = document.getElementById("usermessageTemplate");

	return template.cloneNode(true);
}

function uniqueId() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return "" + Math.floor(date * random);
}

function showOptions() {
	var deleteButton = this.children[4];
	deleteButton.style.display = "inline";
	var editButton = this.children[5];
	editButton.style.display = "inline";
}

function hideOptions() {
	var deleteButton = this.children[4];
	deleteButton.style.display = "none";
	var editButton = this.children[5];
	editButton.style.display = "none";
}

function sendMessage() {
	if(Application.editMode) {
		var index = indexById(Application.editId, Application.messageList);
		var message = Application.messageList[index];
		var text = document.getElementById("inputField").value;
		var messageToPut = {
			id : message.id,
			text : text
		};
		ajax('PUT', Application.mainUrl, JSON.stringify(messageToPut), function(){
			var date = new Date();
			message.text = text;
			message.messageMark = "(edited at " + date.getTime() + ")";
			reRender();
		});
	}
	else {
		var text = document.getElementById("inputField").value;
		if(text == "")
			return;
		var newMsg = newMessage(Application.currentUser, text, "");
		ajax('POST', Application.mainUrl, JSON.stringify(newMsg), function(){
		});
	}
	restoreDefaults();
}

function changeNickname() {
	var temp = prompt("Enter new nickname", Application.currentUser);
	if(temp.length == 0 || temp.length >= 20) {
		alert("invalid input");
		return;
	}
	Application.currentUser = temp;
	logAs(Application.currentUser);
	reRender();
}

function reRender() {
	var node = document.getElementsByClassName("messageList")[0];
	while (node.hasChildNodes()) {
		node.removeChild(node.lastChild);
	}
	
	render(Application.messageList);
}

function onCancelButton() {
	restoreDefaults();
}

function restoreDefaults() {
	document.getElementsByClassName("cancelButton")[0].style.display = "none";
	document.getElementsByClassName("sendButton")[0].innerHTML = "Send";
document.getElementsByClassName("sendButton")[0].style.display = "inline";
document.getElementsByClassName("sendButton")[0].style.marginLeft = "45%";
	document.getElementById("inputField").value = "";
	Application.editMode = false;
}

function updateList() {
    var messages = document.getElementsByClassName("message");
    for(var i = 0; i < messages.length; i++) {
        var fields = messages[i].children;
        var mark = fields[3].textContent;
        var index = indexByElement(messages[i], Application.messageList);
        if(Application.messageList[index].isDeleted && Application.messageList[index].author == Application.currentUser) {
            messages[i].parentNode.removeChild(messages[i]);
        }
        if(mark == Application.messageList[index].messageMark) {
            continue;
        }
        else {
            fields[2].textContent = Application.messageList[index].text;
            fields[3].textContent = Application.messageList[index].messageMark;
        }
    }
}

function loadMessages(done) {
    var url = Application.mainUrl + '?token=' + Application.token;

    ajax('GET', url, null, function(responseText){
        var response = JSON.parse(responseText);

        Application.messageList = Application.messageList.concat(response.messages);
        Application.token = response.token;
        render(response.messages);
		done();
    });
}

function updateMessages(done) {
    var url = Application.mainUrl + '?token=' + 'TN11EN';
 
    ajax('GET', url, null, function(responseText){
        var response = JSON.parse(responseText);
 
        Application.messageList = response.messages;
        Application.token = response.token;
        updateList();
        done();
    });
}

function deleteMessage() {
	var element = this.parentNode;
	var date = new Date();
	var index = indexByElement(element, Application.messageList);
	var message = Application.messageList[index];
	var messageToDelete = {
		id:message.id
	};

	ajax('DELETE', Application.mainUrl, JSON.stringify(messageToDelete), function() {
		message.text = "";
		message.messageMark = "(deleted at " + date.getTime() + ")";
		message.isDeleted = true;
		reRender();
	});
	restoreDefaults();
}

function editMessage() {
	Application.editMode = true;
	var element = this.parentNode;
	Application.editId = element.attributes['data-id'].value;
	document.getElementsByClassName("cancelButton")[0].style.display = "inline-block";
document.getElementsByClassName("sendButton")[0].style.display = "inline-block";
document.getElementsByClassName("sendButton")[0].style.marginLeft = "10%";
	document.getElementsByClassName("sendButton")[0].innerHTML = "Accept";
	document.getElementById("inputField").value = this.parentNode.children[2].textContent;
}

function indexByElement(element, messages){
	var id = element.attributes['data-id'].value;

	return messages.findIndex(function(item) {
		return item.id == id;
	});
}

function indexById(id, messages){
	return messages.findIndex(function(item) {
		return item.id == id;
	});
}

function ajax(method, url, data, continueWith, continueWithError) {
	var xhr = new XMLHttpRequest();

	continueWithError = continueWithError || defaultErrorHandler;
	xhr.open(method || 'GET', url, true);

	xhr.onload = function () {
		if (xhr.readyState !== 4)
			return;

		if(xhr.status != 200) {
			continueWithError('Error on the server side, response ' + xhr.status);
			return;
		}

		if(isError(xhr.responseText)) {
			continueWithError('Error on the server side, response ' + xhr.responseText);
			return;
		}

		continueWith(xhr.responseText);
	};    

    xhr.ontimeout = function () {
    	ontinueWithError('Server timed out !');
    };

    xhr.onerror = function (e) {
    	var errMsg = 'Server connection error !\n'+
    	'\n' +
    	'Check if \n'+
    	'- server is active\n'+
    	'- server sends header "Access-Control-Allow-Origin:*"\n'+
    	'- server sends header "Access-Control-Allow-Methods: PUT, DELETE, POST, GET, OPTIONS"\n';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

function defaultErrorHandler(message) {
	var warning = document.getElementsByClassName("warning")[0];
	warning.style.opacity = 1;
	warning.setAttribute("title", "cannot access the server!");
	console.error(message);
	output(message);
}

function isError(text) {
	if(text == "") {
		var warning = document.getElementsByClassName("warning")[0];
		if(warning.style.opacity == 1) {
			warning.style.opacity = 0.5;
			warning.setAttribute("title", "all right");
		}
		return false;
	}
	
	try {
		var obj = JSON.parse(text);
	} catch(ex) {
		return true;
	}

	return !!obj.error;
}

window.onerror = function(err) {
	console.log(err.toString());
};

function connect() {
 
 
        
    function whileConnected() {
        Application.isConnected = setTimeout(function () {
            updateMessages();
            var url = Application.mainUrl + '?token=' + Application.token;
            ajax('GET', url, null, function(responseText){
                var response = JSON.parse(responseText);
 
                Application.messageList = Application.messageList.concat(response.messages);
                Application.token = response.token;
                render(response.messages);
                whileConnected();
            }, function() {
                whileConnected();
            });
        }, 1000);
    }
 
    whileConnected();
}