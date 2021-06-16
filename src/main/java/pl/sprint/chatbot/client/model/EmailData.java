/*
 * Copyright © 2021 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.model;

/**
 * Email Model
 * @author Sławomir Kostrzewa
 */
public class EmailData {
    private String to;
    private String from;
    private String subject;
    private String text;
    private boolean isHtmlContent;
    private String key;

    public EmailData() {
    }

    public EmailData(String to, String from, String subject, String text, boolean isHtmlContent, String key) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.text = text;
        this.isHtmlContent = isHtmlContent;
        this.key = key;
    }

    
    
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }        

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isIsHtmlContent() {
        return isHtmlContent;
    }

    public void setIsHtmlContent(boolean isHtmlContent) {
        this.isHtmlContent = isHtmlContent;
    }

    @Override
    public String toString() {
        return "EmailData{" + "to=" + to + ", subject=" + subject + ", text=" + text + ", isHtmlContent=" + isHtmlContent + '}';
    }
    
    
    
}
