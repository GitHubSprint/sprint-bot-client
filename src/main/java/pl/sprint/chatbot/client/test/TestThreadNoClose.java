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
public class TestThreadNoClose implements Runnable
{
    private final static String API_KEY = "Sprint";

    private static String endpoint =  "http://192.168.254.191/api";
    
    
    private int nuuOfT = 0;
    public TestThreadNoClose(int nuuOfT, String endpoint)
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
            Session session = cs.createSession(API_KEY, "windykacja","IVR","IVR",map);
            cs.getData(session.getSessionId()).getEntity(String.class); 
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "start Pan GODLEWSKI-ADAM 175 00 1 ", API_KEY).getText();
            Thread.sleep(1000);
            
            cs.chat(session.getSessionId(), "tak", API_KEY).getTopic();
            
            
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "chwila", API_KEY).getTopic();
            
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "Siedlce Sokołowska 54 70", API_KEY).getTopic();
            //SCENRIUSZ
            cs.chat(session.getSessionId(), "SCENRIUSZ", API_KEY).getTopic();
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "dzisiaj", API_KEY).getTopic();
            Thread.sleep(2000);            
            cs.chat(session.getSessionId(), "TERMINDOXDNI 09/07/2020", API_KEY).getTopic();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
