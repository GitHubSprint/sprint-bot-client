/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.service;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import pl.sprint.chatbot.client.BadRequestException;
import pl.sprint.chatbot.client.model.*;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SprintBot client service class.
 * @author skost
 */
public class SprintBotClient {
    
    private int timeout = 5000;
    private String endpoint;   

    /**
     * Constructor 
     * @param endpoint Bot Endpoint 
     */
    public SprintBotClient(String endpoint) 
    {                 
        this.endpoint = endpoint;        
    }
    public SprintBotClient(String endpoint, int timeout) 
    {                 
        this.endpoint = endpoint;   
        this.timeout = timeout;
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
    
    private HttpURLConnection connection(String endpoint, String method, Object inputObject) throws IOException {
        URL url=new URL(endpoint);
        HttpURLConnection  con = (HttpURLConnection) url.openConnection();

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
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
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
    public Session openSession(String key, String botname, String channel, String username, Map<String,String> data, String wave) throws Exception
    {                
        ChatBotData cbd = new ChatBotData(key,botname, channel, username, wave);
        
        if(data != null && !data.isEmpty())
            cbd.setData(data);
                        
        HttpURLConnection conn = connection(endpoint + "/session", "POST", cbd);
        Session session = null;

        if(checkStatusResponse(conn) == 200)
        {
            try (InputStream responseStream = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                session = mapper.readValue(responseStream, Session.class);

            }
        }

        return session;
    }


    private int checkStatusResponse(HttpURLConnection connection) throws BadRequestException, IOException, RuntimeException, JSONException {
        int code = connection.getResponseCode();
        if(code == 200)
            return code;
        else if (code == 400) {
            try (InputStream responseStream = connection.getErrorStream()) {
                String json = new BufferedReader(
                        new InputStreamReader(responseStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                JSONObject obj = new JSONObject(json);
                String status = obj.getString("error");

                if(obj.has("error_description")) {
                    status += " : " + obj.getString("error_description");
                }
                throw new BadRequestException(status);
            }

        } else {
            throw new RuntimeException("Failed : HTTP error code : "
                    + code);
        }
    }
    
    /**
     * Removes sessions 
     * @param sessionId SessionId
     * @param key Bot API KEY
     * @param botname name of Bot
     * @return
     * @throws IOException 
     */
    public Session closeSession(String sessionId, String key, String botname) throws Exception {

        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "DELETE", new ChatBotData(key,botname));
        Session session = null;
        if(checkStatusResponse(connection) == 200) {
            try (InputStream responseStream = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                session = mapper.readValue(responseStream, Session.class);

            }
        }
        return session; 
              
    }
    
    
    public SimpleModel addMessageToSend(String to, String from, String subject, String text, List<String> attachments, boolean isHtmlContent, String key, String session) throws Exception {

        HttpURLConnection connection = connection(endpoint + "/addmessage/" + session, "POST", new EmailData(to, from, subject, text, isHtmlContent, key, attachments));
        SimpleModel simpleModel = null;
        if(checkStatusResponse(connection) == 200)
        {
            try (InputStream responseStream = connection.getInputStream())
            {
                ObjectMapper mapper = new ObjectMapper();
                simpleModel = mapper.readValue(responseStream, SimpleModel.class);

            }
        }
        return simpleModel;                       
    }
        

    /**
     * Retrieve bot data from session
     * @param sessionId SessionId
     * @return
     * @throws java.io.IOException
     */
    public Map<String,String> getSessionData(String sessionId) throws IOException, Exception
    {
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "GET", null);
        Map<String,String> result = new HashMap<>();
        try (InputStream responseStream = connection.getInputStream()) {
            if(responseStream != null)
            {
                ObjectMapper mapper = new ObjectMapper();                
                
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
                result = mapper.readValue(responseStream, typeRef);
                
            }
            
        }
        catch(EOFException ex)
        {
            result = new HashMap<>();
        }
        return result;
    }
    
    
    /**
     * Retrieve bot data from DB
     * @param sessionId SessionId
     * @return
     * @throws java.io.IOException
     */
    public Map<String,String> getSessionDbData(String sessionId) throws IOException, Exception
    {
        HttpURLConnection connection = connection(endpoint + "/session/db/" + sessionId, "GET", null);
        Map<String,String> result = new HashMap<>();
        try (InputStream responseStream = connection.getInputStream()) {
            if(responseStream != null)
            {
                ObjectMapper mapper = new ObjectMapper();                                
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
                result = mapper.readValue(responseStream, typeRef);                
            }
            
        }
        catch(EOFException ex)
        {
            result = new HashMap<>();
        }
        return result;
    }
    
    /**
     * Update bot data
     * @param sessionId SessionId
     * @param update
     * @return 
     * @throws java.io.IOException 
     */
    public Session updateSession(String sessionId, SessionUpdate update) throws Exception {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "PUT", update);
        Session session = null;

        if(checkStatusResponse(connection) == 200)
        {
            try (InputStream responseStream = connection.getInputStream())
            {
                ObjectMapper mapper = new ObjectMapper();
                session = mapper.readValue(responseStream, Session.class);

            }
        }
        return session; 
        
    }
    
    /**
     * Update session extData
     * @param sessionId
     * @param data extension data
     * @return
     * @throws IOException
     * @throws Exception 
     */
    public Session updateData(String sessionId, Map<String,String> data) throws IOException, Exception {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "POST", data);
        Session session = null;

        if(checkStatusResponse(connection) == 200)
        {
            try (InputStream responseStream = connection.getInputStream())
            {
                ObjectMapper mapper = new ObjectMapper();
                session = mapper.readValue(responseStream, Session.class);

            }
        }
        return session;                                 
    }
    
    /**
     * Set BotData 
     * @throws java.io.IOException
     * @deprecated
     * This method delete all extData and add new, please use <p> Use {@link SprintBotClient#updateData(java.lang.String, java.util.Map)} instead.
     * @param sessionId
     * @param data extension data
     * @return 
     */    
    @Deprecated
    public Session updateDataBot20(String sessionId, Map<String,String> data) throws IOException, Exception {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "PUT", data);
        Session session;
        try (InputStream responseStream = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            session = mapper.readValue(responseStream, Session.class);
            
        }
        return session;                  
    }
    
    /**
     * Retrieve count of active sessions.
     * @param channel name of channel
     * @return
     * @throws IOException 
     */
    public CountSessions countSessions(String channel) throws IOException {
        
        HttpURLConnection connection = connection(endpoint + "/session/count?channel=" + channel, "GET", null);
        CountSessions result;
        try (InputStream responseStream = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(responseStream, CountSessions.class);
            
        }
        return result;                
    }
    
    public CountSessions countSessions() throws IOException {

        HttpURLConnection connection = connection(endpoint + "/session/count/", "GET", null);
        CountSessions result;
        try (InputStream responseStream = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(responseStream, CountSessions.class);
            
        }
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
    public ChatBot chat(String sessionId, String chatQuery, String key, boolean bargeIn) throws IOException, BadRequestException, JSONException {
        
        HttpURLConnection connection = connection(endpoint + "/chat", "POST", new ChatBotDTO(sessionId, chatQuery, key, bargeIn));
        ChatBot response = null;
        if(checkStatusResponse(connection) == 200) {
            try (InputStream responseStream = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                response = mapper.readValue(responseStream, ChatBot.class);
            }
        }
        return response;              
    }
    public ChatBot chat(String sessionId, String chatQuery, String key) throws  IOException, BadRequestException, JSONException
    {                
        return this.chat(sessionId,chatQuery,key,false);                
    }
    
    
    
    /**
     * Reoves SSL veryfication
     */
    public static void disableSslVerification() {
        try {
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
