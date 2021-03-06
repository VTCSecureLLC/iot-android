package joanbempong.android;

import org.linphone.R;
import java.util.ArrayList;

/**
 * Created by Joan Bempong on 10/8/2015.
 */
public class MyChoices {
    //declaring variables
    private static MyChoices instance = null;
    private ArrayList<String> durationList = new ArrayList<String>();
    private ArrayList<String> flashPatternList = new ArrayList<String>();
    private ArrayList<String> flashRateList = new ArrayList<String>();
    private ArrayList<String> colorList = new ArrayList<String>();

    private MyChoices() {
        durationList.add("--");
        durationList.add("1");
        durationList.add("5");
        durationList.add("10");
        durationList.add("15");
        durationList.add("30");
        durationList.add("45");
        durationList.add("60");
        durationList.add("Always On (full brightness)");
        durationList.add("Always On (energy saving)");

        flashPatternList.add("--");
        flashPatternList.add("None");
        flashPatternList.add("Short On");
        flashPatternList.add("Long On");
        flashPatternList.add("Color");
        flashPatternList.add("Fire");
        flashPatternList.add("RIT");
        flashPatternList.add("Cloudy Sky");
        flashPatternList.add("Grassy Green");
        flashPatternList.add("Lavender");
        flashPatternList.add("Bloody Red");
        flashPatternList.add("Spring Mist");

        flashRateList.add("--");
        flashRateList.add("1.5");
        flashRateList.add("2.5");
        flashRateList.add("3.5");

        ACEColors colors = ACEColors.getInstance();
        colorList.add("--");
        for (String colorName : colors.getColorsList().keySet()){
            colorList.add(colorName);
        }
    }

    public static MyChoices getInstance() {
        if(instance == null) {
            instance = new MyChoices();
        }
        return instance;
    }

    public static MyChoices create() {
        return getInstance();
    }

    public ArrayList<String> getDurationList(){
        return this.durationList;
    }

    public ArrayList<String> getFlashPatternList(){
        return this.flashPatternList;
    }

    public ArrayList<String> getFlashRateList(){
        return this.flashRateList;
    }

    public ArrayList<String> getColorList(){
        return this.colorList;
    }

    public int getHueValue(String color){
            if(color.equals("warm white")) return 12750;
             if(color.equals("red")) return 0;
            if(color.equals( "orange" )) return 6375;
            if(color.equals( "yellow" )) return 12750;
            if(color.equals( "green" )) return 25500;
            if(color.equals( "blue" )) return 46920;
            if(color.equals( "purple")) return 50100;
            if(color.equals( "pink" )) return 61100;
        return -1;
    }
}
