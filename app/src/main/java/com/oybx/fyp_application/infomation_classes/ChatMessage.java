package com.oybx.fyp_application.infomation_classes;

public class ChatMessage {
    private String messageId;
    private String from;
    private String to;
    private String messageDateTime;
    private String messageContent;

    public ChatMessage(String messageId, String from, String to, String messageDateTime, String messageContent) {
        this.messageId = messageId;
        this.from = from;
        this.to = to;
        this.messageDateTime = messageDateTime;
        this.messageContent = messageContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(String messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
