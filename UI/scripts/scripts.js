var currentUser = "qqwik";
var nextMessageId;
var editMode = false;
var editId;

function run() {
	nextMessageId = document.getElementsByClassName("message").length;
	console.log(nextMessageId);
	
	var userMessagesContainer = document.getElementsByClassName("usermessage");
	
	var numOfMessages = userMessagesContainer.length;
	for(i = 0; i < numOfMessages; i++) {
		userMessagesContainer[i].addEventListener("mouseover", showOptions);
		userMessagesContainer[i].addEventListener("mouseout", hideOptions);
	}
	
	var sendButton = document.getElementsByClassName("sendButton")[0];
	sendButton.addEventListener("click", sendMessage);
	
	var sendButton = document.getElementsByClassName("cancelButton")[0];
	sendButton.addEventListener("click", onCancelButton);
	
	var changeNicknameButton = document.getElementById("changenick");
	changeNicknameButton.addEventListener("click", changeNickname)
}

function showOptions() {
	document.getElementById(this.id+"del").style.display = "inline";
	document.getElementById(this.id+"edi").style.display = "inline";
}

function hideOptions() {
	document.getElementById(this.id+"del").style.display = "none";
	document.getElementById(this.id+"edi").style.display = "none";
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

function createTimestamp() {
	var timestamp = document.createElement("span");
	var time = document.createTextNode(getTextTimestamp());
	timestamp.appendChild(time);
	var classAtt = document.createAttribute("class");
	classAtt.value = "timestamp";
	timestamp.setAttributeNode(classAtt);
	
	return timestamp;
}

function createUserNameMark() {
	var userName = document.createElement("span")
	var name = document.createTextNode(currentUser);
	userName.appendChild(name);
	var classAtt = document.createAttribute("class");
	classAtt.value = "loggedUser";
	userName.setAttributeNode(classAtt);
	
	return userName;
}

function createMessageText() {
	var messageText = document.createElement("span");
	var msgText = document.createTextNode(document.getElementById("inputField").value);
	messageText.appendChild(msgText);
	var classAtt = document.createAttribute("class");
	classAtt.value = "messageText";
	messageText.setAttributeNode(classAtt);
	
	return messageText;
}

function createMessageMark() {
	var messageMark = document.createElement("span")
	var classAtt = document.createAttribute("class");
	classAtt.value = "messageMark";
	messageMark.setAttributeNode(classAtt);
	
	return messageMark;
}

function createDeleteButton() {
	var deleteButton = document.createElement("img");
	var classAtt = document.createAttribute("class");
	classAtt.value = "option";
	deleteButton.setAttributeNode(classAtt);
	var idAtt = document.createAttribute("id");
	idAtt.value = nextMessageId + "del";
	deleteButton.setAttributeNode(idAtt);
	var src = document.createAttribute("src");
	src.value = "delete.png";
	deleteButton.setAttributeNode(src);
	var title = document.createAttribute("title");
	title.value = "delete message";
	deleteButton.setAttributeNode(title);
	deleteButton.addEventListener("click", deleteMessage);
	
	return deleteButton;
}

function createEditButton() {
	var editButton = document.createElement("img");
	var classAtt = document.createAttribute("class");
	classAtt.value = "option";
	editButton.setAttributeNode(classAtt);
	var idAtt = document.createAttribute("id");
	idAtt.value = nextMessageId + "edi";
	editButton.setAttributeNode(idAtt);
	var src = document.createAttribute("src");
	src.value = "edit.png";
	editButton.setAttributeNode(src);
	var title = document.createAttribute("title");
	title.value = "edit message";
	editButton.setAttributeNode(title);
	editButton.addEventListener("click", editMessage);
	
	return editButton;
}

function createNewMessage() {
	var newMessage = document.createElement("div");
	var classAtt = document.createAttribute("class");
	classAtt.value = "message usermessage";
	newMessage.setAttributeNode(classAtt);
	var idAtt = document.createAttribute("id");
	idAtt.value = nextMessageId;
	newMessage.setAttributeNode(idAtt);
	
	var timestamp = createTimestamp();
	var space = document.createTextNode(" ");
	var userName = createUserNameMark();
	var colon = document.createTextNode(":");
	var messageText = createMessageText();
	var messageMark = createMessageMark();
	var deleteButton = createDeleteButton();
	var editButton = createEditButton();
	
	newMessage.appendChild(timestamp);
	newMessage.appendChild(userName);
	newMessage.appendChild(colon);
	newMessage.appendChild(messageText);
	newMessage.appendChild(messageMark);
	newMessage.appendChild(deleteButton);
	newMessage.appendChild(editButton);
	
	newMessage.addEventListener("mouseover", showOptions);
	newMessage.addEventListener("mouseout", hideOptions);
	
	return newMessage;
}

function sendMessage() {
	if(editMode) {
		document.getElementById(editId).childNodes[3].innerHTML = document.getElementById("inputField").value;
		document.getElementById(editId).childNodes[4].innerHTML = "(edited at " + getTextTimestamp() + ")";
	}
	else {
		var newMessage = createNewMessage();
		document.getElementsByClassName("messageList")[0].appendChild(newMessage);
		nextMessageId++;
	}
	restoreDefaults();
}

function changeNickname() {
	var temp = prompt("Enter new nickname", currentUser);
	if(temp.length == 0 || temp.length >= 20) {
		alert("invalid input");
		return;
	}
	currentUser = temp;
	var usernamesContainer = document.getElementsByClassName("loggedUser");
	var n = usernamesContainer.length;
	for(i = 0; i < n; i++) {
		usernamesContainer[i].innerHTML = currentUser;
	}
}

function deleteMessage() {
	var par = document.getElementsByClassName("messageList")[0];
	var child = this.parentNode;
	par.removeChild(child);
	restoreDefaults();
}

function editMessage() {
	editMode = true;
	editId = this.parentNode.id;
	document.getElementsByClassName("cancelButton")[0].style.display = "inline-block";
document.getElementsByClassName("sendButton")[0].style.display = "inline-block";
document.getElementsByClassName("sendButton")[0].style.marginLeft = "10%";
	document.getElementsByClassName("sendButton")[0].innerHTML = "Accept";
	document.getElementById("inputField").value = this.parentNode.childNodes[3].innerHTML;
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
	editMode = false;
}
