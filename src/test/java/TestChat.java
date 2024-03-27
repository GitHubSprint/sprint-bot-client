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
    private int cnt=0;
    private int cntMore = 0;
    private long avgTime = 0;

    public TestChat(String endpoint, String api_key) {
        this.endpoint = endpoint;
        this.api_key = api_key;
    }


    public void start() throws Exception {
        SprintBotClient.disableSslVerification();

        int numberOfTests = 10;
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfTests; i++) {
            tasks.add(this::testCEZBot);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.invokeAll(tasks);
        executorService.shutdown();

        System.out.println("Counter: " + cnt);
        System.out.println("Counter more than 1 sec: " + cntMore);
        System.out.println("Average time: " + avgTime/cnt);
    }


    private Void testCEZBot() throws Exception {
        SprintBotClient sprintBotClient = new SprintBotClient(endpoint, 5000);
        Map<String,String> map = new HashMap<>();

        Session session = sprintBotClient.openSession(api_key, "testowa","CHAT","TEST", map,null);

//        System.out.println("Open session " + session.toString());
        map.put("session", session.getSessionId());

        sprintBotClient.updateData(session.getSessionId(), map);

        ChatBot cb = sprintBotClient.chat(session.getSessionId(), "ML", api_key, true);
//        System.out.println(cb);
//        Thread.sleep(2000);
        cb = sprintBotClient.chat(session.getSessionId(), "TAK", api_key, true);
//        System.out.println("TAK" + " - " + cb.getText());
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
        if(sessionData.isEmpty()) System.err.println("Empty MAP!!!");
//        System.out.println("Session Data: \n" + sessionData);
        cb = sprintBotClient.chat(session.getSessionId(), "NIE", api_key, true);
//        System.out.println("NIE" + " - " + cb.getText());

        cb = sprintBotClient.chat(session.getSessionId(), "TAK", api_key, true);
//        System.out.println("TAK" + " - " + cb.getText());

        cb = sprintBotClient.chat(session.getSessionId(), "NIE", api_key, true);
//        System.out.println("NIE" + " - " + cb.getText());

        long startTime = System.currentTimeMillis();
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
        if(sessionData.isEmpty()) System.err.println("Empty MAP!!!");

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
        if(sessionData.isEmpty()) System.err.println("Empty MAP!!!");

        sprintBotClient.updateData(session.getSessionId(), map);
        sessionData = sprintBotClient.getSessionData(session.getSessionId());
        if(sessionData.isEmpty()) System.err.println("Empty MAP!!!");
        long endTime = System.currentTimeMillis();
        long timeInMs = endTime - startTime;

        avgTime = avgTime + timeInMs;

        System.err.println(cnt + ": Time " + timeInMs + " ms");
        if(timeInMs > 1000) {
            System.out.println(cnt + ": More than 1 sec! time: " + timeInMs + " cntMore: " + cntMore);
            cntMore++;
        }
        cnt++;
        cb = sprintBotClient.chat(session.getSessionId(), "NIE", api_key, true);
//        System.out.println("NIE" + " - " + cb.getText());

        session =  sprintBotClient.closeSession(session.getSessionId(),api_key, "testowa");
//        System.out.println("Close session " + session.toString());
        return null;
    }
    




}
