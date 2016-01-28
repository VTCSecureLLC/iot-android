package joanbempong.android;

import org.linphone.R;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joan Bempong on 10/1/2015.
 */
public class HueController {
    //declaring variables
    private static HueController instance = null;

    //default values for incoming and missed calls
    private List<String> defaultLights = new ArrayList<String>();
    private String defaultDuration, defaultFlashPattern, defaultFlashRate, defaultColor;

    //old default values for incoming and missed calls
    private List<String> oldDefaultLights;
    private String oldDefaultDuration, oldDefaultFlashPattern, oldDefaultFlashRate, oldDefaultColor;


    //Current contact list
    private ArrayList<ACEContact> Contacts = new ArrayList<ACEContact>();

    //old contact information
    private String oldContactFirstName;
    private String oldContactLastName;
    private String oldContactPhoneNumber;
    private String oldContactFlashPattern;
    private String oldContactFlashRate;
    private String oldContactColor;
    private Boolean oldContactUseNotification;
    private Boolean oldContactUseDefaultValues;


    //current light states
    private List<List<String>> LightStates = new ArrayList<List<String>>();
    private List<String> LightState;
    private String brightness;
    private String isOn;
    private String color;
    private String colorX;
    private String colorY;

    private boolean toggle = false;
    private int totalDuration = 0;
    private int totalCommands = 0;
    private boolean callAnswered = false;
    private boolean defaultValues = false;
    private boolean isFlashing = false;
    private boolean newMissedCall = false;
    private boolean onCall = false;
    private boolean timerForMissedCallExists = false;



    private HueController() {
    }

    public static HueController getInstance() {
        if(instance == null) {
            instance = new HueController();
        }

        return instance;
    }

    public static HueController create() {
        return getInstance();
    }

    public void addToDefaultLights(String name){
        defaultLights.add(name);
        System.out.println(name + " added successfully (default)");
    }


    public void removeFromDefaultLights(String name){
        for (Iterator<String> iter = defaultLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + " removed successfully (default)");
            }
        }
    }

    public boolean getToggle(){
        return this.toggle;
    }

    public void setToggle(){
        this.toggle = !this.toggle;
    }

    public String getOldContactFirstName(){
        return this.oldContactFirstName;
    }

    public String getOldContactLastName(){
        return this.oldContactLastName;
    }

    public String getOldContactPhoneNumber(){
        return this.oldContactPhoneNumber;
    }

    public String getOldContactFlashPattern(){
        return this.oldContactFlashPattern;
    }

    public String getOldContactFlashRate(){
        return this.oldContactFlashRate;
    }

    public String getOldContactColor(){
        return this.oldContactColor;
    }

    public Boolean getOldContactUseNotification(){
        return this.oldContactUseNotification;
    }

    public Boolean getOldContactUseDefaultValues(){
        return this.oldContactUseDefaultValues;
    }

    public List<String> getOldDefaultLights(){
        return this.oldDefaultLights;
    }

    public List<String> getDefaultLights(){
        return this.defaultLights;
    }

    public String getDefaultDuration(){
        return this.defaultDuration;
    }

    public String getDefaultFlashPattern(){
        return this.defaultFlashPattern;
    }

    public String getDefaultFlashRate(){
        return this.defaultFlashRate;
    }

    public String getDefaultColor(){
        return this.defaultColor;
    }



    public ArrayList<ACEContact> getContactList(){
        return this.Contacts;
    }

    public boolean getCallAnswered(){
        return this.callAnswered;
    }

    public void setCallAnswered(boolean b){
        this.callAnswered = b;
    }

    public boolean getDefaultValues(){return this.defaultValues;}

    public void setDefaultValues(boolean b){this.defaultValues = b;}

    //save default values
    public void saveDefaultValues(String duration, String flashPattern, String flashRate, String color){
        defaultLights = this.defaultLights;
        defaultDuration = duration;
        defaultFlashPattern = flashPattern;
        defaultFlashRate = flashRate;
        defaultColor = color;
    }

    public void setCleanDefaultLights(){
        defaultLights = new ArrayList<String>();
    }

   //save current default values
    public void saveCurrentDefaultValues(){
        oldDefaultLights = defaultLights;
        oldDefaultDuration = defaultDuration;
        oldDefaultFlashPattern = defaultFlashPattern;
        oldDefaultFlashRate = defaultFlashRate;
        oldDefaultColor = defaultColor;
    }

    public void setNewMissedCall(Boolean b){
        this.newMissedCall = b;
    }

    public Boolean getNewMissedCall(){
        return this.newMissedCall;
    }

    public void setTimerForMissedCallExists(Boolean b){
        this.timerForMissedCallExists = b;
    }

    public Boolean getTimerForMissedCallExists(){
        return this.timerForMissedCallExists;
    }



    public void setOnCall(Boolean b){
        this.onCall = b;
    }

    public Boolean getOnCall(){
        return this.onCall;
    }

    public void setTotalCommands(int i){
        this.totalCommands = i;
    }

    public void createNewContact(String firstName, String lastName, String phoneNumber,
                                 String flashPattern, String flashRate, String color,
                                 Boolean useNotification, Boolean useDefaultValues){

        ACEContact contact = new ACEContact(firstName, lastName, phoneNumber, flashPattern, flashRate, color, useNotification, useDefaultValues);
        Contacts.add(contact);
    }

    public void saveCurrentInformation(String firstName, String lastName){
        oldContactFirstName = firstName;
        oldContactLastName = lastName;
        for (ACEContact contact : getContactList()){
            if (contact.getFirstName().equals(firstName) && contact.getLastName().equals(lastName)){
                oldContactPhoneNumber = contact.getPhoneNumber();
                oldContactFlashPattern = contact.getFlashPattern();
                oldContactFlashRate = contact.getFlashRate();
                oldContactColor = contact.getColor();
                oldContactUseNotification = contact.getUseNotification();
                oldContactUseDefaultValues = contact.getUseDefaultValues();
            }
        }
    }

    public void editContact(String firstName, String lastName, String phoneNumber,
                            String flashPattern, String flashRate, String color,
                            Boolean useNotification, Boolean useDefaultValues) {
        for (ACEContact contact : getContactList()) {
            if (contact.getFirstName().equals(getOldContactFirstName()) && contact.getLastName().equals(getOldContactLastName())) {
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
                contact.setPhoneNumber(phoneNumber);
                contact.setFlashPattern(flashPattern);
                contact.setFlashRate(flashRate);
                contact.setColor(color);
                contact.setUseNotification(useNotification);
                contact.setUseDefaultValues(useDefaultValues);
            }
        }
    }


    public void updateContacts(){

        for (ACEContact contact : getContactList()){
            if (contact.getUseDefaultValues()){
                contact.setFlashPattern(getDefaultFlashPattern());
                contact.setFlashRate(getDefaultFlashRate());
                contact.setColor(getDefaultColor());
            }
        }
    }

    public void simulateAMissedCall(Double[] color) {
        totalCommands = 0;
        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.getInstance();
        totalDuration = 0;
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        final List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        if (!getDefaultDuration().equals("Always On (energy saving)")){
            for (String lightName : getDefaultLights()) {
                innerLoop:
                for (final PHLight light : allLights) {
                    if (lightName.equals(light.getName())) {
                        PHLightState newState = new PHLightState();
                        newState.setOn(true);
                        bridge.updateLightState(light, newState);
                        incrementAndCheck10();
                        newState.setBrightness(255);
                        bridge.updateLightState(light, newState);
                        incrementAndCheck10();
                        if (light.supportsColor()){
                            newState.setX(Float.valueOf(String.valueOf(color[0])));
                            bridge.updateLightState(light, newState);
                            newState.setY(Float.valueOf(String.valueOf(color[1])));
                            bridge.updateLightState(light, newState);
                        }
                        break innerLoop;
                    }
                }
            }
            if (!getDefaultDuration().equals("Always On (full brightness)")) {
                final int MAX_DURATION = Integer.parseInt(getDefaultDuration()) * 60;
                if (!getTimerForMissedCallExists()) {
                    (new Thread() {
                        public void run() {
                            Timer timer = new Timer();
                            setTimerForMissedCallExists(true);
                            System.out.println("timer has been created for missed calls");
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    while (newMissedCall) {
                                        System.out.println("ticking");
                                        System.out.println(totalDuration);
                                        System.out.println(MAX_DURATION);
                                        if (totalDuration >= MAX_DURATION) { //10 rings in total
                                            restoreAllLightStates();
                                            setNewMissedCall(false);
                                            setTimerForMissedCallExists(false);
                                            cancel();
                                        } else {
                                            //wait for a second before repeating
                                            sleepLength(1000);
                                            totalDuration++;
                                        }
                                    }
                                    System.out.println("timer has been cancelled for missed calls");
                                    cancel();
                                }
                            }, 0, 1000);
                        }
                    }).start();
                }
            }
        }
        else{
            for (String lightName : getDefaultLights()) {
                innerLoop:
                for (final PHLight light : allLights) {
                    if (lightName.equals(light.getName())) {
                        PHLightState newState = new PHLightState();
                        newState.setOn(true);
                        bridge.updateLightState(light, newState);
                        incrementAndCheck10();
                        newState.setBrightness(25);
                        bridge.updateLightState(light, newState);
                        incrementAndCheck10();
                        if (light.supportsColor()){
                            newState.setX(Float.valueOf(String.valueOf(color[0])));
                            bridge.updateLightState(light, newState);
                            newState.setY(Float.valueOf(String.valueOf(color[1])));
                            bridge.updateLightState(light, newState);
                        }
                        break innerLoop;
                    }
                }
            }
        }
    }

    public void saveAllLightStates(){
        System.out.println("saving all light states");
        LightStates = new ArrayList<List<String>>();
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if(bridge!= null){
            List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights){
            brightness = String.valueOf(light.getLastKnownLightState().getBrightness());
            isOn = String.valueOf(light.getLastKnownLightState().isOn());
            if (light.supportsColor()){
                colorX = String.valueOf(light.getLastKnownLightState().getX());
                colorY = String.valueOf(light.getLastKnownLightState().getY());
            }
            else{
                color = "null";
            }
            System.out.println(brightness);
            System.out.println(isOn);
            System.out.println(colorX);
            System.out.println(colorY);
            LightState = new ArrayList<String>();
            LightState.add(light.getIdentifier());
            LightState.add(brightness);
            LightState.add(colorX);
            LightState.add(colorY);
            LightState.add(isOn);
            LightStates.add(LightState);
        }
    }
    }

    public void restoreAllLightStates(){
        totalCommands = 0;
        System.out.println("restoring all light states");
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if(bridge!= null){
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (List<String> lightState : LightStates) {
            innerLoop:
            for (PHLight light : allLights) {
                if (light.getIdentifier().equals(lightState.get(0))) {
                    PHLightState state = new PHLightState();
                    state.setBrightness(Integer.parseInt(lightState.get(1)));
                    bridge.updateLightState(light, state);
                    incrementAndCheck10();
                    if (light.supportsColor()) {
                        state.setX(Float.valueOf(lightState.get(2)));
                        bridge.updateLightState(light, state);
                        incrementAndCheck10();

                        state.setY(Float.valueOf(lightState.get(3)));
                        bridge.updateLightState(light, state);
                        incrementAndCheck10();
                    }
                    state.setOn(Boolean.parseBoolean(lightState.get(4)));
                    bridge.updateLightState(light, state);
                    incrementAndCheck10();
                    break innerLoop;
                }
            }
        }
        }
    }

    public void turnOnOnCallLights(){
        totalCommands = 0;
        System.out.println("turning on on call lights");

        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights){
            System.out.println(light.getName());
            PHLightState state = new PHLightState();
            state.setOn(true);
            bridge.updateLightState(light, state);
            incrementAndCheck10();
            state.setBrightness(255);
            bridge.updateLightState(light, state);
            incrementAndCheck10();
            if (light.supportsColor()){
                state.setHue(0);
                bridge.updateLightState(light, state);
                incrementAndCheck10();
            }
        }
    }

    public void startFlashing(final PHLight light){
        saveAllLightStates();
        //System.out.println(light.getName() + " should be flashing");
        isFlashing = true;
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        if(bridge !=null){
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (final PHLight currentLight : allLights){
            if (light == null|| ( light != null &&light.getName().equals(currentLight.getName()))){
                (new Thread() {
                    public void run() {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                while(isFlashing) {
                                    PHLightState state = new PHLightState();
                                    state.setOn(getToggle());
                                    bridge.updateLightState(currentLight, state);
                                    state.setBrightness(255);
                                    bridge.updateLightState(currentLight, state);
                                    System.out.println(currentLight.getName() + " is " + getToggle());
                                    setToggle();
                                    //wait for half of a second before repeating
                                    sleepLength(500);
                                }
                                cancel();
                            }
                        }, 0, 1000);
                    }
                }).start();
            }
        }
        }

    }
    public void stopFlashing(){
        System.out.println("no flashing");
        isFlashing = false;
        sleepLength(50);
        restoreAllLightStates();
    }

    public void sleepLength(int sleep){
        //wait for half of a second before repeating
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void incrementAndCheck10(){
        totalCommands++;
        //System.out.println("total commands: " + totalCommands);
        if (totalCommands >= 10){
            //cannot exceed 10 commands per second
            //wait for a bit
            //System.out.println(totalCommands + " >= 10");
            sleepLength(1500);
            totalCommands = 0;
        }
    }
}
