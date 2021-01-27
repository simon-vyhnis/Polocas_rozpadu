package core;

import components.Layout;
import components.View;
import exceptions.InvalidViewReferenceException;
import exceptions.MissingAttributeException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import views.ButtonView;
import views.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LayoutParser {
    private final File layoutFile;
    private final JUIXApplication application;
    public LayoutParser(File layoutFile, JUIXApplication application){
        this.layoutFile = layoutFile;
        this.application = application;
    }
    /**
     * Method for parsing the root layout
     */
    public Layout parseFile() throws InvalidViewReferenceException, JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document xmlFile;
        try {
            xmlFile = builder.build(layoutFile);
            Element root = xmlFile.getRootElement();
            Class<?> clazz = Class.forName(root.getName());
            Constructor<?> constructor = clazz.getConstructor(Element.class, Layout.class, LayoutParser.class, JUIXApplication.class);
            return (Layout)constructor.newInstance(root, null, this, application);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new InvalidViewReferenceException(layoutFile.getAbsolutePath(),"Root Layout");
        }
    }

    /**
     *
     * @param root
     * @param layout
     * @return list of views in layout
     * @throws MissingAttributeException
     * @throws InvalidViewReferenceException
     *
     * Used to parse layouts in layout
     */
    public Map<String,View> parseLayout(Element root, Layout layout) throws MissingAttributeException, InvalidViewReferenceException {
        ViewMap<String, View> result = new ViewMap<>();
        for (Element child:root.getChildren()) {
            result.put(parseView(child, layout));
        }
        System.out.println("PARSING_LAYOUT: "+root.getName());
        return  result;
    }

    /**
     *
     * @param attributeName
     * @param xml
     * @param file
     * @throws MissingAttributeException
     *
     * Method for checking if view has all needed attributes
     */
    private void checkAttribute(String attributeName, Element xml, File file) throws MissingAttributeException {
        if(xml.getAttribute(attributeName)==null){
            throw new MissingAttributeException(file.getAbsolutePath(), xml.getName(), attributeName);
        }
    }

    /**
     *
     * @param child
     * @param layout
     * @return view with id as key
     * @throws MissingAttributeException
     * @throws InvalidViewReferenceException
     *
     * Creates view from XML file
     */

    private Map.Entry<String, View> parseView(Element child, Layout layout) throws MissingAttributeException, InvalidViewReferenceException {
        Map.Entry<String, View> result;
        checkAttribute("id", child, layoutFile);
        checkAttribute("height", child, layoutFile);
        checkAttribute("width", child, layoutFile);
        switch (child.getName()){
            case "Text":
                result = new AbstractMap.SimpleEntry<>(child.getAttributeValue("id"),new TextView(child, layout));
                break;
            case "Button":
                result = new AbstractMap.SimpleEntry<>(child.getAttributeValue("id"), new ButtonView(child, layout));
                break;
            default:
                try {
                    Class<?> clazz = Class.forName(child.getName());
                    View view;
                    if(clazz.getDeclaredConstructors()[0].getParameterCount()==2) {
                        Constructor<?> constructor = clazz.getConstructor(Element.class, Layout.class);
                        view = (View) constructor.newInstance(child, layout);
                    }else{
                        Constructor<?> constructor = clazz.getConstructor(Element.class, Layout.class, LayoutParser.class, JUIXApplication.class);
                        view = (Layout) constructor.newInstance(child, layout, this, application);
                    }
                    result = new AbstractMap.SimpleEntry<>(child.getAttributeValue("id"),view);

                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new InvalidViewReferenceException(layoutFile.getAbsolutePath(),child.getName());
                }
                break;
        }
        return result;

    }
    /**
     *  Customized HashMap for storing views
     */
    public static class ViewMap<K,V> extends HashMap<K,V> implements KeyInsertableMap<K,V> {}
    public interface KeyInsertableMap<K,V> extends Map<K,V> {
            /**
             * Puts a whole entry containing a key-value pair to the map.
             */
            default V put(Entry<K, V> entry) {
                return put(entry.getKey(), entry.getValue());
            }
        }

}
