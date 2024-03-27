package pl.sprint.chatbot.client.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse{

    @JsonProperty("path")
    private String path;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("status")
    private int status;

    @JsonProperty("error_description")
    private String description;

    public String getPath(){
        return path;
    }

    public String getError(){
        return error;
    }

    public String getMessage(){
        return message;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public int getStatus(){
        return status;
    }
}