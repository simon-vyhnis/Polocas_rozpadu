package core;


import exceptions.InvalidAttributeException;
import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.awt.*;

public class AttributesParser {
    private final Element xml;
    private final JUIXApplication application;

    public AttributesParser(Element xml, JUIXApplication application) {
        this.xml = xml;
        this.application = application;
    }

    public int getIntValue(String name, int defaultValue) throws InvalidAttributeException {
        Attribute attribute = xml.getAttribute(name);
        if(attribute != null && attribute.isSpecified()){
            try {
                return attribute.getIntValue();
            } catch (DataConversionException e) {
                e.printStackTrace();
                throw new InvalidAttributeException(
                        "Invalid attribute: "+attribute.getName()+
                        " view: "+xml.getAttributeValue("id"));
            }
        }else {
            return defaultValue;
        }
    }

    public float getFloatValue(String name, int defaultValue) throws InvalidAttributeException {
        Attribute attribute = xml.getAttribute(name);
        if(attribute != null && attribute.isSpecified()){
            try {
                return attribute.getFloatValue();
            } catch (DataConversionException e) {
                e.printStackTrace();
                throw new InvalidAttributeException(
                        "Invalid attribute: "+attribute.getName()+
                        " view: "+xml.getAttributeValue("id"));
            }
        }else {
            return defaultValue;
        }
    }

    public boolean getBooleanValue(String name, boolean defaultValue) throws InvalidAttributeException {
        Attribute attribute = xml.getAttribute(name);
        if(attribute != null && attribute.isSpecified()){
            try {
                return attribute.getBooleanValue();
            } catch (DataConversionException e) {
                e.printStackTrace();
                throw new InvalidAttributeException(
                        "Invalid attribute: "+attribute.getName()+
                                " view: "+xml.getAttributeValue("id"));
            }
        }else {
            return defaultValue;
        }
    }


    public String getStringValue(String name, String defaultValue){
        Attribute attribute = xml.getAttribute(name);
        if (attribute != null && attribute.isSpecified()){
            return attribute.getValue();
        }else {
            return defaultValue;
        }
    }

    public int getStringOptionsValue(String name, String[] options, int defaultValue) throws InvalidAttributeException {
        Attribute attribute = xml.getAttribute(name);
        if (attribute != null && attribute.isSpecified()){
            for (int i=0; i<options.length;i++) {
                if (options[i].equals(attribute.getValue())){
                    return i;
                }
            }
            throw new InvalidAttributeException(
                    "Invalid attribute: "+attribute.getName()+
                    " view: "+xml.getAttributeValue("id"));
        }else {
            return defaultValue;
        }
    }

    public Color getColor(String name, Color defaultColor) throws InvalidAttributeException {
        Attribute attribute = xml.getAttribute(name);
        if (attribute != null && attribute.isSpecified()){
             Color result = application.getColors().getColor(attribute.getValue());
             if(result == null){
                 throw new InvalidAttributeException("Invalid color: "+attribute.getValue()+" view: "+xml.getAttributeValue("id"));
             }else {
                 return result;
             }
        }
        return defaultColor;
    }




}
