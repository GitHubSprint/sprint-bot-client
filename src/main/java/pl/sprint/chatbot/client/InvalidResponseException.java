package pl.sprint.chatbot.client;

public class InvalidResponseException extends Exception {
    public InvalidResponseException(String errorMessage) {
        super(errorMessage);
    }
}
