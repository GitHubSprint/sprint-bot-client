/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.sprint.chatbot.client.error.BadRequestException;
import pl.sprint.chatbot.client.error.ErrorResponse;
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

/**
 * SprintBot client service class.
 * @author skost
 */
public class SprintBotClient {
    
    private final int timeout;
    private final String endpoint;
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * Constructor 
     * @param endpoint Bot Endpoint
     */
    public SprintBotClient(String endpoint) {
        timeout = 5000;
        this.endpoint = endpoint;
    }
    public SprintBotClient(String endpoint, int timeout) {
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

    /**
     * Get and set Endpoint
     * @return 
     */
    public String getEndpoint() {
        return endpoint;
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
        
        if(inputObject != null) {
            String json = mapper.writeValueAsString(inputObject);
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
    public Session openSession(String key, String botname, String channel, String username, Map<String,String> data, String wave) throws Exception {
        ChatBotData cbd = new ChatBotData(key,botname, channel, username, wave);
        
        if(data != null && !data.isEmpty())
            cbd.setData(data);
                        
        HttpURLConnection conn = connection(endpoint + "/session", "POST", cbd);
        Session session = null;

        if(checkStatusResponse(conn) == 200) {
            try (InputStream responseStream = conn.getInputStream()) {
                session = mapper.readValue(responseStream, Session.class);
            }
        }
        return session;
    }


    private int checkStatusResponse(HttpURLConnection connection) throws BadRequestException, IOException, RuntimeException {
        int code = connection.getResponseCode();
        if(code == 200)
            return code;
        else {
            try (InputStream responseStream = connection.getErrorStream()) {
                ErrorResponse error = mapper.readValue(responseStream, ErrorResponse.class);
                throw new BadRequestException(error.getMessage());
            }
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
    public Session closeSession(String sessionId, String key, String botname) throws IOException, BadRequestException {

        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "DELETE", new ChatBotData(key,botname));
        Session session = null;
        if(checkStatusResponse(connection) == 200) {
            try (InputStream responseStream = connection.getInputStream()) {
                session = mapper.readValue(responseStream, Session.class);
            }
        }
        return session; 
              
    }
    
    
    public SimpleModel addMessageToSend(String to, String from, String subject, String text, List<String> attachments, boolean isHtmlContent, String key, String session) throws IOException, BadRequestException {

        HttpURLConnection connection = connection(endpoint + "/addmessage/" + session, "POST", new EmailData(to, from, subject, text, isHtmlContent, key, attachments));
        SimpleModel simpleModel = null;
        if(checkStatusResponse(connection) == 200) {
            try (InputStream responseStream = connection.getInputStream()) {
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
    public Map<String,String> getSessionData(String sessionId) throws IOException {
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "GET", null);
        Map<String,String> result = new HashMap<>();
        try (InputStream responseStream = connection.getInputStream()) {
            if(responseStream != null) {
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
    public Map<String,String> getSessionDbData(String sessionId) throws IOException {
        HttpURLConnection connection = connection(endpoint + "/session/db/" + sessionId, "GET", null);
        Map<String,String> result = new HashMap<>();
        try (InputStream responseStream = connection.getInputStream()) {
            if(responseStream != null) {
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
                result = mapper.readValue(responseStream, typeRef);                
            }
            
        } catch(EOFException ex) {
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
    public Session updateSession(String sessionId, SessionUpdate update) throws IOException, BadRequestException {
        
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "PUT", update);
        Session session = null;

        if(checkStatusResponse(connection) == 200) {
            try (InputStream responseStream = connection.getInputStream()) {
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
    public void updateData(String sessionId, Map<String,String> data) throws IOException, BadRequestException {
        HttpURLConnection connection = connection(endpoint + "/session/" + sessionId, "POST", data);
        checkStatusResponse(connection);
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
            result = mapper.readValue(responseStream, CountSessions.class);
        }
        return result;                
    }
    
    public CountSessions countSessions() throws IOException {

        HttpURLConnection connection = connection(endpoint + "/session/count/", "GET", null);
        CountSessions result;
        try (InputStream responseStream = connection.getInputStream()) {
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
    public ChatBot chat(String sessionId, String chatQuery, String key, boolean bargeIn) throws IOException, BadRequestException {
        
        HttpURLConnection connection = connection(endpoint + "/chat", "POST", new ChatBotDTO(sessionId, chatQuery, key, bargeIn));
        ChatBot response = null;
        if(checkStatusResponse(connection) == 200) {
            try (InputStream responseStream = connection.getInputStream()) {
                response = mapper.readValue(responseStream, ChatBot.class);
            }
        }
        return response;              
    }
    public ChatBot chat(String sessionId, String chatQuery, String key) throws  IOException, BadRequestException
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
        } catch (NoSuchAlgorithmException | KeyManagementException ignored) {}
    }
    
    
}
