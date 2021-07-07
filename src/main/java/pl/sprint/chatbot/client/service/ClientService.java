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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.codehaus.jackson.map.ObjectMapper;
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
    
    private HttpURLConnection connection(String endpoint, String method, Object inputObject) throws MalformedURLException, IOException
    {
        URL url=new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);
        
        if(inputObject != null)
        {
            ObjectMapper objectMapper = new ObjectMapper(); 
            String json = objectMapper.writeValueAsString(inputObject);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);			
            }             
        }
                       
        
        return con;
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
    public Session createSession(String key, String botname, String channel, String username, Map<String,String> data, String wave) throws IOException, Exception
    {                
        ChatBotData cbd = new ChatBotData(key,botname, channel, username, wave);
        
        if(data != null && data.size() >0)
            cbd.setData(data);
                        
        HttpURLConnection connection = connection(endpoint + "/session", "POST", cbd);
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Session session = mapper.readValue(responseStream, Session.class);
        
        connection.disconnect();
        
        return session;
    }
    
    /**
     * Removes sessions 
     * @param sessionId SessionId
     * @param key Bot API KEY
     * @param botname name of Bot
     * @return
     * @throws IOException 
     */
    public Session removeSession(String sessionId, String key, String botname) throws IOException, Exception
    {

        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "DELETE", new ChatBotData(key,botname));
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Session session = mapper.readValue(responseStream, Session.class);        
        connection.disconnect();        
        return session; 
              
    }
    
    
    public SimpleModel addMessageToSend(String to, String from, String subject, String text, boolean isHtmlContent, String key, String session) throws IOException, Exception
    {

        HttpURLConnection connection = connection(endpoint + "/addmessage/" + session, "POST", new EmailData(to, from, subject, text, isHtmlContent, key));
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModel simpleModel = mapper.readValue(responseStream, SimpleModel.class);        
        connection.disconnect();        
        return simpleModel;                       
    }
        

    /**
     * Retrieve bot data
     * @param sessionId SessionId
     * @return
     */
    public Map<String,String> getData(String sessionId) throws IOException, Exception
    {
        Map<String, String> map = new HashMap<>();
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "GET", null);
        InputStream responseStream = connection.getInputStream();        
        ObjectMapper mapper = new ObjectMapper();                        
        Map<String,String> result = new ObjectMapper().readValue(responseStream, HashMap.class);                      
        connection.disconnect();    
        return result;
    }
    
    /**
     * Update bot data
     * @param sessionId SessionId
     * @param update
     * @param data new data
     * @return 
     */
    public Session updateSession(String sessionId, SessionUpdate update) throws IOException, Exception
    {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "PUT", update);
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Session session = mapper.readValue(responseStream, Session.class);        
        connection.disconnect();        
        return session; 
        
    }
    
    
    public Session updateData(String sessionId, Map<String,String> data) throws IOException, Exception
    {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "POST", data);
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Session session = mapper.readValue(responseStream, Session.class);        
        connection.disconnect();        
        return session;                                 
    }
    
    /**
     * 
     * @param sessionId
     * @param data
     * @return 
     */    
    public Session updateDataBot20(String sessionId, Map<String,String> data) throws IOException, Exception
    {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "PUT", data);
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Session session = mapper.readValue(responseStream, Session.class);        
        connection.disconnect();        
        return session;                  
    }
    
    /**
     * Retrieve count of active sessions.
     * @param channel name of channel
     * @return
     * @throws IOException 
     */
    public CountSessions countSessions(String channel) throws IOException
    { 
        
        HttpURLConnection connection = connection(endpoint + "/session/count?channel=" + channel, "GET", null);
        InputStream responseStream = connection.getInputStream(); 
        ObjectMapper mapper = new ObjectMapper();
        CountSessions result = mapper.readValue(responseStream, CountSessions.class);                      
        connection.disconnect();           
        return result;                
    }
    
    public CountSessions countSessions() throws IOException
    {

        HttpURLConnection connection = connection(endpoint + "/session/count/", "GET", null);
        InputStream responseStream = connection.getInputStream(); 
        ObjectMapper mapper = new ObjectMapper();
        CountSessions result = mapper.readValue(responseStream, CountSessions.class);                      
        connection.disconnect();           
        return result;                
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
        
        HttpURLConnection connection = connection(endpoint + "/chat", "POST", new ChatBotDTO(sessionId, chatQuery, key, bargeIn));
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        ChatBot response = mapper.readValue(responseStream, ChatBot.class);        
        connection.disconnect();        
        return response;              
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
    public TTSResponse tts(TTSRequest req) throws IOException, Exception
    {
        
        HttpURLConnection connection = connection(endpoint + "/tts", "POST", req);
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        TTSResponse response = mapper.readValue(responseStream, TTSResponse.class);        
        connection.disconnect();        
        return response;                                 
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
