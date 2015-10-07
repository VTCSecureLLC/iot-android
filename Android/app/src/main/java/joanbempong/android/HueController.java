package joanbempong.android;

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
    private List<String> defaultIncomingLight, defaultMissedLight;
    private String defaultIncomingFlashPattern, defaultIncomingFlashRate;
    private String defaultMissedDuration;

    //old default values for incoming and missed calls
    private List<String> oldDefaultIncomingLight, oldDefaultMissedLight;
    private String oldDefaultIncomingFlashPattern, oldDefaultIncomingFlashRate;
    private String oldDefaultMissedDuration;

    private List<String> incomingCallDefaultLights = new ArrayList<>();
    private List<String> missedCallDefaultLights = new ArrayList<>();

    private List<String> incomingCallLights = new ArrayList<>();
    private List<String> missedCallLights = new ArrayList<>();

    //Current contact list
    private List<List<String[]>> Contacts = new ArrayList<>();
    private List<String[]> Contact;
    private String[] ContactName;
    private String[] ContactPhoneNumber;
    private String[] ContactIncomingCallLight;
    private String[] ContactIncomingCallFlash;
    private String[] ContactMissedCallLight;
    private String[] ContactMissedCallDuration;
    private String[] ContactIncomingCallUseDefault;
    private String[] ContactMissedCallUseDefault;
    private String[] ContactNotificationYN;

    //old contact information
    private String[] oldContactName;
    private String[] oldContactPhoneNumber;
    private String[] oldContactIncomingCallFlash;
    private String[] oldContactIncomingCallLight;
    private String[] oldContactMissedCallLight;
    private String[] oldContactMissedCallDuration;
    private String[] oldContactIncomingCallUseDefault;
    private String[] oldContactMissedCallUseDefault;
    private String[] oldContactNotificationYN;


    //current light states
    private List<List<String>> LightStates = new ArrayList<>();
    private List<String> LightState;
    private String brightness;
    private String isOn;
    private String color;

    private boolean toggle = false;
    private int totalRings = 0;
    private int totalDuration = 0;
    private boolean callAnswered = false;
    private boolean defaultValues = false;




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

    public void addToIncomingCallDefaultLights(String name){
        incomingCallDefaultLights.add(name);
        System.out.println(name + " added successfully (incoming -- default)");
    }

    public void addToMissedCallDefaultLights(String name){
        missedCallDefaultLights.add(name);
        System.out.println(name + " added successfully (missed -- default)");
    }

    public void removeFromIncomingCallDefaultLights(String name){
        for (Iterator<String> iter = incomingCallDefaultLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + " removed successfully (incoming)");
            }
        }
    }

    public void removeFromMissedCallDefaultLights(String name){
        for (Iterator<String> iter = missedCallDefaultLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + " removed successfully (missed)");
            }
        }
    }

    public void addToIncomingCallLightsList(String name){
        incomingCallLights.add(name);
        System.out.println(name + " added successfully (incoming)");
    }

    public void addToMissedCallLightsList(String name){
        missedCallLights.add(name);
        System.out.println(name + " added successfully (missed)");
    }

    public void removeFromIncomingCallLightsList(String name){
        for (Iterator<String> iter = incomingCallLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + "removed successfully");
            }
        }
    }

    public void removeFromMissedCallLightsList(String name){
        for (Iterator<String> iter = missedCallLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + "removed successfully");
            }
        }
    }

    public void setCleanIncomingCallLights(){
        incomingCallLights = new ArrayList<>();
    }

    public void  setCleanMissedCallLights(){
        missedCallLights = new ArrayList<>();
    }

    public boolean getToggle(){
        return this.toggle;
    }

    public void setToggle(){
        this.toggle = !this.toggle;
    }

    public List<String> getIncomingCallLights(){
        return this.incomingCallLights;
    }

    public String[] getOldContactName(){
        return this.oldContactName;
    }


    public String[] getOldContactPhoneNumber(){
        return this.oldContactPhoneNumber;
    }

    public String[] getOldContactIncomingCallLight(){
        return this.oldContactIncomingCallLight;
    }

    public String[] getOldContactIncomingCallFlash(){
        return this.oldContactIncomingCallFlash;
    }

    public String[] getOldContactMissedCallLight(){
        return this.oldContactMissedCallLight;
    }

    public String[] getOldContactMissedDuration(){
        return this.oldContactMissedCallDuration;
    }

    public String[] getOldContactIncomingCallUseDefault(){
        return this.oldContactIncomingCallUseDefault;
    }

    public String[] getOldContactMissedCallUseDefault(){
        return this.oldContactMissedCallUseDefault;
    }

    public String[] getOldContactNotificationYN(){
        return this.oldContactNotificationYN;
    }


    public List<String> getMissedCallLights(){
        return this.missedCallLights;
    }

    public List<String> getIncomingCallDefaultLights(){
        return this.incomingCallDefaultLights;
    }

    public List<String> getMissedCallDefaultLights(){
        return this.missedCallDefaultLights;
    }

    public List<String> getDefaultIncomingLight(){
        return this.defaultIncomingLight;
    }

    public List<String> getDefaultMissedLight(){
        return this.defaultMissedLight;
    }

    public String getDefaultIncomingFlashPattern(){
        return this.defaultIncomingFlashPattern;
    }

    public String getDefaultIncomingFlashRate(){
        return this.defaultIncomingFlashRate;
    }

    public String getDefaultMissedDuration(){
        return this.defaultMissedDuration;
    }

    public List<String> getOldDefaultIncomingLight(){
        return this.oldDefaultIncomingLight;
    }

    public List<String> getOldDefaultMissedLight(){
        return this.oldDefaultMissedLight;
    }

    public List<List<String[]>> getContactList(){
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
    public void saveDefaultValues(String flashPattern, String flashRate, String duration){
        defaultIncomingLight = this.incomingCallDefaultLights;
        defaultIncomingFlashPattern = flashPattern;
        defaultIncomingFlashRate = flashRate;
        defaultMissedLight = this.missedCallDefaultLights;
        defaultMissedDuration = duration;
    }

    public void setCleanDefaultValues(){
        incomingCallDefaultLights = new ArrayList<>();
        missedCallDefaultLights = new ArrayList<>();
    }

   //save current default values
    public void saveCurrentDefaultValues(){
        oldDefaultIncomingLight = defaultIncomingLight;
        oldDefaultIncomingFlashPattern = defaultIncomingFlashPattern;
        oldDefaultIncomingFlashRate = defaultIncomingFlashRate;
        oldDefaultMissedLight = defaultMissedLight;
        oldDefaultMissedDuration = defaultMissedDuration;
    }

    public void createNewContact(String firstName, String lastName, String phoneNumber,
                                 List<String> incomingCallLight,
                                 String incomingCallFlashPattern,
                                 String incomingCallFlashRate,
                                 List<String> missedCallLight,
                                 String missedCallDuration,
                                 String incomingCallUseDefault,
                                 String missedCallUseDefault,
                                 String notificationYN){

        ContactName = new String[2];
        ContactName[0] = firstName;
        ContactName[1] = lastName;

        ContactPhoneNumber = new String[1];
        ContactPhoneNumber[0] = phoneNumber;

        int i = 0;
        int lightCount = 0;
        for (String lightName : incomingCallLight){
            lightCount++;
            i++;
        }

        ContactIncomingCallLight = new String[lightCount];
        i = 0;
        for (String lightName : incomingCallLight){
            ContactIncomingCallLight[i] = lightName;
            i++;
        }
        /*ContactIncomingCallLight = new String[1];
        ContactIncomingCallLight[0] = incomingCallLight;*/

        ContactIncomingCallFlash = new String[2];
        ContactIncomingCallFlash[0] = incomingCallFlashPattern;
        ContactIncomingCallFlash[1] = incomingCallFlashRate;

        i = 0;
        lightCount = 0;
        for (String lightName : missedCallLight){
            lightCount++;
            i++;
        }

        ContactMissedCallLight = new String[lightCount];
        i = 0;
        for (String lightName : missedCallLight){
            ContactMissedCallLight[i] = lightName;
            i++;
        }
        /*ContactMissedCallLight = new String[1];
        ContactMissedCallLight[0] = missedCallLight;*/

        ContactMissedCallDuration = new String[1];
        ContactMissedCallDuration[0] = missedCallDuration;

        ContactIncomingCallUseDefault = new String[1];
        ContactIncomingCallUseDefault[0] = incomingCallUseDefault;

        ContactMissedCallUseDefault = new String[1];
        ContactMissedCallUseDefault[0] = missedCallUseDefault;

        ContactNotificationYN = new String[1];
        ContactNotificationYN[0] = notificationYN;

        //create a new contact
        Contact = new ArrayList<>();
        Contact.add(ContactName);
        Contact.add(ContactPhoneNumber);
        Contact.add(ContactIncomingCallLight);
        Contact.add(ContactIncomingCallFlash);
        Contact.add(ContactMissedCallLight);
        Contact.add(ContactMissedCallDuration);
        Contact.add(ContactIncomingCallUseDefault);
        Contact.add(ContactMissedCallUseDefault);
        Contact.add(ContactNotificationYN);

        //add to the list of contacts
        Contacts.add(Contact);

        /*System.out.println("New Contact : " +
                "\nFirst Name : " + Contact.get(0)[0] +
                "\nLast Name : " + Contact.get(0)[1] +
                "\nPhone Number : " + Contact.get(1)[0] +
                "\nIncoming Call Light : " +
                "\nIncoming Call Flash Pattern : " + Contact.get(3)[0] +
                "\nIncoming Call Flash Rate : " + Contact.get(3)[1] +
                "\nMissed Call Light : " +
                "\nMissed Call Duration : " + Contact.get(5)[0]+
                "\nUse Incoming Default : " + Contact.get(6)[0]+
                "\nUse Missed Default : " + Contact.get(7)[0] +
                "\nUse Notification : " + Contact.get(8)[0]);*/
    }

    public void saveCurrentInformation(String firstName, String lastName){
        oldContactName = new String[2];

        oldContactName[0] = firstName;
        oldContactName[1] = lastName;
        for (List<String[]> contact : getContactList()){
            if (contact.get(0)[0].equals(firstName) && contact.get(0)[1].equals(lastName)){
                oldContactPhoneNumber = contact.get(1);
                oldContactIncomingCallLight = contact.get(2);
                oldContactIncomingCallFlash = contact.get(3);
                oldContactMissedCallLight = contact.get(4);
                oldContactMissedCallDuration = contact.get(5);
                oldContactIncomingCallUseDefault = contact.get(6);
                oldContactMissedCallUseDefault = contact.get(7);
                oldContactNotificationYN = contact.get(8);
            }
        }
    }

    public void editContact(String firstName, String lastName, String phoneNumber,
                            List<String> incomingCallLight,
                            String incomingCallFlashPattern,
                            String incomingCallFlashRate,
                            List<String> missedCallLight,
                            String missedCallDuration,
                            String incomingCallUseDefault,
                            String missedCallUseDefault,
                            String notificationYN){
        //update the contact
        if (Contacts != null) {
            ContactName = new String[2];
            ContactName[0] = firstName;
            ContactName[1] = lastName;

            ContactPhoneNumber = new String[1];
            ContactPhoneNumber[0] = phoneNumber;

            int i = 0;
            int lightCount = 0;
            for (String lightName : incomingCallLight){
                lightCount++;
                i++;
            }

            ContactIncomingCallLight = new String[lightCount];
            i = 0;
            for (String lightName : incomingCallLight){
                ContactIncomingCallLight[i] = lightName;
                i++;
            }

            ContactIncomingCallFlash = new String[2];
            ContactIncomingCallFlash[0] = incomingCallFlashPattern;
            ContactIncomingCallFlash[1] = incomingCallFlashRate;

            i = 0;
            lightCount = 0;
            for (String lightName : missedCallLight){
                lightCount++;
                i++;
            }

            ContactMissedCallLight = new String[lightCount];
            i = 0;
            for (String lightName : missedCallLight){
                ContactMissedCallLight[i] = lightName;
                i++;
            }

            ContactMissedCallDuration = new String[1];
            ContactMissedCallDuration[0] = missedCallDuration;

            ContactIncomingCallUseDefault = new String[1];
            ContactIncomingCallUseDefault[0] = incomingCallUseDefault;

            ContactMissedCallUseDefault = new String[1];
            ContactMissedCallUseDefault[0] = missedCallUseDefault;

            ContactNotificationYN = new String[1];
            ContactNotificationYN[0] = notificationYN;

            for (List<String[]> contact : Contacts) {
                if (contact.get(0)[0].equals(getOldContactName()[0]) && contact.get(0)[1].equals(getOldContactName()[1])) {
                    contact.set(0, ContactName);
                    contact.set(1, ContactPhoneNumber);
                    contact.set(2, ContactIncomingCallLight);
                    contact.set(3, ContactIncomingCallFlash);
                    contact.set(4, ContactMissedCallLight);
                    contact.set(5, ContactMissedCallDuration);
                    contact.set(6, ContactIncomingCallUseDefault);
                    contact.set(7, ContactMissedCallUseDefault);
                    contact.set(8, ContactNotificationYN);

                    /*System.out.println("Edit Contact : " +
                            "\nFirst Name : " + contact.get(0)[0] +
                            "\nLast Name : " + contact.get(0)[1] +
                            "\nPhone Number : " + contact.get(1)[0] +
                            "\nIncoming Call Light : " + contact.get(2)[0] +
                            "\nIncoming Call Flash Pattern : " + contact.get(3)[0] +
                            "\nIncoming Call Flash Rate : " + contact.get(3)[1] +
                            "\nMissed Call Light : " + contact.get(4)[0] +
                            "\nMissed Call Duration : " + contact.get(5)[0]+
                            "\nUse Incoming Default : " + contact.get(6)[0]+
                            "\nUse Missed Default : " + contact.get(7)[0]+
                            "\nUse Notification : " + Contact.get(8)[0]);*/
                }
            }
        }
    }

    public void updateContacts(){
        for (List<String[]> contact : getContactList()){
            if (contact.get(8)[0].equals("yes")) {
                if ((contact.get(6)[0].equals("yes")) && (contact.get(7)[0].equals("no"))) {
                    List<String> listMissed = new ArrayList<>();
                    for (String miss : contact.get(4)) {
                        listMissed.add(miss);
                    }
                    editContact(contact.get(0)[0], contact.get(0)[1], contact.get(1)[0],
                            getDefaultIncomingLight(), getDefaultIncomingFlashPattern(),
                            getDefaultIncomingFlashRate(), listMissed,
                            contact.get(5)[0], "yes", "no", "yes");
                } else if ((contact.get(6)[0].equals("no")) && (contact.get(7)[0].equals("yes"))) {
                    List<String> listIncoming = new ArrayList<>();
                    for (String in : contact.get(2)) {
                        listIncoming.add(in);
                    }
                    editContact(contact.get(0)[0], contact.get(0)[1], contact.get(1)[0],
                            listIncoming, contact.get(3)[0],
                            contact.get(3)[1], getDefaultMissedLight(),
                            getDefaultMissedDuration(), "no", "yes", "yes");
                } else if ((contact.get(6)[0].equals("yes")) && (contact.get(7)[0].equals("yes"))) {
                    List<String> listIncoming = new ArrayList<>();
                    for (String in : contact.get(4)) {
                        listIncoming.add(in);
                    }
                    List<String> listMissed = new ArrayList<>();
                    for (String miss : contact.get(4)) {
                        listMissed.add(miss);
                    }
                    editContact(contact.get(0)[0], contact.get(0)[1], contact.get(1)[0],
                            listIncoming, getDefaultIncomingFlashPattern(),
                            getDefaultIncomingFlashRate(), listMissed,
                            getDefaultMissedDuration(), "yes", "yes", "yes");
                }
            }
        }
    }

    public void simulateAnIncomingCall(CharSequence name) {
        String[] nameSplit = name.toString().split("\\s+");

        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.create();
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        final List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        totalRings = 0;


        if (Contacts.size() != 0) {
            for (final List<String[]> contact : Contacts) {
                if (nameSplit[0].equals(contact.get(0)[0]) && nameSplit[1].equals(contact.get(0)[1])) {
                    (new Thread() {
                        public void run() {
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    for (final PHLight light : allLights) {
                                        for (String lightName : contact.get(2)) {
                                            if (lightName.equals(light.getName())) {

                                                System.out.println("ticking");
                                                System.out.println(totalRings);
                                                if (totalRings == 20) { //10 rings in total
                                                    System.out.println("timer cancelled");
                                                    cancel();
                                                    if (!getCallAnswered()) {
                                                        simulateAMissedCall(contact.get(4), contact.get(5)[0]);
                                                        break;
                                                    }
                                                    break;
                                                }
                                                if (!getCallAnswered()) {
                                                    PHLightState state = new PHLightState();
                                                    state.setOn(toggle);
                                                    state.setBrightness(255);
                                                    bridge.updateLightState(light, state);
                                                    System.out.println(light.getName() + " is " + toggle);

                                                } else {
                                                    PHLightState state = new PHLightState();
                                                    state.setOn(false);
                                                    bridge.updateLightState(light, state);
                                                    System.out.println("call answered -- timer cancelled");
                                                    cancel();
                                                }
                                            }
                                        }
                                    }
                                    toggle = !toggle;
                                    totalRings++;
                                }
                            }, 0, 1000);
                        }
                    }).start();
                }
            }
        }

    }

    public void simulateAMissedCall(final String[] missedCallList, final String duration) {
        final int MAX_DURATION = Integer.parseInt(duration) * 60;
        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.create();
        totalDuration = 0;
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        final List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (final PHLight light : allLights) {
            PHLightState state = new PHLightState();
            state.setOn(false);
            bridge.updateLightState(light, state);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (final PHLight light : allLights) {
            for (String lightName : missedCallList) {
                if (lightName.equals(light.getName())) {
                    PHLightState newState = new PHLightState();
                    newState.setOn(true);
                    bridge.updateLightState(light, newState);
                    newState.setBrightness(255);
                    bridge.updateLightState(light, newState);
                    System.out.println(light.getName() + " is on, missed");
                }
            }
        }

        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("ticking");
                        System.out.println(totalDuration);
                        System.out.println(MAX_DURATION);
                        if (totalDuration == MAX_DURATION) { //10 rings in total
                            restoreAllLightStates();
                            cancel();
                        } else {
                            totalDuration++;
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }

    public void saveAllLightStates(){
        System.out.println("saving all light states");
        LightStates = new ArrayList<>();
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights){
            brightness = String.valueOf(light.getLastKnownLightState().getBrightness());
            isOn = String.valueOf(light.getLastKnownLightState().isOn());
            if (light.supportsColor()){
                color = String.valueOf(light.getLastKnownLightState().getHue());
            }
            else{
                color = "null";
            }
            System.out.println(brightness);
            System.out.println(isOn);
            System.out.println(color);
            LightState = new ArrayList<>();
            LightState.add(light.getIdentifier());
            LightState.add(brightness);
            LightState.add(isOn);
            LightState.add(color);
            LightStates.add(LightState);
        }
    }

    public void restoreAllLightStates(){
        System.out.println("restoring all light states");
        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights){
            for (List<String> lightState : LightStates){
                if (light.getIdentifier().equals(lightState.get(0))){
                    PHLightState state = new PHLightState();
                    state.setBrightness(Integer.parseInt(lightState.get(1)));
                    System.out.println(Integer.parseInt(lightState.get(1)));
                    state.setOn(Boolean.parseBoolean(lightState.get(2)));
                    System.out.println(Boolean.parseBoolean(lightState.get(2)));
                    if (!lightState.get(3).equals("null")) {
                        state.setHue(Integer.parseInt(lightState.get(3)));
                        System.out.println(Integer.parseInt(lightState.get(3)));
                    }
                    bridge.updateLightState(light, state);
                }
            }
        }
    }

    public void turnOnOnCallLights(){
        System.out.println("turning on on call lights");

        PHHueSDK phHueSDK = PHHueSDK.getInstance();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights){
            System.out.println(light.getName());
            PHLightState state = new PHLightState();
            state.setOn(true);
            bridge.updateLightState(light, state);
            state.setBrightness(255);
            bridge.updateLightState(light, state);
            if (light.supportsColor()){
                state.setHue(0);
                bridge.updateLightState(light, state);
            }
        }
    }

}
