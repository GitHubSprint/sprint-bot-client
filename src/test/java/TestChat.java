/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */

import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.model.Session;
import pl.sprint.chatbot.client.service.SprintBotClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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

        int numberOfTests = 500;
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfTests; i++) {
            tasks.add(this::testCEZBot);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        executorService.invokeAll(tasks);
        executorService.shutdown();
    }


    private Void testCEZBot() throws Exception {
        SprintBotClient sprintBotClient = new SprintBotClient(endpoint, 5000);
        Map<String,String> map = new HashMap<>();

        Session session = sprintBotClient.openSession(api_key, "testowa","CHAT","TEST", map,null);

        System.out.println("Open session " + session.toString());
        map.put("session", session.getSessionId());

        sprintBotClient.updateData(session.getSessionId(), map);

        ChatBot cb = sprintBotClient.chat(session.getSessionId(), "ML", api_key, true);
        System.out.println(cb);
        Thread.sleep(1000);
        cb = sprintBotClient.chat(session.getSessionId(), "TAK", api_key, true);
        System.out.println("TAK" + " - " + cb.getText());
        map = new HashMap<>();
        map.put("a", "Łódź");
        map.put("b", "Warszawa");
        map.put("c", "Kraków");
        map.put("d", "Poznań");
        map.put("e", "Gdańsk");
        map.put("f", "Gorzów Wielkopolski");
        map.put("g", "Zielona Góra");
        map.put("h", "Żelechów");

        sprintBotClient.updateData(session.getSessionId(), map);
        Map<String, String> sessionData = sprintBotClient.getSessionData(session.getSessionId());
        System.out.println("Session Data: \n" + sessionData);
        cb = sprintBotClient.chat(session.getSessionId(), "NIE", api_key, true);
        System.out.println("NIE" + " - " + cb.getText());

        cb = sprintBotClient.chat(session.getSessionId(), "TAK", api_key, true);
        System.out.println("TAK" + " - " + cb.getText());

        cb = sprintBotClient.chat(session.getSessionId(), "NIE", api_key, true);
        System.out.println("NIE" + " - " + cb.getText());
        map = new HashMap<>();
        map.put("h", "Łódź");
        map.put("g", "Warszawa");
        map.put("f", "Kraków");
        map.put("e", "Poznań");
        map.put("d", "Gdańsk");
        map.put("c", "Gorzów Wielkopolski");
        map.put("b", "Zielona Góra");
        map.put("a", "Żelechów");
        sprintBotClient.updateData(session.getSessionId(), map);
        sessionData = sprintBotClient.getSessionData(session.getSessionId());
        System.out.println("Session Data: \n" + sessionData);

        map = new HashMap<>();
        map.put("l", "Łódź");
        map.put("w", "Warszawa");
        map.put("k", "Kraków");
        map.put("p", "Poznań");
        map.put("g", "Gdańsk");
        map.put("gw", "Gorzów Wielkopolski");
        map.put("zg", "Zielona Góra");
        map.put("z", "Żelechów");
        sprintBotClient.updateData(session.getSessionId(), map);
        sessionData = sprintBotClient.getSessionData(session.getSessionId());
        System.out.println("Session Data: \n" + sessionData);

        sprintBotClient.updateData(session.getSessionId(), map);
        session =  sprintBotClient.closeSession(session.getSessionId(),api_key, "testowa");
        System.out.println("Close session " + session.toString());
        return null;
    }
    




}
