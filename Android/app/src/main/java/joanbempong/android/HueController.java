package joanbempong.android;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.util.PHHueCountTimer;
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

    private List<String> incomingCallDefaultLights = new ArrayList<>();
    private List<String> missedCallDefaultLights = new ArrayList<>();

    //nondefault values for incoming and missed calls
    private List<String> IncomingLight, MissedLight;
    private String IncomingFlashPattern, IncomingFlashRate;
    private String MissedDuration;

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

    //old contact information
    private String[] oldContactName;
    private String[] oldContactPhoneNumber;
    private String[] oldContactIncomingCallFlash;
    private String[] oldContactIncomingCallLight;
    private String[] oldContactMissedCallLight;
    private String[] oldContactMissedDuration;


    private PHHueCountTimer timer = null;
    private boolean toggle = false;
    private int totalRings = 0;
    private boolean callAnswered = false;




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
        System.out.println(name + "added successfully");
    }

    public void addToMissedCallDefaultLights(String name){
        missedCallDefaultLights.add(name);
        System.out.println(name + "added successfully");
    }

    public void removeFromIncomingCallDefaultLights(String name){
        for (Iterator<String> iter = incomingCallDefaultLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + "removed successfully");
            }
        }
    }

    public void removeFromMissedCallDefaultLights(String name){
        for (Iterator<String> iter = missedCallDefaultLights.listIterator(); iter.hasNext();){
            String lightName = iter.next();
            if (lightName.equals(name)){
                iter.remove();
                System.out.println(lightName + "removed successfully");
            }
        }
    }

    public void addToIncomingCallLightsList(String name){
        incomingCallLights.add(name);
        System.out.println(name + "added successfully");
    }

    public void addToMissedCallLightsList(String name){
        missedCallLights.add(name);
        System.out.println(name + "added successfully");
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
        return this.oldContactMissedDuration;
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

    public List<List<String[]>> getContactList(){
        return this.Contacts;
    }

    public boolean getCallAnswered(){
        return this.callAnswered;
    }

    public void setCallAnswered(boolean b){
        this.callAnswered = b;
    }

    //save default values for incoming calls
    public void saveDefaultIncomingCall(String flashPattern, String flashRate){
        defaultIncomingLight = this.incomingCallDefaultLights;
        defaultIncomingFlashPattern = flashPattern;
        defaultIncomingFlashRate = flashRate;
    }

    //save default values for missed calls
    public void saveDefaultMissedCall(String duration){
        defaultMissedLight = this.missedCallDefaultLights;
        defaultMissedDuration = duration;
    }

    public void createNewContact(String firstName, String lastName, String phoneNumber,
                                 List<String> incomingCallLight,
                                 String incomingCallFlashPattern,
                                 String incomingCallFlashRate,
                                 List<String> missedCallLight,
                                 String missedCallDuration){

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

        //create a new contact
        Contact = new ArrayList<>();
        Contact.add(ContactName);
        Contact.add(ContactPhoneNumber);
        Contact.add(ContactIncomingCallLight);
        Contact.add(ContactIncomingCallFlash);
        Contact.add(ContactMissedCallLight);
        Contact.add(ContactMissedCallDuration);

        //add to the list of contacts
        Contacts.add(Contact);

        System.out.println("New Contact : " +
                "\nFirst Name : " + Contact.get(0)[0] +
                "\nLast Name : " + Contact.get(0)[1] +
                "\nPhone Number : " + Contact.get(1)[0] +
                "\nIncoming Call Light : " +
                "\nIncoming Call Flash Pattern : " + Contact.get(3)[0] +
                "\nIncoming Call Flash Rate : " + Contact.get(3)[1] +
                "\nMissed Call Light : " +
                "\nMissed Call Duration : " + Contact.get(5)[0]);
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
                oldContactMissedDuration = contact.get(5);
            }
        }
    }

    public void editContact(String firstName, String lastName, String phoneNumber,
                            List<String> incomingCallLight,
                            String incomingCallFlashPattern,
                            String incomingCallFlashRate,
                            List<String> missedCallLight,
                            String missedCallDuration){
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


            for (List<String[]> contact : Contacts) {
                if (contact.get(0)[0].equals(getOldContactName()[0]) && contact.get(0)[1].equals(getOldContactName()[1])) {
                    contact.set(0, ContactName);
                    contact.set(1, ContactPhoneNumber);
                    contact.set(2, ContactIncomingCallLight);
                    contact.set(3, ContactIncomingCallFlash);
                    contact.set(4, ContactMissedCallLight);
                    contact.set(5, ContactMissedCallDuration);

                    System.out.println("Edit Contact : " +
                            "\nFirst Name : " + contact.get(0)[0] +
                            "\nLast Name : " + contact.get(0)[1] +
                            "\nPhone Number : " + contact.get(1)[0] +
                            "\nIncoming Call Light : " + contact.get(2)[0] +
                            "\nIncoming Call Flash Pattern : " + contact.get(3)[0] +
                            "\nIncoming Call Flash Rate : " + contact.get(3)[1] +
                            "\nMissed Call Light : " + contact.get(4)[0] +
                            "\nMissed Call Duration : " + contact.get(5)[0]);
                }
            }
        }
    }

    public void simulateAnIncomingCall(CharSequence name) {
        String[] nameSplit = name.toString().split("\\s+");

        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.create();
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        totalRings = 0;


        if (Contacts.size() != 0) {
            for (final List<String[]> contact : Contacts) {
                if (nameSplit[0].equals(contact.get(0)[0]) && nameSplit[1].equals(contact.get(0)[1])) {
                    for (final PHLight light : allLights) {
                        for (String lightName : contact.get(2)) {
                            if (lightName.equals(light.getName())) {
                                (new Thread() {
                                    public void run() {
                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                System.out.println("ticking");
                                                System.out.println(totalRings);
                                                if (totalRings == 10){
                                                    System.out.println("timer cancelled");
                                                    cancel();
                                                    if (!getCallAnswered()){
                                                        simulateAMissedCall(contact.get(4));
                                                    }
                                                }
                                                if (!getCallAnswered()) {
                                                    PHLightState state = new PHLightState();
                                                    state.setOn(toggle);
                                                    toggle = !toggle;
                                                    bridge.updateLightState(light, state);
                                                    System.out.println(light.getName() + " is on");

                                                }
                                                else{
                                                    System.out.println("call answered -- timer cancelled");
                                                    cancel();
                                                }
                                                totalRings++;
                                            }
                                        }, 0, 1000);
                                    }
                                }).start();
                            }
                        }
                    }
                }
            }
        }

    }

    public void simulateAMissedCall(String[] missedCallList){
        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            for (String lightName : missedCallList) {
                if (lightName.equals(light.getName())) {
                    PHLightState state = new PHLightState();
                    state.setOn(true);
                    bridge.updateLightState(light, state);
                    System.out.println(light.getName() + " is on - missed");
                }
            }
        }
    }

}