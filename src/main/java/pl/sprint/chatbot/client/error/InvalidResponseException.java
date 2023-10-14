package pl.sprint.chatbot.client.error;

public class InvalidResponseException extends Exception {
    public InvalidResponseException(String errorMessage) {
        super(errorMessage);
    }
}
