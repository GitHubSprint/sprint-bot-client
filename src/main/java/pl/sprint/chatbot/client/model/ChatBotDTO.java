/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.model;

/**
 *
 * @author skost
 */
public class ChatBotDTO {
    private String sessionId;
    private String question;
    private String key;
    private boolean bargeIn;

    public ChatBotDTO(String sessionId, String question, String key, boolean bargeIn) {
        this.sessionId = sessionId;
        this.question = question;
        this.key = key;
        this.bargeIn = bargeIn; 
    }

    public boolean isBargeIn() {
        return bargeIn;
    }

    public void setBargeIn(boolean bargeIn) {
        this.bargeIn = bargeIn;
    }

    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
}
