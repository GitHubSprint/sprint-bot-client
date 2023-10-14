package pl.sprint.chatbot.client.error;

public class BadRequestException extends Exception
{
    public BadRequestException(String message)
    {
        super(message);
    }
}
