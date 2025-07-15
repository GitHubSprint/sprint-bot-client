/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl
 *
 * Refactored to use Unirest library.
 */
package pl.sprint.chatbot.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.jackson.JacksonObjectMapper;
import pl.sprint.chatbot.client.model.*;
import java.util.List;
import java.util.Map;

/**
 * SprintBot client service class (using Unirest).
 * @author skost
 */
public class SprintBotClient {

    private final String endpoint;
    private final int timeout;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor
     * @param endpoint Bot Endpoint
     */
    public SprintBotClient(String endpoint) {
        this(endpoint, 20000);
    }

    public SprintBotClient(String endpoint, int timeout) {
        this.endpoint = endpoint;
        this.timeout = timeout;
        // Configure Unirest instance
        Unirest.config()
                .setObjectMapper(new JacksonObjectMapper(mapper))
                .connectTimeout(timeout)
                .socketTimeout(timeout + 1000)
                .setDefaultHeader("Accept", "application/json")
                .setDefaultHeader("Content-Type", "application/json; charset=utf-8")
                .verifySsl(false);
    }

    public int getTimeout() {
        return timeout;
    }

    public String getEndpoint() {
        return endpoint;
    }



    /**
     * Create session
     * @param key chatbot api key
     * @param botname name of bota
     * @param channel name of channel like VOICE, CHAT etc.
     * @param username username e.g. phone number
     * @param data additional chat data
     * @param wave IVR System Session ID
     * @return Session object
     */
    public Session openSession(String key, String botname, String channel, String username, Map<String, String> data, String wave) {
        ChatBotData cbd = new ChatBotData(key, botname, channel, username, wave);
        if (data != null && !data.isEmpty()) {
            cbd.setData(data);
        }

        HttpResponse<Session> response = Unirest.post(endpoint + "/session")
                .body(cbd)
                .asObject(Session.class);

        return response.getBody();
    }

    /**
     * Removes sessions
     * @param sessionId SessionId
     * @param key Bot API KEY
     * @param botname name of Bot
     * @return Closed session object
     */
    public Session closeSession(String sessionId, String key, String botname) {
        HttpResponse<Session> response = Unirest.delete(endpoint + "/session/{sessionId}")
                .routeParam("sessionId", sessionId)
                .body(new ChatBotData(key, botname))
                .asObject(Session.class);

        return response.getBody();
    }

    public SimpleModel addMessageToSend(String to, String from, String subject, String text, List<String> attachments, boolean isHtmlContent, String key, String session) {
        EmailData emailData = new EmailData(to, from, subject, text, isHtmlContent, key, attachments);
        HttpResponse<SimpleModel> response = Unirest.post(endpoint + "/addmessage/{session}")
                .routeParam("session", session)
                .body(emailData)
                .asObject(SimpleModel.class);

        return response.getBody();
    }

    /**
     * Retrieve bot data from session
     * @param sessionId SessionId
     * @return Map of session data
     */
    public Map<String, String> getSessionData(String sessionId) {
        HttpResponse<Map<String, String>> response = Unirest.get(endpoint + "/session/{sessionId}")
                .routeParam("sessionId", sessionId)
                .asObject(new GenericType<Map<String, String>>() {});

        // Return an empty map if body is null to match original behavior (e.g., for EOFException)
        return response.getBody();
    }

    /**
     * Retrieve bot data from DB
     * @param sessionId SessionId
     * @return Map of session data from DB
     */
    public Map<String, String> getSessionDbData(String sessionId) {
        HttpResponse<Map<String, String>> response = Unirest.get(endpoint + "/session/db/{sessionId}")
                .routeParam("sessionId", sessionId)
                .asObject(new GenericType<Map<String, String>>() {});

        return response.getBody();
    }

    /**
     * Update bot data
     * @param sessionId SessionId
     * @param update SessionUpdate object
     * @return Updated session object
     */
    public Session updateSession(String sessionId, SessionUpdate update) {
        HttpResponse<Session> response = Unirest.put(endpoint + "/session/{sessionId}")
                .routeParam("sessionId", sessionId)
                .body(update)
                .asObject(Session.class);

        return response.getBody();
    }

    /**
     * Update session extData
     * @param sessionId SessionId
     * @param data extension data
     */
    public void updateData(String sessionId, Map<String, String> data) {
        Unirest.post(endpoint + "/session/{sessionId}")
                .routeParam("sessionId", sessionId)
                .body(data)
                .asEmpty();

    }

    /**
     * Retrieve count of active sessions.
     * @param channel name of channel
     * @return Count of sessions
     */
    public CountSessions countSessions(String channel) {
        HttpResponse<CountSessions> response = Unirest.get(endpoint + "/session/count")
                .queryString("channel", channel)
                .asObject(CountSessions.class);

        return response.getBody();
    }

    public CountSessions countSessions() {
        HttpResponse<CountSessions> response = Unirest.get(endpoint + "/session/count/")
                .asObject(CountSessions.class);

        return response.getBody();
    }

    /**
     * Chatting
     * @param sessionId SessionId
     * @param chatQuery customer input
     * @param key Bot API KEY
     * @param bargeIn is BargeIn
     * @return Bot Output
     */
    public ChatBot chat(String sessionId, String chatQuery, String key, boolean bargeIn) {
        ChatBotDTO chatData = new ChatBotDTO(sessionId, chatQuery, key, bargeIn);
        HttpResponse<ChatBot> response = Unirest.post(endpoint + "/chat")
                .body(chatData)
                .asObject(ChatBot.class);

        return response.getBody();
    }

    public ChatBot chat(String sessionId, String chatQuery, String key) {
        return this.chat(sessionId, chatQuery, key, false);
    }

    /**
     * Disables SSL verification for all requests.
     * NOTE: Use with caution, intended for development/testing with self-signed certificates.
     */
    public static void disableSslVerification() {
        Unirest.config().verifySsl(false);
    }
}