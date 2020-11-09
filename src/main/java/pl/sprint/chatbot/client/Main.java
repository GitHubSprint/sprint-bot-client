/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client;

import pl.sprint.chatbot.client.service.ClientService;
import java.io.IOException;
import pl.sprint.chatbot.client.test.TestThread;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import pl.sprint.chatbot.client.test.TestThreadNoClose;
import pl.sprint.chatbot.client.test.TestThreadBotWithData;

/**
 * Main Class for SprintBot stress tests.
 * @author skost
 */
public class Main
{    
    private final static String ENDPOINT =  "http://192.168.254.159:8080/api";
    private final static String API_KEY = "Sprint";
    
    public static void main(String[] args) throws InterruptedException, IOException
    {
        ClientService cs = new ClientService(ENDPOINT);
                                        
        for(int i=0; i < 100; i++)
        {     
            
            TestThread m1=new TestThread(ENDPOINT, API_KEY);  
            Thread t1 =new Thread(m1);  
            t1.start(); 
            //Thread.sleep(1000);
            
            
            
            TestThreadBotWithData m2=new TestThreadBotWithData(ENDPOINT, API_KEY);  
            Thread t2 =new Thread(m2);  
            t2.start(); 
            //Thread.sleep(1000);
                                    
            
            TestThreadNoClose m3=new TestThreadNoClose(ENDPOINT, API_KEY);  
            Thread t3 =new Thread(m3);  
            t3.start(); 
            //Thread.sleep(1000);
            
            int cnt = cs.countSessions().getCount();
            System.out.println("cnt: " + cnt);
            //cnt = cs.countSessions("TEST").getCount();
            //System.out.println("TEST cnt: " + cnt );
            
            cnt = cs.countSessions("CHAT").getCount();
            System.out.println("CHAT cnt: " + cnt );
            
            cnt = cs.countSessions("VOICE").getCount();
            System.out.println("VOICE cnt: " + cnt );
        }
        
    }
    
    
    /**
     * Reoves SSL veryfication
     */
    private static void disableSslVerification() 
    {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
