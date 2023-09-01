/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.test;

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
public class TestThread
{
    private final String api_key;
    private final String endpoint;
    private final int threads = 5;


    public TestThread(String endpoint, String api_key)
    {
        this.endpoint = endpoint;
        this.api_key = api_key;
    }

    public void start(){
        SprintBotClient.disableSslVerification();
        ExecutorService executorService = Executors.newFixedThreadPool(500);

        for (int i = 0; i < threads; i++) {
            executorService.execute(() -> {
                try {
                    testCEZBot();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }


    private void testCEZBot() throws Exception {
        SprintBotClient cs = new SprintBotClient(endpoint);
        Map<String,String> map = new HashMap<>();
        Session session = cs.openSession(api_key, "testowa","CHAT","TEST", map,null);
        System.out.println("Open session " + session.toString());
        map.put("session", session.getSessionId());
        cs.updateData(session.getSessionId(), map);

        ChatBot cb = cs.chat(session.getSessionId(), "ML", api_key, true);
        System.out.println(cb);
        cb = cs.chat(session.getSessionId(), "TAK", api_key, true);
        System.out.println(cb);
        session =  cs.closeSession(session.getSessionId(),api_key, "testowa");
        System.out.println("Close session " + session.toString());
    }
    




}
