package pl.sprint.chatbot.client;

import pl.sprint.chatbot.client.error.InternalServerException;
import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.model.Session;
import pl.sprint.chatbot.client.service.SprintBotClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static String ENDPOINT =  "https://192.168.254.64:8443/api";
//    private final static String ENDPOINT =  "https://localhost:8443/api";
    private final static String API_KEY = "Sprint";

    private static int errorOpenSession = 0;
    private static int errorChat = 0;
    private static int errorCloseSession = 0;
    private static int errorUpdate = 0;
    private static int error = 0;
    private static final int threads = 300;
    private static final int numberOfTests = 1;

    public static void main(String[] args) throws Exception {

        System.out.println(ENDPOINT);
        TestChat test = new TestChat();
        test.start();

    }

    private static class TestChat {
        private int cnt = 1;
        private int cntMore = 0;
        private long avgTime = 0;

        private final static SprintBotClient sprintBotClient = new SprintBotClient(ENDPOINT, 20000);

        public TestChat() {
            SprintBotClient.disableSslVerification();
        }



        public void start() throws InterruptedException {

            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < numberOfTests; i++) {
                tasks.add(this::test);
            }

            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            executorService.invokeAll(tasks);
            executorService.shutdown();

            System.out.println("Counter: " + cnt);
            System.out.println("Counter more than 1 sec: " + cntMore);
            System.out.println("Average time: " + avgTime/cnt);

            System.out.println("Errors: " + error);
            System.out.println("errorOpenSession: " + errorOpenSession);
            System.out.println("errorChat: " + errorChat);
            System.out.println("errorCloseSession: " + errorCloseSession);
            System.out.println("errorUpdate: " + errorUpdate);
        }

        private Void test() {
            String sessionId = null;
            try {


                Map<String, String> map = new HashMap<>();

                Session session = sprintBotClient.openSession(API_KEY, "testowa", "CHAT", "TEST", map, null);

                sessionId = session.getSessionId();
                map.put("session", session.getSessionId());

                sprintBotClient.updateData(session.getSessionId(), map);
                sprintBotClient.chat(session.getSessionId(), "DATA", API_KEY, false);
                sprintBotClient.chat(session.getSessionId(), "GETALL", API_KEY, false);
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
                if (sessionData.isEmpty()) System.err.println("Empty MAP!!!");


                long startTime = System.currentTimeMillis();
                sprintBotClient.chat(session.getSessionId(), "TXT2NUM dwa", API_KEY, false);
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
                if (sessionData.isEmpty()) System.err.println("Empty MAP!!!");

                sprintBotClient.chat(session.getSessionId(), "TXT2NUM jeden", API_KEY, false);
                long endTime = System.currentTimeMillis();
                long timeInMs = endTime - startTime;

                avgTime = avgTime + timeInMs;
                System.out.println(cnt + ": Time " + timeInMs + " ms");

                sprintBotClient.closeSession(session.getSessionId(), API_KEY, "testowa");


                if (timeInMs > 1000) {
                    System.err.println(cnt + ": Chat more than 1 sec! time: " + timeInMs + " cntMore: " + cntMore);
                    cntMore++;
                }
                cnt++;

            } catch (Exception ex) {

                if(sessionId != null) {
                    try {
                        sprintBotClient.closeSession(sessionId, API_KEY, "testowa");
                    } catch (Exception e) {
                        System.err.println("Close session error: " + e.getMessage());
                    }
                }
                if(ex.getMessage().startsWith("openSession"))
                    errorOpenSession++;
                else if (ex.getMessage().startsWith("closeSession"))
                    errorCloseSession++;
                else if(ex.getMessage().startsWith("updateData"))
                    errorUpdate++;
                else if(ex.getMessage().startsWith("chat"))
                    errorChat++;
                else
                    error++;

                ex.printStackTrace();

            }

            return null;
        }
    }

}
