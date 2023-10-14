import org.junit.jupiter.api.Test;

public class ClientTest {

    private final static String ENDPOINT =  "https://demo.sprintbot.ai:8443/api";
    private final static String API_KEY = null;

    @Test
    public void Test() throws Exception {

        if(API_KEY == null)
            return;


        TestThread test = new TestThread(ENDPOINT, API_KEY);
        test.start();
    }

}
