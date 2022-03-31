/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client.test;

import java.util.HashMap;
import java.util.Map;
import pl.sprint.chatbot.client.service.SprintBotClient;
import pl.sprint.chatbot.client.model.Session;

/**
 * Test with many Bot Data. 
 * @author Sławomir Kostrzewa
 */
public class TestThreadBotWithData implements Runnable
{
    private String api_key = "Sprint";
    private String endpoint =  "http://192.168.254.191/api";
    
    public TestThreadBotWithData(String endpoint, String api_key)
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
            Session session = cs.openSession(api_key, "vectra","CHAT","TEST",map,null);
            //cs.getData(session.getSessionId()).getEntity(String.class); 
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "START", api_key, true).getText();
            Thread.sleep(1000);
            
            cs.chat(session.getSessionId(), "witam", api_key, true).getTopic();
            
            
            Thread.sleep(2000);
            cs.chat(session.getSessionId(), "tv", api_key, true).getTopic();
            cs.chat(session.getSessionId(), "z kim rozmawiam", api_key, true).getTopic();
            //SCENRIUSZ
            cs.chat(session.getSessionId(), "chwila", api_key, true).getTopic();
            Thread.sleep(1000);
            cs.chat(session.getSessionId(), "cześć", api_key, true).getTopic();
            Thread.sleep(1000);            
            cs.chat(session.getSessionId(), "koniec", api_key, true).getTopic();
            Thread.sleep(2000);
            cs.closeSession(session.getSessionId(),api_key, "vectra");
                                    
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
