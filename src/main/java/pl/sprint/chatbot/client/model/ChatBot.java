/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client.model;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 *
 * @author skost
 */
public class ChatBot {
    private String sessionId;
    private String status;
    private String text;
    
    private String prevQuestion;
    private String currentQuestion;
    private String prevText;
    private String prevPrevText;

    private String topic;        
    private int textDuration; 

    public int getTextDuration() {
        return textDuration;
    }

    public void setTextDuration(int textDuration) {
        this.textDuration = textDuration;
    }
        
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getPrevQuestion() {
        return prevQuestion;
    }

    public void setPrevQuestion(String prevQuestion) {
        this.prevQuestion = prevQuestion;
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getPrevText() {
        return prevText;
    }

    public void setPrevText(String prevText) {
        this.prevText = prevText;
    }

    public String getPrevPrevText() {
        return prevPrevText;
    }

    public void setPrevPrevText(String prevPrevText) {
        this.prevPrevText = prevPrevText;
    }
    
    

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
            
    public void setStatus(String status){ this.status = status;}
    public String getStatus(){ return this.status;}
    
    public void setText(String text){ this.text = text;}
    public String getText(){ return this.text;}
      
}
