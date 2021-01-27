package exceptions;

public class MissingAttributeException extends Exception{

    public MissingAttributeException(String layoutPath, String childName, String attributeName){
        super("ERROR WHILE PARSING LAYOUT: "+layoutPath+ " Missing attribute "+attributeName+" in "+childName);
    }
}
