package joanbempong.android;

import java.util.ArrayList;

/**
 * Created by Joan Bempong on 10/8/2015.
 */
public class MyChoices {
    //declaring variables
    private static MyChoices instance = null;
    private ArrayList<String> durationList = new ArrayList<>();
    private ArrayList<String> flashPatternList = new ArrayList<>();
    private ArrayList<String> flashRateList = new ArrayList<>();
    private ArrayList<String> colorList = new ArrayList<>();

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
        switch (color){
            case "warm white": return 12750;
            case "red" : return 0;
            case "orange" : return 6375;
            case "yellow" : return 12750;
            case "green" : return 25500;
            case "blue" : return 46920;
            case "purple" : return 50100;
            case "pink" : return 61100;
        }
        return -1;
    }
}
