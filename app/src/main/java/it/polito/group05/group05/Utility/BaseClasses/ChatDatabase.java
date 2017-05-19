package it.polito.group05.group05.Utility.BaseClasses;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatDatabase {
    private String messageText;
    private String messageUser;
    private String messageUserId;
    private long messageTime;

    public ChatDatabase(String messageText, String messageUser, String messageUserId) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageUserId = messageUserId;
        messageTime = new Date().getTime();
    }

    public ChatDatabase(){

    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
