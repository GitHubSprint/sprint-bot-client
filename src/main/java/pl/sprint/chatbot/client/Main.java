/*
 * Copyright © 2022 Sprint S.A.
 * Contact: slawomir.kostrzewa@sprint.pl

 */
package pl.sprint.chatbot.client;

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
    public static void main(String[] args) {
        TestThread test = new TestThread(ENDPOINT, API_KEY);
        test.start();
    }
    
    
    

}
