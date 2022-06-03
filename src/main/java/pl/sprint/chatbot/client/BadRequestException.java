package pl.sprint.chatbot.client;

public class BadRequestException extends Exception
{
    public BadRequestException(String message)
    {
        super(message);
    }
}
