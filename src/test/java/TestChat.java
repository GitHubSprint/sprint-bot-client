/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */

import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.model.Session;
import pl.sprint.chatbot.client.service.SprintBotClient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Standard chat test.
 * @author Sławomir Kostrzewa
 */
public class TestChat {
    private final String api_key;
    private final String endpoint;


    public TestChat(String endpoint, String api_key) {
        this.endpoint = endpoint;
        this.api_key = api_key;
    }


    public void start() throws Exception {
        SprintBotClient.disableSslVerification();

        for (int i = 0; i < 5; i++) {
            testCEZBot();
        }
    }


    private void testCEZBot() throws Exception {
        SprintBotClient sprintBotClient = new SprintBotClient(endpoint, 5000);
        Map<String,String> map = new HashMap<>();

        Session session = sprintBotClient.openSession(api_key, "testowa","CHAT","TEST", map,null);

        System.out.println("Open session " + session.toString());
        map.put("session", session.getSessionId());
        sprintBotClient.updateData(session.getSessionId(), map);

        ChatBot cb = sprintBotClient.chat(session.getSessionId(), "ML", api_key, true);
        System.out.println(cb);
        cb = sprintBotClient.chat(session.getSessionId(), "TAK", api_key, true);
        System.out.println(cb);
        session =  sprintBotClient.closeSession(session.getSessionId(),api_key, "testowa");
        System.out.println("Close session " + session.toString());
    }
    




}
