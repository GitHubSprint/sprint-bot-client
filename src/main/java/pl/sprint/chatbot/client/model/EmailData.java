/*
 * Copyright © 2021 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.model;

import java.util.List;

/**
 * Email Model
 * @author Sławomir Kostrzewa
 */
public class EmailData {
    private String to;
    private String from;
    private String subject;
    private String text;
    private Boolean htmlContent;
    private String key;
    private List<String> attachments;
    private String symbol = "sprint"; // default symbol, can be overridden
    private String template; // email template name
    
    public EmailData() {
    }

    public EmailData(String to, String from, String subject, String text, boolean isHtmlContent, String key, List<String> attachments, String symbol, String template) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.text = text;
        this.htmlContent = isHtmlContent;
        this.key = key;
        this.attachments = attachments;
        this.symbol = symbol != null ? symbol : "sprint";
        this.template = template;
    }
    
    

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
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

    public Boolean getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(Boolean htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public String toString() {
        return "EmailData{" + "to=" + to + ", subject=" + subject + ", text=" + text + ", htmlContent=" + htmlContent + '}';
    }
    
    
    
}
