package exceptions;

public class InvalidViewReferenceException extends Exception {
    public InvalidViewReferenceException(String layoutPath, String viewName){
        super("ERROR WHILE PARSING LAYOUT: "+layoutPath+ " Invalid view reference "+viewName);
    }
}
