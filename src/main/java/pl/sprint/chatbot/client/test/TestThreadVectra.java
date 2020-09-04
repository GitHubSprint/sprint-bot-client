/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client.test;

import java.util.HashMap;
import java.util.Map;
import pl.sprint.chatbot.client.ClientService;
import pl.sprint.chatbot.client.Session;

/**
 *
 * @author Sławomir Kostrzewa
 */
public class TestThreadVectra implements Runnable
{
    private final static String API_KEY = "Sprint";
    private static String endpoint =  "http://192.168.254.191/api";

    private int nuuOfT = 0;
    public TestThreadVectra(int nuuOfT, String endpoint)
    {
        this.nuuOfT = nuuOfT; 
        this.endpoint = endpoint;
    }
    
    @Override
    public void run() {
        testBot(); 
            
    }
    
    private void testBot()
    {
        try {
                    
            
            
            ClientService cs = new ClientService(endpoint);
            
            Map<String,String> map = new HashMap<>();
            map.put("city", "Siedlce");
            map.put("street", "Sokołowska");
            map.put("flat", "54");
            map.put("customerId", "777280");
            map.put("termindozaplaty", "8");
            map.put("saldo", "175,00");
            map.put("termindopobrania", "8");
            map.put("pesel", "43033104152");
            map.put("house", "70");
            Session session = cs.createSession(API_KEY, "vectra","IVR","IVR",map);
            cs.getData(session.getSessionId()).getEntity(String.class); 
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "start", API_KEY).getText();
            Thread.sleep(1000);
            
            cs.chat(session.getSessionId(), "witam", API_KEY).getTopic();
            
            
            Thread.sleep(2000);
            cs.chat(session.getSessionId(), "tv", API_KEY).getTopic();
            cs.chat(session.getSessionId(), "z kim rozmawiam", API_KEY).getTopic();
            //SCENRIUSZ
            cs.chat(session.getSessionId(), "chwila", API_KEY).getTopic();
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "cześć", API_KEY).getTopic();
            Thread.sleep(1000);            
            cs.chat(session.getSessionId(), "koniec", API_KEY).getTopic();
            Thread.sleep(2000);
            cs.removeSession(session.getSessionId(),API_KEY, "vectra");
            
            
            //System.out.println("sessionId=" + session.getSessionId() + " Thread: " + nuuOfT);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
