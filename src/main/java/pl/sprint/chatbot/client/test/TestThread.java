/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client.test;

import java.util.HashMap;
import java.util.Map;
import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.service.ClientService;
import pl.sprint.chatbot.client.model.Session;

/**
 * Standard chat test.
 * @author Sławomir Kostrzewa
 */
public class TestThread implements Runnable
{
    private String api_key = "Sprint";
    private String endpoint =  "https://localhost:8443/api";

    public TestThread(String endpoint, String api_key)
    {        
        this.endpoint = endpoint;
        this.api_key = api_key;
    }
    
    @Override
    public void run() {
        
        for(int i=0;i<10;i++)
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
            Session session = cs.createSession(api_key, "windykacja","CHAT","TEST",map,null);
            
            
            Map<String,String> respMap = cs.getSessionData(session.getSessionId()); 
            
            respMap.forEach((key, value) -> System.out.println(key + ":" + value));
            
            Thread.sleep(1000);
            ChatBot cb = cs.chat(session.getSessionId(), "START PAN GODLEWSKI-ADAM 175 00 1 ", api_key, true);
            
            System.out.println("TextDuration: "  + cb.getTextDuration());
            System.out.println("countSessions: "  + cs.countSessions().getCount());
            
            
            Thread.sleep(1000);
            
            cb = cs.chat(session.getSessionId(), "tak", api_key, true);
            System.out.println("TextDuration: "  + cb.getTextDuration());
            
            Thread.sleep(1000);
            cb = cs.chat(session.getSessionId(), "chwila", api_key, true);
            System.out.println("TextDuration: "  + cb.getTextDuration());
            cb = cs.chat(session.getSessionId(), "Siedlce Sokołowska 54 70", api_key,true);
            System.out.println("TextDuration: "  + cb.getTextDuration());
            //SCENRIUSZ
            String topic = cs.chat(session.getSessionId(), "SCENRIUSZ", api_key, true).getTopic();
            System.out.println("topic = " + topic);
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "dzisiaj", api_key, true).getTopic();
            Thread.sleep(1000);            
            cs.chat(session.getSessionId(), "TERMINDOXDNI 09/07/2020", api_key, true).getTopic();
            Thread.sleep(1000);
            session =  cs.removeSession(session.getSessionId(),api_key, "windykacja");  
            
            System.out.println("removeSession session = " + session.getSessionId());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
