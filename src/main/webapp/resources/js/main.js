'use strict';

var nameInput = $('#name');
var roomInput = $('#room-id');
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var roomIdDisplay = document.querySelector('#room-id-display');
var roomForm = document.querySelector('#roomForm');

var stompClient = null;
var currentSubscription;
var username = null;
var roomId = null;
var topic = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
  username = nameInput.val().trim();
  Cookies.set('name', username);
  if (username) {
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
  }
  event.preventDefault();
}

// Leave the current room and enter a new one.
function enterRoom(newRoomId) {
  roomId = newRoomId;
  Cookies.set('roomId', roomId);
  roomIdDisplay.textContent = roomId;
  topic = `/app/chat/${newRoomId}`;
  while (messageArea.firstChild) {
    messageArea.removeChild(messageArea.firstChild);
  }

  if (currentSubscription) {
    currentSubscription.unsubscribe();
  }
  currentSubscription = stompClient.subscribe(`/channel/${roomId}`, onMessageReceived);

  stompClient.send(`${topic}/addUser`,
    {},
    JSON.stringify({sender: username, type: 'JOIN'})
  );
}

function onConnected() {
  enterRoom(roomInput.val());
  connectingElement.classList.add('hidden');
}

function onError(error) {
  connectingElement.textContent = 'Не получилось подключиться';
  connectingElement.style.color = 'red';
}

function sendMessage(event) {
  var messageContent = messageInput.value.trim();
  if (messageContent.startsWith('/join ')) {
    var newRoomId = messageContent.substring('/join '.length);
    enterRoom(newRoomId);

  } else if (messageContent && stompClient) {
    var chatMessage = {
      sender: username,
      content: messageInput.value,
      type: 'CHAT'
    };
    stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(chatMessage));
  }
  messageInput.value = '';
  event.preventDefault();
}

function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);

  var messageElement = document.createElement('li');

  if (message.type == 'JOIN') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' присоединился!';
  } else if (message.type == 'LEAVE') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' покинул!';
  } else {
    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);
    if(message.sender === username){
      avatarElement.style['color'] = 'red';
    }

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
  }

  var textElement = document.createElement('p');
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);

  messageArea.appendChild(messageElement);




  var chanels =  document.querySelector('#chanels');
  chanels.innerHTML = "";
  for (let i = 0; i < message.rooms.length; i++) {
    var paragraphElement = document.createElement('p');

    var textel = document.createTextNode(message.rooms[i]);
    var textel2 = document.createTextNode(' : ' + message.countUser[i]);

    paragraphElement.appendChild(textel);
    paragraphElement.appendChild(textel2);
    chanels.appendChild(paragraphElement);
    paragraphElement.onclick  = function(){
      enterRoom(message.rooms[i]);
    }
  }



  messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
      hash = 31 * hash + messageSender.charCodeAt(i);
  }
  var index = Math.abs(hash % colors.length);
  return colors[index];
}

$(document).ready(function() {
  var savedName = Cookies.get('name');
  if (savedName) {
    nameInput.val(savedName);
  }

  var savedRoom = Cookies.get('roomId');
  if (savedRoom) {
    roomInput.val(savedRoom);
  }

  function createRoom(event){
    roomInput = $('#room');
    enterRoom(roomInput.val());
    event.preventDefault();
  }

  usernamePage.classList.remove('hidden');
  usernameForm.addEventListener('submit', connect, true);
  messageForm.addEventListener('submit', sendMessage, true);
  roomForm.addEventListener('submit', createRoom, true);
});
