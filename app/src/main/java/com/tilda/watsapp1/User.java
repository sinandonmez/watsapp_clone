package com.tilda.watsapp1;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String name;
    private String phone;
    private String token;
    private String imageUrl;
    private String lastMessage;
    private Object lastMessageTime;
    private int unreadMessageCount;

    public User() {
    }

    public User(String name, String phone, String token, String imageUrl, String lastMessage, Object lastMessageTime) {
        this.name = name;
        this.phone = phone;
        this.token = token;
        this.imageUrl = imageUrl;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public String getName() {
        if (name != null && !name.equals(""))
            return name;
        else return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Object getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Object lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }

    public Map<String, Object> toMap(String name, String phone, String imageUrl, String lastMessage, Object lastMessageTime){

        Map<String, Object> map = new HashMap<>();

        if(name==null) map.put("name", getName()); else map.put("name", name);
        if(phone==null) map.put("phone", getPhone()); else map.put("phone", phone);
        if(imageUrl==null) map.put("imageUrl", getImageUrl()); else map.put("imageUrl", imageUrl);
        if(lastMessage==null) map.put("lastMessage", getLastMessage()); else map.put("lastMessage", lastMessage);
        if(lastMessageTime==null) map.put("lastMessageTime", getLastMessageTime()); else map.put("lastMessageTime", lastMessageTime);

        return map;
    }

}
