package client;

public class ClientIdException extends Exception{
    @Override
    public String getMessage() {
        return "You entered an incorrect client id";
    }
}
