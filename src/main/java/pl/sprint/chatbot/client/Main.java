package pl.sprint.chatbot.client;

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

public class Main {
    private final static String ENDPOINT =  "https://localhost:8443/api";
    private final static String API_KEY = "Sprint";


    public static void main(String[] args) throws Exception {

        System.out.println(ENDPOINT);
        TestChat test;

        if(args.length > 0)
            test = new TestChat(Integer.parseInt(args[0]));
        else
            test = new TestChat();

        test.start();

    }

    private static class TestChat {
        private int cnt=1;
        private int cntMore = 0;
        private long avgTime = 0;
        int numberOfTests = 1;
        private final static SprintBotClient sprintBotClient = new SprintBotClient(ENDPOINT, 5000);

        public TestChat() {
            SprintBotClient.disableSslVerification();
        }

        public TestChat(int numberOfTests) {
            this.numberOfTests = numberOfTests;
        }

        public void start() throws InterruptedException {

            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < numberOfTests; i++) {
                tasks.add(this::test);
            }

            ExecutorService executorService = Executors.newFixedThreadPool(200);
            executorService.invokeAll(tasks);
            executorService.shutdown();

            System.out.println("Counter: " + cnt);
            System.out.println("Counter more than 1 sec: " + cntMore);
            System.out.println("Average time: " + avgTime/cnt);
        }

        private Void test() {

            try {


                Map<String, String> map = new HashMap<>();


                Session session = sprintBotClient.openSession(API_KEY, "testowa", "CHAT", "TEST", map, null);
                map.put("session", session.getSessionId());

                sprintBotClient.updateData(session.getSessionId(), map);
                sprintBotClient.chat(session.getSessionId(), "ML", API_KEY, true);
                sprintBotClient.chat(session.getSessionId(), "TAK", API_KEY, true);
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

                ChatBot chatBot = sprintBotClient.chat(session.getSessionId(), "NIE", API_KEY, true);

                System.out.println(chatBot);

                long startTime = System.currentTimeMillis();
                sprintBotClient.chat(session.getSessionId(), "NIE", API_KEY, true);
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

                sprintBotClient.chat(session.getSessionId(), "TAK", API_KEY, true);

                long endTime = System.currentTimeMillis();
                long timeInMs = endTime - startTime;

                avgTime = avgTime + timeInMs;

                System.out.println(cnt + ": Time " + timeInMs + " ms");
                if (timeInMs > 1000) {
                    System.err.println(cnt + ": More than 1 sec! time: " + timeInMs + " cntMore: " + cntMore);
                    cntMore++;
                }
                cnt++;

                sprintBotClient.closeSession(session.getSessionId(), API_KEY, "testowa");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }

}
