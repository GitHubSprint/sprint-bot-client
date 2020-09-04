/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

/**
 *
 * @author skost
 */
public class ClientService {
    
    private int timeout = 15000;
    private String endpoint;// = "http://192.168.253.64/api";    

    
  

    public ClientService(String endpoint) 
    {         
        
        this.endpoint = endpoint;        
    }

 
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Session createSession(String key, String botname, String channel, String username, Map<String,String> data) throws IOException
    {                
        ChatBotData cbd = new ChatBotData(key,botname, channel, username);
        
        if(data != null && data.size() >0)
            cbd.setData(data);
        
        WebResource webResource = client().resource(endpoint + "/session");
 
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class, cbd);         
        
        checkStatusResponse(response.getStatus());
            
        return response.getEntity(Session.class);
    }
    
    
    public Session removeSession(String sessionId, String key, String botname) throws IOException
    {        
              
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").delete(ClientResponse.class, new ChatBotData(key,botname));
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(Session.class);
    }
    
    public ClientResponse getData(String sessionId) throws RuntimeException, UniformInterfaceException, ClientHandlerException
    {
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").get(ClientResponse.class); 
        
        checkStatusResponse(response.getStatus());
        
        return response;
    }
    
    
    public Session updateData(String sessionId, Map<String,String> data)
    {
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
                        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").put(ClientResponse.class,data); 
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(Session.class);
    }
    
    
    public CountSessions countSessions() throws IOException
    {        
        WebResource webResource = client().resource(endpoint + "/session/count");
 
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").get(ClientResponse.class);         
        
        checkStatusResponse(response.getStatus());
        
        
        return response.getEntity(CountSessions.class);
    }
    
    
    public ChatBot chat(String sessionId, String chatQuery, String key) throws UnsupportedEncodingException, IOException
    {
        WebResource webResource = client().resource(endpoint + "/chat");
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class, new ChatBotDTO(sessionId, chatQuery, key));
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(ChatBot.class);
        
        
    }
    
    
    public TTSResponse tts(TTSRequest req)
    {
        WebResource webResource = client().resource(endpoint + "/tts");
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class, req);   
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(TTSResponse.class);
    }
    
    
    private void checkStatusResponse(int status)
    {
        if (status != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + status);
        } 
    }
    
    private Client client()
    {
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add(JacksonJsonProvider.class);
//        Client client = Client.create(defaultClientConfig);
        
        
//        ClientConfig clientConfig = new DefaultClientConfig();
//        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);        
        return Client.create(defaultClientConfig);
    }
    
    
}
