/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.test;

import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.sprint.chatbot.client.InvalidResponseException;
import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.service.SprintBotClient;
import pl.sprint.chatbot.client.model.Session;

import javax.net.ssl.*;

/**
 * Standard chat test.
 * @author Sławomir Kostrzewa
 */
public class TestThread
{
    private final String api_key;
    private final String endpoint;
    private final int threads = 100;
    private final Map<Integer,Mapa> reqResp = new HashMap<>();


    public TestThread(String endpoint, String api_key)
    {        
        this.endpoint = endpoint;
        this.api_key = api_key;

        reqResp.put(0, new Mapa("co mam zrobić jeżeli na moim ikp nie ma niektórych dokuemntów np e recept", "brakdokumentow"));
        reqResp.put(1, new Mapa("gdzie znajdę swój certfikat covid", "certyfikatcovid"));
        reqResp.put(2, new Mapa("nie mam danych swojego dziecka w jkp", "dodaniedziecka"));
        reqResp.put(3, new Mapa("czy historia leczenia szpitalnego jest widoczna w ikp", "edm"));
        reqResp.put(4, new Mapa("chce wypełnić upoważnienie na odbiór dokumentacji medycznej", "edm"));
        reqResp.put(5, new Mapa("czy mogę się zalogować przez edowód", "edowod"));
        reqResp.put(6, new Mapa("błędne dane w e-rejestarcja", "edycjadanych"));
        reqResp.put(7, new Mapa("błąd w wyrobieniu karty ekuz dla dziecka", "ekuz"));
        reqResp.put(8, new Mapa("rejestracja do chirurga", "erejestracja"));
        reqResp.put(9, new Mapa("rejestracja do ginekologa", "erejestracja"));
        reqResp.put(10, new Mapa("ile ważne jest e skierowanie", "eskierowanie"));
        reqResp.put(11, new Mapa("złożenie skargi", "zgloszenienieprawidlowosci"));
        reqResp.put(12, new Mapa("zgłoszenie nieprawidłowości", "zgloszenienieprawidlowosci"));
        reqResp.put(13, new Mapa("co to jest zdarzenie medyczne", "zdarzeniamedyczne"));
        reqResp.put(14, new Mapa("czemu nie pojawiają się wszystkie zdarzenia medyczne na  ikp", "zdarzeniamedyczne"));

    }

    public void start(){
        trustAllCertificates();
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
        Map<String,String> req = new HashMap<>();
        for(int i=0; i<15;i++)
        {
            Random random = new Random();
            int randomNumber = random.nextInt(11);
            Mapa m = reqResp.get(randomNumber);
            req.put(m.getK(), m.getV());
        }
        Map<String,String> map = new HashMap<>();
        Session session = cs.openSession(api_key, "testowa","CHAT","CEZ-TEST", map,null);
//        System.out.println("Open session " + session.toString());
//        map.put("session", session.getSessionId());
//        cs.updateData(session.getSessionId(), map);

        ChatBot cb = cs.chat(session.getSessionId(), "CEZ", api_key, true);

        if(!cb.getText().startsWith("Podaj"))
            throw new InvalidResponseException("Invalid response: " + cb.getText());

        for (Map.Entry<String, String> entry : req.entrySet()) {
//            System.out.println(session.getSessionId() + "\t" + entry.getKey() + " ::: " + entry.getValue());

            ChatBot resp = cs.chat(session.getSessionId(), entry.getKey(), api_key, true);
//            System.out.println(session.getSessionId() + "\t" + entry.getKey() + " ::: " + entry.getValue() +  " ::: " + resp.getCurrentQuestion() + " ::: " + resp.getText());

            if(!resp.getText().startsWith(entry.getValue())) {
                session =  cs.closeSession(session.getSessionId(),api_key, "testowa");
                System.out.println("Close ERROR session " + session.toString());
                throw new InvalidResponseException("Key: " + entry.getKey() + " value: " + entry.getValue() + "\t response: " + resp.getText() + "\t session: " + session.getSessionId());
            }

            Thread.sleep(500);
        }

        session =  cs.closeSession(session.getSessionId(),api_key, "testowa");
//        System.out.println("Close session " + session.toString());
    }
    

    public static void trustAllCertificates()
    {
        try {
            ////////////////////////////////////////////////////////////////////////////////
            TrustManager[] trustAllCerts = new TrustManager[]
                    {
                            new X509TrustManager()
                            {
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return null;
                                }
                                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                            }
                    };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            /////////////////////////////////////////////////////////////////////////////////
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace();
        }
    }

    private static class Mapa{
        private String k;
        private String v;

        public Mapa(String k, String v) {
            this.k = k;
            this.v = v;
        }

        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }
    }
}
