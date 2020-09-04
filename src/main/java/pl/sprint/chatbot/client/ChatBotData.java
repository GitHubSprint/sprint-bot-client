/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client;

import java.util.Map;

/**
 *
 * @author skost
 */
public class ChatBotData {
    
    private String key;
    private String botname;
    private String channel;
    private String username;
    private Map<String, String> data;   

    public ChatBotData(String key, String botname, String channel, String username) {
        this.key = key;
        this.botname = botname;
        this.channel = channel;
        this.username = username;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
    
    

    public ChatBotData(String key, String botname) {
        this.key = key;
        this.botname = botname;
    }
    
   

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    
    public String getBotname() {
        return botname;
    }

    public void setBotname(String botname) {
        this.botname = botname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
    
    
    
}
