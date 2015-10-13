package joanbempong.android;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;

/**
 * Created by Joan Bempong on 10/13/2015.
 */
public class ACEPattern {
    private Integer index;
    private static ACEPattern instance = null;
    private PHHueSDK phHueSDK = PHHueSDK.getInstance();
    private PHBridge bridge = phHueSDK.getSelectedBridge();
    private HueController hueController = HueController.getInstance();
    private ArrayList<String> colors = new ArrayList<>();

    private ACEPattern(){
        colors.add("red");
        colors.add("orange");
        colors.add("yellow");
        colors.add("green");
        colors.add("blue");
        colors.add("purple");
        colors.add("pink");
    }

    public static ACEPattern getInstance() {
        if(instance == null) {
            instance = new ACEPattern();
        }

        return instance;
    }

    public static ACEPattern create() {
        return getInstance();
    }

    public Integer getIndex(){
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public void incrementIndex() {
        this.index++;
    }

    public ArrayList<String> getColors(){
        return this.colors;
    }

    public void nonePattern(PHLight light, int color) {
        PHLightState state = new PHLightState();
        state.setOn(hueController.getToggle());
        bridge.updateLightState(light, state);
        state.setBrightness(255);
        bridge.updateLightState(light, state);
        if (light.supportsColor()){
            state.setHue(color);
            bridge.updateLightState(light, state);
        }
    }

    public void colorPattern(PHLight light){
        ArrayList<String> list = getColors();
        int size = list.size();
        if (getIndex() >= size){
            setIndex(0);
        }
        String color = list.get(index);
        int val = getHueValue(color);
        if (light.supportsColor()) {
            PHLightState state = new PHLightState();
            state.setOn(true);
            bridge.updateLightState(light, state);
            state.setHue(val);
            bridge.updateLightState(light, state);
            state.setBrightness(255);
            bridge.updateLightState(light, state);
        }
    }

    public void shortOnPattern(PHLight light, int val, int color){
        PHLightState state = new PHLightState();
        state.setOn(true);
        bridge.updateLightState(light, state);
        if (light.supportsColor()) {
            state.setHue(color);
            bridge.updateLightState(light, state);
        }
        state.setBrightness(255);
        bridge.updateLightState(light, state);
        try {
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }state.setBrightness(0);
        bridge.updateLightState(light, state);
        state.setOn(false);
        bridge.updateLightState(light, state);
    }

    public void longOnPattern(PHLight light, int val, int color){
        PHLightState state = new PHLightState();
        state.setOn(false);
        bridge.updateLightState(light, state);
        try {
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        state.setOn(true);
        bridge.updateLightState(light, state);
        if (light.supportsColor()) {
            state.setHue(color);
            bridge.updateLightState(light, state);
        }
        state.setBrightness(255);
        bridge.updateLightState(light, state);
    }

    /*public void pulsePattern(PHLight light, int color) {
        PHLightState state = new PHLightState();
        if(!light.getLastKnownLightState().isOn()) {
            state.setOn(true);
            bridge.updateLightState(light, state);
        }
        if (light.getLastKnownLightState().getBrightness().equals(255)) {
            state.setBrightness(127);
            bridge.updateLightState(light, state);
        }
        else{
            state.setBrightness(255);
            bridge.updateLightState(light, state);
        }
        if (light.supportsColor()){
            state.setHue(color);
            bridge.updateLightState(light, state);
        }
    }*/

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
