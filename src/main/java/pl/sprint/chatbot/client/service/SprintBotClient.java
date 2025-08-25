package pl.sprint.chatbot.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import pl.sprint.chatbot.client.model.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SprintBotClient {

    private final String endpoint;
    private final int timeout;
    private final ObjectMapper mapper = new ObjectMapper();
    private final OkHttpClient client;
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public SprintBotClient(String endpoint) {
        this(endpoint, 20000);
    }

    public SprintBotClient(String endpoint, int timeout) {
        this.endpoint = endpoint;
        this.timeout = timeout;
        this.client = createUnsafeClient(timeout);
    }

    public int getTimeout() {
        return timeout;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Session openSession(String key, String botname, String channel, String username, Map<String, String> data, String wave) throws IOException {
        ChatBotData cbd = new ChatBotData(key, botname, channel, username, wave);
        if (data != null && !data.isEmpty()) cbd.setData(data);

        Request request = buildPostRequest("/session", cbd);
        return execute(request, Session.class);
    }

    public Session closeSession(String sessionId, String key, String botname) throws IOException {
        ChatBotData cbd = new ChatBotData(key, botname);
        Request request = buildRequestWithBody("/session/" + sessionId, cbd, "DELETE");
        return execute(request, Session.class);
    }

    public SimpleModel addMessageToSend(String to, String from, String subject, String text, List<String> attachments, boolean isHtmlContent, String key, String session, String symbol, String template) throws IOException {
        EmailData emailData = new EmailData(to, from, subject, text, isHtmlContent, key, attachments, symbol, template);
        Request request = buildPostRequest("/addmessage/" + session, emailData);
        return execute(request, SimpleModel.class);
    }

    public Map<String, String> getSessionData(String sessionId) throws IOException {
        Request request = new Request.Builder().url(endpoint + "/session/" + sessionId).get().build();
        return execute(request, new TypeReference<Map<String, String>>() {});
    }

    public Map<String, String> getSessionDbData(String sessionId) throws IOException {
        Request request = new Request.Builder().url(endpoint + "/session/db/" + sessionId).get().build();
        return execute(request, new TypeReference<Map<String, String>>() {});
    }

    public Session updateSession(String sessionId, SessionUpdate update) throws IOException {
        Request request = buildRequestWithBody("/session/" + sessionId, update, "PUT");
        return execute(request, Session.class);
    }

    public void updateData(String sessionId, Map<String, String> data) throws IOException {
        Request request = buildPostRequest("/session/" + sessionId, data);
        client.newCall(request)
                .execute()
                .close();
    }

    public CountSessions countSessions(String channel) throws IOException {
        HttpUrl url = HttpUrl
                .parse(endpoint + "/session/count")
                .newBuilder()
                .addQueryParameter("channel", channel)
                .build();

        Request request = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        return execute(request, CountSessions.class);
    }

    public CountSessions countSessions() throws IOException {
        Request request = new Request
                .Builder()
                .url(endpoint + "/session/count/")
                .get()
                .build();
        return execute(request, CountSessions.class);
    }

    public ChatBot chat(String sessionId, String chatQuery, String key, boolean bargeIn) throws IOException {
        ChatBotDTO chatData = new ChatBotDTO(sessionId, chatQuery, key, bargeIn);
        Request request = buildPostRequest("/chat", chatData);
        return execute(request, ChatBot.class);
    }

    public ChatBot chat(String sessionId, String chatQuery, String key) throws IOException {
        return this.chat(sessionId, chatQuery, key, false);
    }

    private Request buildPostRequest(String path, Object body) throws IOException {
        String json = mapper.writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(json, JSON);
        return new Request.Builder()
                .url(endpoint + path)
                .post(requestBody)
                .build();
    }

    private Request buildRequestWithBody(String path, Object body, String method) throws IOException {
        String json = mapper.writeValueAsString(body);
        RequestBody requestBody = RequestBody
                .create(json, JSON);
        return new Request.Builder()
                .url(endpoint + path)
                .method(method, requestBody)
                .build();
    }

    private <T> T execute(Request request, Class<T> clazz) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String body = response.body() != null ? response.body().string() : null;
            return body != null ? mapper.readValue(body, clazz) : null;
        }
    }

    private <T> T execute(Request request, TypeReference<T> typeRef) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String body = response.body() != null ? response.body().string() : null;
            return body != null ? mapper.readValue(body, typeRef) : null;
        }
    }

    private OkHttpClient createUnsafeClient(int timeoutMillis) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }};

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                    .readTimeout(timeoutMillis + 1000L, TimeUnit.MILLISECONDS)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
