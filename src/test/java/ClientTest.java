import org.junit.jupiter.api.Test;

public class ClientTest {

    private final static String ENDPOINT =  "https://192.168.254.64:8443/api";
    private final static String API_KEY = "Sprint";

    @Test
    public void Test() throws Exception {
        System.out.println(ENDPOINT);
        TestChat test = new TestChat(ENDPOINT, API_KEY);
        test.start();
    }

}
