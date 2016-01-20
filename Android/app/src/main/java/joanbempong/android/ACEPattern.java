package joanbempong.android;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joan Bempong on 10/13/2015.
 */
public class ACEPattern {
    private Integer index;
    private static ACEPattern instance = null;
    private PHHueSDK phHueSDK = PHHueSDK.getInstance();
    private PHBridge bridge = phHueSDK.getSelectedBridge();
    private HueController hueController = HueController.getInstance();
    private ArrayList<String> colors = new ArrayList<String>();
    private boolean nonePatternToggle = false;
    private boolean useThisPattern = false;
    private boolean patternInterrupted = false;

    private ACEPattern() {
        colors.add("red");
        colors.add("orange");
        colors.add("yellow");
        colors.add("green");
        colors.add("blue");
        colors.add("purple");
        colors.add("pink");
    }

    public static ACEPattern getInstance() {
        if (instance == null) {
            instance = new ACEPattern();
        }

        return instance;
    }

    public static ACEPattern create() {
        return getInstance();
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public Boolean getPatternToggle() {
        return this.nonePatternToggle;
    }

    public void setPatternToggle() {
        this.nonePatternToggle = !this.nonePatternToggle;
    }

    public Boolean getPatternInterrupted() {
        return this.patternInterrupted;
    }

    public void setPatternInterrupted(boolean b) {
        this.patternInterrupted = b;
    }

    public void setUseThisPattern(boolean b) {
        this.useThisPattern = b;
    }

    public void changeState(PHLight light, Boolean stateOn, Double[] color, long sleep) {
        PHLightState state = new PHLightState();
        if (stateOn) {
            state.setOn(stateOn);
            bridge.updateLightState(light, state);
            state.setBrightness(255);
            bridge.updateLightState(light, state);
            state.setTransitionTime(8);
            bridge.updateLightState(light, state);
            if (light.supportsColor()) {
                state.setX(Float.valueOf(String.valueOf(color[0])));
                bridge.updateLightState(light, state);
                state.setY(Float.valueOf(String.valueOf(color[1])));
                bridge.updateLightState(light, state);
            }
        } else {
            state.setBrightness(0);
            bridge.updateLightState(light, state);
            state.setOn(stateOn);
            bridge.updateLightState(light, state);
        }
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void threadForColorPatterns(final PHLight light, final Boolean repeat, final long sleep,
                                       final List<Double[]> colorPatternList, final Double[] color ) {
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            int index = 0;
                            while (useThisPattern && !getPatternInterrupted()) {
                                Double[] color = colorPatternList.get(index);
                                changeState(light, true, color, sleep);
                                index++;
                                if (index >= colorPatternList.size()) {
                                    index = 0;
                                }
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        } else {
                            int total = 0;
                            int index = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                Double[] color = colorPatternList.get(index);
                                changeState(light, true, color, sleep);
                                index++;
                                if (index >= colorPatternList.size()) {
                                    index = 0;
                                    total++;
                                }
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }

    public void checkCurrentCircumstance(Boolean repeat, Double[] color) {
        if (repeat) {
            if (hueController.getOnCall()) {
                System.out.println("a call has been answered");
                hueController.turnOnOnCallLights();
            } else if (hueController.getNewMissedCall()) {
                System.out.println("a call has been missed");
                hueController.simulateAMissedCall(color);
            } else {
                System.out.println("a call has been declined");
                hueController.restoreAllLightStates();
            }
        }
        else{
            hueController.restoreAllLightStates();
        }
    }

    public void nonePattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color) {
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                changeState(light, getPatternToggle(), color, sleep);
                                setPatternToggle();
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        } else {
                            int total = 0;
                            while ((total < 4) && !getPatternInterrupted()) {
                                changeState(light, getPatternToggle(), color, sleep);
                                setPatternToggle();
                                total++;
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }

    public void shortOnPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color) {
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                changeState(light, true, color, sleep);
                                changeState(light, false, color, sleep*3);
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                changeState(light, true, color, sleep);
                                changeState(light, false, color, sleep*3);
                                total++;
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void longOnPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat){
                            while (useThisPattern && !getPatternInterrupted()) {
                                changeState(light, true, color, sleep*3);
                                changeState(light, false, color, sleep);
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        }
                        else{
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                changeState(light, true, color, sleep*3);
                                changeState(light, false, color, sleep);
                                total++;
                            }
                            cancel();
                            checkCurrentCircumstance(repeat, color);
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void colorPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("red"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("gold"));
        colorPatternList.add((Double[]) colorsList.get("green"));
        colorPatternList.add((Double[]) colorsList.get("deepSkyBlue"));
        colorPatternList.add((Double[]) colorsList.get("purple"));
        colorPatternList.add((Double[]) colorsList.get("pink"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void firePattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("red"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("yellow"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void ritPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("black"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("brown"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void cloudySkyPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("blue"));
        colorPatternList.add((Double[]) colorsList.get("white"));
        colorPatternList.add((Double[]) colorsList.get("deepSkyBlue"));
        colorPatternList.add((Double[]) colorsList.get("skyBlue"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void grassyGreenPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("green"));
        colorPatternList.add((Double[]) colorsList.get("darkOliveGreen"));
        colorPatternList.add((Double[]) colorsList.get("seaGreen"));
        colorPatternList.add((Double[]) colorsList.get("lightGreen"));
        colorPatternList.add((Double[]) colorsList.get("springGreen"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void lavenderPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("purple"));
        colorPatternList.add((Double[]) colorsList.get("lavender"));
        colorPatternList.add((Double[]) colorsList.get("plum"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void bloodyRedPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("red"));
        colorPatternList.add((Double[]) colorsList.get("crimson"));
        colorPatternList.add((Double[]) colorsList.get("darkRed"));
        colorPatternList.add((Double[]) colorsList.get("fireBrick"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
    public void springMistPattern(final PHLight light, final Boolean repeat, final long sleep, final Double[] color){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<Double[]>();
        colorPatternList.add((Double[]) colorsList.get("aqua"));
        colorPatternList.add((Double[]) colorsList.get("lightGreen"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("lightPink"));
        threadForColorPatterns(light, repeat, sleep, colorPatternList, color);
    }
}
