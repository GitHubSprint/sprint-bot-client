package pl.sprint.chatbot.client.model;

/**
 *
 * @author skost
 */
public class SimpleModel {
    
    private String response;
    private String status;    

    public SimpleModel(String response, String status) {
        this.response = response;
        this.status = status;
    }

    public SimpleModel() {
    }
    
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
      
    
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "SimpleModel{" + "response=" + response + ", status=" + status + '}';
    }
    
    
}
