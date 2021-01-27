package exceptions;

public class InvalidPartException extends Exception{
    public InvalidPartException(){
        super("Invalid part! Can't change default Part constructor");
    }
}
