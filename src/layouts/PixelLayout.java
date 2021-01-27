package layouts;

import components.Bounds;
import components.Layout;
import components.View;
import core.JUIXApplication;
import core.LayoutParser;
import exceptions.InvalidDimensionException;
import org.jdom2.Element;

public class PixelLayout extends Layout {
    private static final int DIMENSION_X = 0;
    private static final int DIMENSION_Y = 1;
    private static final int DIMENSION_WIDTH = 2;
    private static final int DIMENSION_HEIGHT = 3;
    public PixelLayout(Element xml, Layout layout, LayoutParser parser, JUIXApplication application) {
        super(xml, layout, parser, application);
    }

    public void parseViews(){
        getViews().forEach((k,v)->{
            try {
                v.setAbsoluteSize(parseWholeDimension(v.getRawWidth(),DIMENSION_WIDTH, v), parseWholeDimension(v.getRawHeight(), DIMENSION_HEIGHT, v));
                v.setAbsolutePosition(parseWholeDimension(v.getRawX(),DIMENSION_X, v), parseWholeDimension(v.getRawY(),DIMENSION_Y, v));
            } catch (InvalidDimensionException e) {
                e.printStackTrace();
            }
        });
    }
    public int parseWholeDimension(String rawDimension, int dimensionType, View view) throws InvalidDimensionException {
        if(rawDimension.contains("+")){
            String[] singleDimensions  = rawDimension.split("\\+");
            return parseSingleDimension(singleDimensions[0], dimensionType, view) + parseSingleDimension(singleDimensions[1],dimensionType, view);
        }else{
            return parseSingleDimension(rawDimension, dimensionType, view);
        }
    }
    public int parseSingleDimension(String singleRawDimension, int dimensionType, View view) throws InvalidDimensionException {
        try {
            //Pixel dimension
            if(singleRawDimension.contains("px")){
                return parsePixelsValue(singleRawDimension);
            //Percent dimension
            }else if(singleRawDimension.contains("%")) {
                if (dimensionType == DIMENSION_WIDTH || dimensionType == DIMENSION_X) {
                    return parsePercentDimensionX(singleRawDimension);
                } else {
                    return parsePercentDimensionY(singleRawDimension);
                }
            //Center Dimension
            }else if(singleRawDimension.equals("center")){
                if (dimensionType == DIMENSION_X) {
                    return parsePercentDimensionX("50%")-(view.getAbsoluteWidth()/2);
                } else if(dimensionType == DIMENSION_Y) {
                    return parsePercentDimensionY("50%") - (view.getAbsoluteHeight() / 2);
                }else{
                    throw new InvalidDimensionException("Invalid dimension: "+singleRawDimension+" view: "+view.getId());
                }
            //Content dimension
            }else if(singleRawDimension.equals("content")) {
                if (dimensionType == DIMENSION_WIDTH) {
                    return view.getContentWidth();
                } else if (dimensionType == DIMENSION_HEIGHT) {
                    return view.getContentHeight();
                } else {
                    throw new InvalidDimensionException("Invalid dimension: " + singleRawDimension + " view: " + view.getId());
                }
            }else{
                throw new InvalidDimensionException("Invalid dimension: "+singleRawDimension+" view: "+view.getId());
            }
        }catch (NumberFormatException e){
            throw new InvalidDimensionException("Invalid dimension: "+singleRawDimension+" view: "+view.getId());
        }
    }

    public int parsePercentDimensionY(String percentDimension) {
        int percent = Integer.parseInt(percentDimension.replace("%", ""));
        return application.getWindowHeight()/ 100 * percent;
    }

    public int parsePercentDimensionX(String percentDimension) {
        int percent = Integer.parseInt(percentDimension.replace("%", ""));
        return application.getWindowWidth()/ 100 * percent;
    }

    public int parsePixelsValue(String pixelDimension) {
        return Integer.parseInt(pixelDimension.replace("px", ""));
    }

    @Override
    public void notifyViewsChanged() {
        parseViews();
    }

    @Override
    public int getContentWidth() {
        return 0;
    }

    @Override
    public int getContentHeight() {
        return 0;
    }
}
