package core;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class JUIXColors {
    private final HashMap<String, Color> colors;
    private Element root;
    public JUIXColors(URL colorsFile){
        colors = new HashMap<>();
        SAXBuilder builder = new SAXBuilder();
        Document xmlFile;
        try {
            xmlFile = builder.build(colorsFile);
            root = xmlFile.getRootElement();
            for (Element colorXml : root.getChildren()){
                colors.put(colorXml.getAttributeValue("name"),
                        Color.decode(colorXml.getAttributeValue("value")));
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

    }

    public Color getColor(String name){
        return colors.get(name);
    }

    public void changeColor(String name, Color newValue){
        colors.put(name,newValue);
        String hex = String.format("#%02x%02x%02x", newValue.getRed(), newValue.getGreen(), newValue.getBlue());
        for (Element colorXml : root.getChildren()){
            if (colorXml.getAttributeValue("name").equals(name)){
                colorXml.setAttribute("color",hex);
            }
        }
    }
}
