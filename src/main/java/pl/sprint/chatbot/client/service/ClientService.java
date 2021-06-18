/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client.service;


import pl.sprint.chatbot.client.model.ChatBotData;
import pl.sprint.chatbot.client.model.ChatBotDTO;
import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.model.CountSessions;
import pl.sprint.chatbot.client.model.Session;
import pl.sprint.chatbot.client.model.TTSRequest;
import pl.sprint.chatbot.client.model.TTSResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import pl.sprint.chatbot.client.model.EmailData;
import pl.sprint.chatbot.client.model.SessionUpdate;
import pl.sprint.chatbot.client.model.SimpleModel;

/**
 * SprintBot client service class.
 * @author skost
 */
public class ClientService {
    
    private int timeout = 15000;
    private String endpoint;   

    /**
     * COnstructor 
     * @param endpoint Bot Endpoint 
     */
    public ClientService(String endpoint) 
    {                 
        this.endpoint = endpoint;        
    }

    /**
     * Get and Set Timeout
     * @return 
     */
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Get and set Endpoint
     * @return 
     */
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    

    /**
     * Create session
     * @param key chatbot api key
     * @param botname name of bota
     * @param channel name of channel like VOICE, CHAT etc.
     * @param username username e.g. phone number
     * @param data additional chat data
     * @param wave IVR System Session ID
     * @return
     * @throws IOException 
     */
    public Session createSession(String key, String botname, String channel, String username, Map<String,String> data, String wave) throws IOException
    {                
        ChatBotData cbd = new ChatBotData(key,botname, channel, username, wave);
        
        if(data != null && data.size() >0)
            cbd.setData(data);
        
        WebResource webResource = client().resource(endpoint + "/session");
 
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class, cbd);         
        
        checkStatusResponse(response.getStatus());
            
        return response.getEntity(Session.class);
    }
    
    /**
     * Removes sessions 
     * @param sessionId SessionId
     * @param key Bot API KEY
     * @param botname name of Bot
     * @return
     * @throws IOException 
     */
    public Session removeSession(String sessionId, String key, String botname) throws IOException
    {        
              
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").delete(ClientResponse.class, new ChatBotData(key,botname));
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(Session.class);
    }
    
    
    public SimpleModel addMessageToSend(String to, String from, String subject, String text, boolean isHtmlContent, String key, String session)
    {        
              
        WebResource webResource = client().resource(endpoint + "/addmessage/" + session);
        
        System.out.println(endpoint + "/addmessage/" + session);
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class, 
                        new EmailData(to, from, subject, text, isHtmlContent, key));
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(SimpleModel.class);
    }
    
    public SimpleModel addMessageToSend(String session)
    {        
              
        WebResource webResource = client().resource(endpoint + "/addmessage/" + session);
                
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").get(ClientResponse.class);
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(SimpleModel.class);
    }

    
    
    /**
     * Retrieve bot data
     * @param sessionId SessionId
     * @return
     * @throws RuntimeException
     * @throws UniformInterfaceException
     * @throws ClientHandlerException 
     */
    public ClientResponse getData(String sessionId) throws RuntimeException, UniformInterfaceException, ClientHandlerException
    {
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").get(ClientResponse.class); 
        
        checkStatusResponse(response.getStatus());
        
        return response;
    }
    
    /**
     * Update bot data
     * @param sessionId SessionId
     * @param update
     * @param data new data
     * @return 
     */
    public Session updateSession(String sessionId, SessionUpdate update)
    {
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
                        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").put(ClientResponse.class,update); 
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(Session.class);
    }
    
    
    public Session updateData(String sessionId, Map<String,String> data)
    {
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
                        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class,data); 
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(Session.class);
    }
    
    /**
     * 
     * @param sessionId
     * @param data
     * @return 
     */    
    public Session updateDataBot20(String sessionId, Map<String,String> data)
    {
        WebResource webResource = client().resource(endpoint + "/session/" + sessionId);
                        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").put(ClientResponse.class,data); 
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(Session.class);
    }
    
    /**
     * Retrieve count of active sessions.
     * @param channel name of channel
     * @return
     * @throws IOException 
     */
    public CountSessions countSessions(String channel) throws IOException
    {        
        WebResource webResource = client().resource(endpoint + "/session/count?channel=" + channel);
                
 
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").get(ClientResponse.class);         
        
        checkStatusResponse(response.getStatus());
        
        
        return response.getEntity(CountSessions.class);
    }
    
    public CountSessions countSessions() throws IOException
    {        
        WebResource webResource = client().resource(endpoint + "/session/count/");
                
 
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").get(ClientResponse.class);         
        
        checkStatusResponse(response.getStatus());
        
        
        return response.getEntity(CountSessions.class);
    }
    
    /**
     * Chatting
     * @param sessionId SessionId
     * @param chatQuery customer input
     * @param key Bot API KEY
     * @param bargeIn is BargeIn
     * @return Bot Output
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public ChatBot chat(String sessionId, String chatQuery, String key, boolean bargeIn) throws UnsupportedEncodingException, IOException
    {
        WebResource webResource = client().resource(endpoint + "/chat");
        
        ClientResponse  response = webResource
                .accept("application/json")
                .type("application/json").post(ClientResponse.class, new ChatBotDTO(sessionId, chatQuery, key, bargeIn));
        
        checkStatusResponse(response.getStatus());
        
        return response.getEntity(ChatBot.class);
        
        
    }
    public ChatBot chat(String sessionId, String chatQuery, String key) throws UnsupportedEncodingException, IOException
    {                
        return this.chat(sessionId,chatQuery,key,false);                
    }
    
    /**
     * Retrieves TTS
     * @param req query name
     * @return 
     */
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
        return Client.create(defaultClientConfig);
    }
    
    /**
     * Reoves SSL veryfication
     */
    public static void disableSslVerification() 
    {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    
    
}
