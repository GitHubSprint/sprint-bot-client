/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.model;


/**
 * Session object
 * @author skost
 */
public class Session 
{
    private String sessionId;
    private boolean created;  
    private String sessionCreated;
    private String status;
    private String botname;    
    private long id;
    private String wave; 

    public String getWave() {
        return wave;
    }

    public void setWave(String wave) {
        this.wave = wave;
    }

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    

    
    public String getBotname() {
        return botname;
    }

    public void setBotname(String botname) {
        this.botname = botname;
    }

    public String getSessionCreated() {
        return sessionCreated;
    }

    public void setSessionCreated(String sessionCreated) {
        this.sessionCreated = sessionCreated;
    }
    
    
  

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
    
    
}
