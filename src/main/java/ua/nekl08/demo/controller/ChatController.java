package ua.nekl08.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import ua.nekl08.demo.WebApplication;
import ua.nekl08.demo.model.ChatMessage;

import java.security.Principal;

import static java.lang.String.format;
import static ua.nekl08.demo.WebApplication.minusUserInRoom;
import static ua.nekl08.demo.WebApplication.plusUserInRoom;

@Controller
public class ChatController {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/chat/{roomId}/sendMessage")
  @SendTo("/channel/{roomId}") // раньше так нельзя было сделать
  public ChatMessage sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, Principal principal) {
    chatMessage.getRooms().addAll(WebApplication.roomsAndUsers.keySet());
    chatMessage.getCountUser().addAll(WebApplication.roomsAndUsers.values());
    chatMessage.setSender(principal.getName());
    return chatMessage;
//    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
  }

  @MessageMapping("/chat/{roomId}/addUser")
  public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage,
                      SimpMessageHeaderAccessor headerAccessor, Principal principal) {
    // мапа возвращает старое значение
    String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
    // если юзер уже был на каком то канале, то на старый канал прийдет сообщение, что юзер покинул канал
    if (currentRoomId != null) {
      ChatMessage leaveMessage = new ChatMessage();
      leaveMessage.setType(ChatMessage.MessageType.LEAVE);
//      leaveMessage.setSender(chatMessage.getSender());
      leaveMessage.setSender(principal.getName());
      minusUserInRoom(currentRoomId);
      messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);
    }
    plusUserInRoom(roomId);
    chatMessage.getRooms().addAll(WebApplication.roomsAndUsers.keySet());
    chatMessage.getCountUser().addAll(WebApplication.roomsAndUsers.values());
//    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
    headerAccessor.getSessionAttributes().put("username", principal.getName());
    chatMessage.setSender(principal.getName());
    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);

  }
}
