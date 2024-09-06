package pl.sprint.chatbot.client.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    private String status;

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

    public String getStatus(){
        return status;
    }
}