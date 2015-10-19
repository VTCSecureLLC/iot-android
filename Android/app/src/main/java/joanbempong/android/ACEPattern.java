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
    private ArrayList<String> colors = new ArrayList<>();
    private boolean nonePatternToggle = false;
    private boolean useThisPattern = false;
    private boolean patternInterrupted = false;

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

    public Boolean getPatternToggle(){
        return this.nonePatternToggle;
    }

    public void setPatternToggle() {
        this.nonePatternToggle = !this.nonePatternToggle;
    }

    public Boolean getUseThisPattern(){
        return this.useThisPattern;
    }

    public Boolean getPatternInterrupted(){
        return this.patternInterrupted;
    }

    public void setPatternInterrupted(boolean b) {
        this.patternInterrupted = b;
    }

    public void setUseThisPattern(boolean b) {
        this.useThisPattern = b;
    }


    /*public void nonePattern(PHLight light, int color) {
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
    }*/

    public void nonePattern(final PHLight light, final Boolean repeat) {
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern) {
                                PHLightState state = new PHLightState();
                                state.setOn(getPatternToggle());
                                bridge.updateLightState(light, state);
                                state.setBrightness(255);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                setPatternToggle();
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while (total < 3) {
                                PHLightState state = new PHLightState();
                                state.setOn(getPatternToggle());
                                bridge.updateLightState(light, state);
                                state.setBrightness(255);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                setPatternToggle();
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void shortOnPattern(final PHLight light, final Boolean repeat) {
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern) {
                                PHLightState state = new PHLightState();
                                state.setOn(true);
                                bridge.updateLightState(light, state);
                                state.setBrightness(255);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                state.setBrightness(0);
                                bridge.updateLightState(light, state);
                                state.setOn(false);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while (total < 3) {
                                PHLightState state = new PHLightState();
                                state.setOn(true);
                                bridge.updateLightState(light, state);
                                state.setBrightness(255);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                state.setBrightness(0);
                                bridge.updateLightState(light, state);
                                state.setOn(false);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void longOnPattern(final PHLight light, final Boolean repeat){
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat){
                            while (useThisPattern){
                                PHLightState state = new PHLightState();
                                state.setOn(true);
                                bridge.updateLightState(light, state);
                                state.setBrightness(255);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                state.setBrightness(0);
                                bridge.updateLightState(light, state);
                                state.setOn(false);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                        else{
                            int total = 0;
                            while (total < 3) {
                                PHLightState state = new PHLightState();
                                state.setOn(true);
                                bridge.updateLightState(light, state);
                                state.setBrightness(255);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                state.setBrightness(0);
                                bridge.updateLightState(light, state);
                                state.setOn(false);
                                bridge.updateLightState(light, state);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void colorPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("red"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("yellow"));
        colorPatternList.add((Double[]) colorsList.get("green"));
        colorPatternList.add((Double[]) colorsList.get("blue"));
        colorPatternList.add((Double[]) colorsList.get("purple"));
        colorPatternList.add((Double[]) colorsList.get("pink"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void firePattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("red"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("yellow"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void ritPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("black"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("brown"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void cloudySkyPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("blue"));
        colorPatternList.add((Double[]) colorsList.get("white"));
        colorPatternList.add((Double[]) colorsList.get("deepSkyBlue"));
        colorPatternList.add((Double[]) colorsList.get("skyBlue"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void grassyGreenPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("green"));
        colorPatternList.add((Double[]) colorsList.get("darkOliveGreen"));
        colorPatternList.add((Double[]) colorsList.get("seaGreen"));
        colorPatternList.add((Double[]) colorsList.get("lightGreen"));
        colorPatternList.add((Double[]) colorsList.get("springGreen"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void lavenderPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("purple"));
        colorPatternList.add((Double[]) colorsList.get("lavender"));
        colorPatternList.add((Double[]) colorsList.get("plum"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void bloodyRedPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("red"));
        colorPatternList.add((Double[]) colorsList.get("crimson"));
        colorPatternList.add((Double[]) colorsList.get("darkRed"));
        colorPatternList.add((Double[]) colorsList.get("fireBrick"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }
    public void springMistPattern(final PHLight light, final Boolean repeat){
        ACEColors colors = ACEColors.getInstance();
        Map colorsList = colors.getColorsList();
        final List<Double[]> colorPatternList = new ArrayList<>();
        colorPatternList.add((Double[]) colorsList.get("aqua"));
        colorPatternList.add((Double[]) colorsList.get("lightGreen"));
        colorPatternList.add((Double[]) colorsList.get("orange"));
        colorPatternList.add((Double[]) colorsList.get("lightPink"));
        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeat) {
                            while (useThisPattern && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        } else {
                            int total = 0;
                            while ((total < 3) && !getPatternInterrupted()) {
                                for (Double[] color : colorPatternList) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(true);
                                    bridge.updateLightState(light, state);
                                    state.setX(Float.valueOf(String.valueOf(color[0])));
                                    bridge.updateLightState(light, state);
                                    state.setY(Float.valueOf(String.valueOf(color[1])));
                                    bridge.updateLightState(light, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(light, state);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total++;
                            }
                            cancel();
                            hueController.restoreAllLightStates();
                        }
                    }
                }, 0, 1000);
            }
        }).start();
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
