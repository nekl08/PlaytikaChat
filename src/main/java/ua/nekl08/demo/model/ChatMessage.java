package ua.nekl08.demo.model;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage {

  public enum MessageType {
    CHAT, JOIN, LEAVE
  }

  private MessageType messageType;
  private String content;
  private String sender;

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public MessageType getType() {
    return messageType;
  }

  public void setType(MessageType messageType) {
    this.messageType = messageType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }


  private List<String> rooms = new ArrayList<>();
  private List<Integer> countUser = new ArrayList<>();


  public List<String> getRooms() {
    return rooms;
  }

  public void setRooms(List<String> rooms) {
    this.rooms = rooms;
  }

  public List<Integer> getCountUser() {
    return countUser;
  }

  public void setCountUser(List<Integer> countUser) {
    this.countUser = countUser;
  }
}
