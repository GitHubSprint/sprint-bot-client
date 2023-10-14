/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */

package pl.sprint.chatbot.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 *
 * @author skost
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatBot {
    private String sessionId;
    private String status;
    private String text;
    
    private String prevQuestion;
    private String currentQuestion;
    private String prevText;
    
    
    private List<String> answers; 

    private String topic;        
    private int textDuration; 

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
            

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

    @Override
    public String toString() {
        return "ChatBot{" +
                "sessionId='" + sessionId + '\'' +
                ", status='" + status + '\'' +
                ", text='" + text + '\'' +
                ", prevQuestion='" + prevQuestion + '\'' +
                ", currentQuestion='" + currentQuestion + '\'' +
                ", prevText='" + prevText + '\'' +
                ", answers=" + answers +
                ", topic='" + topic + '\'' +
                ", textDuration=" + textDuration +
                '}';
    }
}
