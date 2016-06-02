'use strict';

var messageList = [];
var currentUser = "qqwik";
var editMode = false;
var editId;

function run() {
	logAs(currentUser);
	
	messageList = loadMessages();
	
	var sendButton = document.getElementsByClassName("sendButton")[0];
	sendButton.addEventListener("click", sendMessage);
	
	var sendButton = document.getElementsByClassName("CancelButton")[0];
	sendButton.addEventListener("click", onCancelButton);
	
	var changeNicknameButton = document.getElementById("changenick");
	changeNicknameButton.addEventListener("click", changeNickname);
	
	render(messageList);
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
	return {
		timestamp: getTextTimestamp(),
		user: user,
		messageText: messageText,
		messageMark: mark,
		id: uniqueId(),
		deleted: false
	};
}

function renderMessage(message) {
	var list = document.getElementsByClassName('messageList')[0];
	
	var element;
	if(message.user === currentUser) {
		if(message.messageText === "")
			return;
		element = elementFromTemplate(1);
		element.addEventListener("mouseover", showOptions);
		element.addEventListener("mouseout", hideOptions);
		var fields = element.children;
		fields[0].textContent = message.timestamp;
		fields[1].textContent = message.user;
		fields[2].textContent = message.messageText;
		fields[3].textContent = message.messageMark;
		fields[4].addEventListener("click", deleteMessage);
		fields[5].addEventListener("click", editMessage);
	}
	else {
		element = elementFromTemplate(0);
		var fields = element.children;
		fields[0].textContent = message.timestamp;
		fields[1].textContent = message.user;
		fields[2].textContent = message.messageText;
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

function getTextTimestamp() {
	var date = new Date();
	var hours = date.getHours();
	var minutes = date.getMinutes();
	var seconds = date.getSeconds();
	if(hours < 10)
		hours = "0" + hours;
	if(minutes < 10)
		minutes = "0" + minutes;
	if(seconds < 10)
		seconds = "0" + seconds;
	return hours + ":" + minutes + ":" + seconds;
}

function uniqueId() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random);
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
	if(editMode) {
		var index = indexById(editId, messageList);
		messageList[index].messageText = document.getElementById("inputField").value;
		messageList[index].messageMark = "(edited at " + getTextTimestamp() + ")";
		saveMessages(messageList);
		reRender();
	}
	else {
		var text = document.getElementById("inputField").value;
		if(text == "")
			return;
		var newMsg = newMessage(currentUser, text, "");
		messageList.push(newMsg);
		render([newMsg]);
	}
	saveMessages(messageList);
	restoreDefaults();
}

function changeNickname() {
	var temp = prompt("Enter new nickname", currentUser);
	if(temp.length == 0 || temp.length >= 20) {
		alert("invalid input");
		return;
	}
	currentUser = temp;
	logAs(currentUser);
	reRender();
}

function reRender() {
	var node = document.getElementsByClassName("messageList")[0];
	while (node.hasChildNodes()) {
		node.removeChild(node.lastChild);
	}
	
	render(messageList);
}

function onCancelButton() {
	restoreDefaults();
}

function restoreDefaults() {
	document.getElementsByClassName("CancelButton")[0].style.display = "none";
	document.getElementsByClassName("sendButton")[0].innerHTML = "Send";
document.getElementsByClassName("sendButton")[0].style.display = "inline";
document.getElementsByClassName("sendButton")[0].style.marginLeft = "45%";
	document.getElementById("inputField").value = "";
	editMode = false;
}

function saveMessages(listToSave) {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	localStorage.setItem("Menge messageList", JSON.stringify(listToSave));
}

function loadMessages() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	var item = localStorage.getItem("Menge messageList");

	return item && JSON.parse(item);
}

function deleteMessage() {
	var element = this.parentNode;
	var index = indexByElement(element, messageList);
	messageList[index] = newMessage(messageList[index].user, "", "(deleted at " + getTextTimestamp() + ")");
	saveMessages(messageList);
	reRender();
	restoreDefaults();
}

function editMessage() {
	editMode = true;
	var element = this.parentNode;
	editId = element.attributes['data-id'].value;
	console.log(editId);
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