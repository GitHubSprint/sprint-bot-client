/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sprint.chatbot.client;

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
import pl.sprint.chatbot.client.test.TestThreadVectra;

/**
 *
 * @author skost
 */
public class Main
{
    //private static String endpoint =  "http://192.168.254.191/api";
    private static String endpoint =  "http://192.168.1.100/api";
    private final static String API_KEY = "Sprint";
    
    public static void main(String[] args) throws InterruptedException, IOException
    {
        ClientService cs = new ClientService(endpoint);
        
        
        
        
        
        for(int i=0; i < 100; i++)
        {     
            
            
            TestThread m1=new TestThread(i, endpoint);  
            Thread t1 =new Thread(m1);  
            t1.start(); 
            Thread.sleep(1000);
            
            int cnt = cs.countSessions().getCount();
            System.out.println("cnt: " + cnt);
            
            cnt = cs.countSessions().getCount();
            System.out.println("cnt: " + cnt);
            
            TestThreadVectra m2=new TestThreadVectra(i, endpoint);  
            Thread t2 =new Thread(m2);  
            t2.start(); 
            Thread.sleep(1000);
            
            cnt = cs.countSessions().getCount();
            System.out.println("cnt: " + cnt);
            
            TestThreadNoClose m3=new TestThreadNoClose(i, endpoint);  
            Thread t3 =new Thread(m3);  
            t3.start(); 
            Thread.sleep(1000);
            
            cnt = cs.countSessions().getCount();
            System.out.println("cnt: " + cnt);
        }
        
    }
    
    
    
    private static void disableSslVerification() {
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
