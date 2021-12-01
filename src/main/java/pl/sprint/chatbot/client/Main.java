/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client;

import pl.sprint.chatbot.client.service.ClientService;
import java.io.IOException;
import pl.sprint.chatbot.client.test.TestThread;

/**
 * Main Class for SprintBot stress tests.
 * @author skost
 */
public class Main
{    
    private final static String ENDPOINT =  "https://localhost:8443/api";
    private final static String API_KEY = "Sprint";
    
    public static void main(String[] args) throws InterruptedException, IOException
    {
        
        
        ClientService.disableSslVerification();
        
        ClientService cs = new ClientService(ENDPOINT);
        
        TestThread m1=new TestThread(ENDPOINT, API_KEY);  
        Thread t1 =new Thread(m1);  
        t1.start(); 
        
        
        //cs.addMessageToSend("skoslaw@gmail.com","noreply@sprintbot.ai", "test tematu", "jakaś treść", false, API_KEY, "1231241");
                                        
//        for(int i=0; i < 200; i++)
//        {     
//            
//            TestThread m1=new TestThread(ENDPOINT, API_KEY);  
//            Thread t1 =new Thread(m1);  
//            t1.start(); 
//            
//            
//                      
//            TestThreadBotWithData m2=new TestThreadBotWithData(ENDPOINT, API_KEY);  
//            Thread t2 =new Thread(m2);  
//            t2.start(); 
//            
//                                    
//            
//            TestThreadNoClose m3=new TestThreadNoClose(ENDPOINT, API_KEY);  
//            Thread t3 =new Thread(m3);  
//            t3.start(); 
//            
//            
//            Thread.sleep(1000);
//            
//            int cnt = cs.countSessions().getCount();
//            System.out.println("cnt: " + cnt);
//            //cnt = cs.countSessions("TEST").getCount();
//            //System.out.println("TEST cnt: " + cnt );
//            
//            cnt = cs.countSessions("CHAT").getCount();
//            System.out.println("CHAT cnt: " + cnt );
//            
//            cnt = cs.countSessions("VOICE").getCount();
//            System.out.println("VOICE cnt: " + cnt );
//        }
        
    }
    
    
    

}
