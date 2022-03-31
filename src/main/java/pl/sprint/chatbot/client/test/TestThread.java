/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.test;

import java.util.HashMap;
import java.util.Map;
import pl.sprint.chatbot.client.model.ChatBot;
import pl.sprint.chatbot.client.service.SprintBotClient;
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
            testBot();             
    }
    
    private void testBot()
    {
        try {
                    
                        
            SprintBotClient cs = new SprintBotClient(endpoint);
            
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
            Session session = cs.openSession(api_key, "windykacja","CHAT","TEST",map,null);
            
            
            Map<String,String> respMap = cs.getSessionData(session.getSessionId()); 
            
            respMap.forEach((key, value) -> System.out.println("respMap: " + key + ":" + value));
            
            Thread.sleep(1000);
            ChatBot cb = cs.chat(session.getSessionId(), "STARTV PAN GODLEWSKI-ADAM 175 00 1 21-01-2022", api_key, true);
            
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
            
            for(int i=0;i<5;i++)
            {
                map.put(String.valueOf(i), String.valueOf(i));
                cs.updateData(session.getSessionId(), map);
                
                Map<String, String> ret = cs.getSessionData(session.getSessionId());
                
                System.out.println("RESP MAP DATA:\n" + ret);
            }
            
            
            session =  cs.closeSession(session.getSessionId(),api_key, "windykacja");  
            
            System.out.println("removeSession session = " + session.getSessionId());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
