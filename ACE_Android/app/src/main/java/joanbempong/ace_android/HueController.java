package joanbempong.ace_android;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Joan Bempong on 9/25/2015.
 */
public class HueController extends AsyncTask<Void, Void, Boolean> {
    //declaring variables
    public static HueController controller;

    //some are static so that it can be accessed by other classes -- not sure if okay
    public static String DefaultHost = "192.168.0.102"; //ip address of the Hue Bridge
    public int DefaultPort = 80; //port used to connect to the Hue Bridge
    public static String DefaultUsername = "ACEAndroid"; //username for the Hue Bridge

    public static Socket socket;
    private HueConnection hueConnection;
    public static boolean connected = false; //is the socket between Android and Hue Bridge connected?

    public static boolean HUEregistered = false; //is the Hue Bridge registered?
    public static boolean testFlash = false;
    public static boolean testColor = false;

    static String hueCommandOff = "{\"on\": false}";
    static String hueCommandOn = "{\"on\": true, \"bri\": 255}";
    static String postCommand = String.format("{\"username\": \"%s\", \"devicetype\": \"ACE Android Hue\"}", DefaultUsername);
    static String linkError = "[{\"error\":{\"type\":101,\"address\":\"\",\"description\":\"link button not pressed\"}}]";

    //Colors for Philips Hue
    static String hueColorRed = "{\"on\": true, \"bri\": 255, \"xy\": [0.7,0.2986]}";
    static String hueColorOrange = "{\"on\": true, \"bri\": 255, \"xy\": [0.6,0.38]}";
    static String hueColorYellow = "{\"on\": true, \"bri\": 255, \"xy\": [0.5,0.41]}";
    static String hueColorGreen = "{\"on\": true, \"bri\": 255, \"xy\": [0.21,0.7]}";
    static String hueColorBlue = "{\"on\": true, \"bri\": 255, \"xy\": [0.139,0.081]}";
    static String hueColorPurple = "{\"on\": true, \"bri\": 255, \"xy\": [0.2651,0.1291]}";
    static String hueColorPink = "{\"on\": true, \"bri\": 255, \"xy\": [0.5,0.23]}";
    static String hueColorWhite = "{\"on\": true, \"bri\": 255, \"xy\": [0.3227,0.329]}";
    static String hueColorDaylight = "{\"on\": true, \"bri\": 255, \"xy\": [0.4947,0.35]}";
    static String hueColorSoftWhite = "{\"on\": true, \"bri\": 255, \"xy\": [0.3695,0.3584]}";
    static String hueColorWarmWhite = "{\"on\": true, \"bri\": 255, \"xy\": [0.5104,0.3826]}";

    //List of connected lights
    public static List<String[]> Lights = new ArrayList<>();
    public static String[] Light;
    public static String LightState;
    public static String LightType;
    public static String LightName;
    public static String LightModelId;
    public static String LightManufacturerName;
    public static String LightUniqueId;
    public static String LightSWVersion;
    public static String LightPointSymbol;
    public static String Configured;
    public static String Color;
    public static String[] wordsToLookFor = new String[]{"state", "type", "name", "modelid",
            "manufacturername", "uniqueid", "swversion", "pointsymbol"};
    public static Integer[] wordsToLookForIndexes;

    public static String currentLightConfigure = "";

    //Current contact list
    public static List<List<String[]>> Contacts = new ArrayList<>();
    public static List<String[]> Contact;
    public static String[] ContactName;
    public static String[] ContactPhoneNumber;
    public static String[] ContactIncomingCallLight;
    public static String[] ContactIncomingCallFlash;
    public static String[] ContactMissedCallLight;
    public static String[] ContactMissedCallFlash;

    //default values for incoming and missed calls
    public static String defaultIncomingLight, defaultIncomingFlashPattern, defaultIncomingFlashRate;
    public static String defaultMissedLight, defaultMissedFlashPattern, defaultMissedFlashRate;

    //old contact information
    static String oldFirstName, oldLastName, oldPhoneNumber, oldIncomingCallLight, oldIncomingCallFlashPattern,
            oldIncomingCallFlashRate, oldMissedCallLight, oldMissedCallFlashPattern, oldMissedCallFlashRate;

    //constructor
    public HueController(HueConnection hueConnection) {
        this.hueConnection = hueConnection;
    }

    //method required to perform async task -- runs in the background
    //used to connect the phone to the hue bridge
    @Override
    protected Boolean doInBackground(Void... params) {

        try{
            //creates a new socket and connects to the host and post provided
            socket = new Socket(DefaultHost, DefaultPort);
            //successful connection
            if (socket.isConnected()){
                return true;
            }
            //unsuccessful connection
            else{
                return false;
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    //action that should be taken after a result is returned by doInBackground
    @Override
    protected void onPostExecute(Boolean result){
        System.out.println("onPostExecute");
        System.out.println(result);
        connected = result;
        if (hueConnection != null){
            //send the result to the Connected function in the myConnection interface
            hueConnection.Connected(result);
        }
    }

    //registers the Hue Bridge
    public static boolean postHue(){
        URL url;
        String response = "";
        //run this code only if the Hue Bridge has not been registered yet
        if (!HUEregistered) {
            try {
                //creates a new url and client
                url = new URL(String.format("http://%s/api", DefaultHost));
                System.out.println(url);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setDoOutput(true);
                client.setRequestMethod("POST");

                //prints to the bridge to register it
                PrintWriter out = new PrintWriter(client.getOutputStream());
                System.out.println(postCommand);
                out.print(postCommand);
                out.flush();
                out.close();


                //reads the message sent by the Hue Bridge
                Scanner in = new Scanner(client.getInputStream());
                while (in.hasNextLine()) {
                    response += (in.nextLine());
                }
                System.out.println("response =");
                System.out.println(response);

                //the Hue Bridge was not successfully registered
                if (response.equals(linkError)) {
                    System.out.println("link button not pressed");
                    return false;
                }
                client.disconnect();

                //the Hue Bridge has been successfully registered
                HUEregistered = true;

                //populate the Hue Lights
                populateHueLights();

                return true;
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
                System.out.println("in network on main thread exception");
            } catch (IOException e) {
                System.out.println("in ioexception");
                e.printStackTrace();
            }
        }
        return true;
    }

    //receives a list of Lights connected to the Hue
    public static void populateHueLights(){
        URL url;
        String response = "";
        try{
            //creates a new url and client
            url = new URL(String.format("http://%s/api/%s/lights", DefaultHost, DefaultUsername));
            System.out.println(url);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setDoInput(true);
            client.setRequestMethod("GET");

            //reads the message sent by the Hue Bridge
            Scanner in = new Scanner(client.getInputStream());
            while (in.hasNextLine()){
                String print = in.nextLine();
                response += (print);
                System.out.println(print);
            }

            //splits the string into a list of different lights read
            String[] responseSplit = response.split(Pattern.quote("}}"));
            int i = 0;
            while (i != responseSplit.length){
                int j = 0;
                boolean notfound = false;
                wordsToLookForIndexes = new Integer[8];
                while (j != wordsToLookFor.length){
                    if (responseSplit[i].indexOf(wordsToLookFor[j]) != -1){
                        /*System.out.println("Found the word " + wordsToLookFor[j] +
                                " at index number " + responseSplit[i].indexOf(wordsToLookFor[j]));*/
                        wordsToLookForIndexes[j] = responseSplit[i].indexOf(wordsToLookFor[j]);
                    }
                    else{
                        notfound = true;
                    }
                    j++;
                }

                if (!notfound) {
                    LightState = responseSplit[i].substring(wordsToLookForIndexes[0] - 1, wordsToLookForIndexes[1] - 3);

                    LightType = responseSplit[i].substring(wordsToLookForIndexes[1] - 1, wordsToLookForIndexes[2] - 3);

                    LightName = responseSplit[i].substring(wordsToLookForIndexes[2] - 1, wordsToLookForIndexes[3] - 3);

                    LightModelId = responseSplit[i].substring(wordsToLookForIndexes[3] - 1, wordsToLookForIndexes[4] - 3);

                    LightManufacturerName = responseSplit[i].substring(wordsToLookForIndexes[4] - 1, wordsToLookForIndexes[5] - 3);

                    LightUniqueId = responseSplit[i].substring(wordsToLookForIndexes[5] - 1, wordsToLookForIndexes[6] - 3);

                    LightSWVersion = responseSplit[i].substring(wordsToLookForIndexes[6] - 1, wordsToLookForIndexes[7] - 3);

                    LightPointSymbol = responseSplit[i].substring(wordsToLookForIndexes[7] - 1);

                    Configured = "false";

                    Color = "nocolor";

                    //"create" a new light
                    Light = new String[11];
                    Light[0] = String.valueOf(i+1);
                    Light[1] = LightState;
                    Light[2] = LightType;
                    Light[3] = LightName;
                    Light[4] = LightModelId;
                    Light[5] = LightManufacturerName;
                    Light[6] = LightUniqueId;
                    Light[7] = LightSWVersion;
                    Light[8] = LightPointSymbol;
                    Light[9] = Configured;
                    Light[10] = Color;

                    //clean up the strings in the Light array by splitting them
                    //only if the string does not have the character { which means that
                    //the string contains a list -- leave this string alone
                    int lightlength = 1;
                    while (lightlength != Light.length) {
                        System.out.println(Light[lightlength]);
                        if (Light[lightlength].indexOf("{") == -1){
                            String[] Split = Light[lightlength].split(Pattern.quote("\""));
                            Light[lightlength] = Split[Split.length-1];
                        }
                        System.out.println(Light[lightlength]);
                        lightlength++;
                    }

                    //add this light to the list of Lights
                    Lights.add(Light);
                }
                i++;

            }
            System.out.println(response);
            client.disconnect();

        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
            System.out.println("in network on main thread exception");
        }
        catch (IOException e){
            System.out.println("in ioexception");
            e.printStackTrace();
        }
    }

    //turns off the Hue
    public static void putHueOff(int lightNum){
        URL url;
        String response = "";
        try{
            //creates a new url and client
            url = new URL(String.format("http://%s/api/%s/lights/%s/state", DefaultHost, DefaultUsername, lightNum));
            System.out.println(url);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);
            client.setRequestMethod("PUT");

            //writes to the bridge to turn off the LED bulb
            OutputStreamWriter send = new OutputStreamWriter(client.getOutputStream());
            send.write(hueCommandOff);
            send.close();

            //reads the message sent by the Hue Bridge
            Scanner in = new Scanner(client.getInputStream());
            while (in.hasNextLine()){
                response += (in.nextLine());
            }
            System.out.println("in hue controller puthueoff");
            System.out.println(response);
            client.disconnect();
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
            System.out.println("in network on main thread exception");
        }
        catch (IOException e){
            System.out.println("in ioexception");
            e.printStackTrace();
        }
    }

    //turns on the Hue
    public static void putHueDefaultColor(int lightNum){
        URL url;
        String response = "";
        try{
            //creates a new url and client
            url = new URL(String.format("http://%s/api/%s/lights/%s/state", DefaultHost, DefaultUsername, lightNum));
            System.out.println(url);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);
            client.setRequestMethod("PUT");

            //writes to the bridge to turn on the LED bulb
            OutputStreamWriter send = new OutputStreamWriter(client.getOutputStream());
            send.write(hueColorWarmWhite);
            send.close();

            //reads the message sent by the Hue Bridge
            Scanner in = new Scanner(client.getInputStream());
            while (in.hasNextLine()){
                response += (in.nextLine());
            }
            System.out.println("in huecontroller puthuecoloron");
            System.out.println(response);
            client.disconnect();
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
            System.out.println("in network on main thread exception");
        }
        catch (IOException e){
            System.out.println("in ioexception");
            e.printStackTrace();
        }
    }

    //creates a new TestHueFlash object to flash the Hue
    //a new class is created to be able to flash and perform other task -- needs async task
    public static void putHueFlashCheck(int lightNum){
        HueFlash flash = new HueFlash(DefaultHost, DefaultUsername, lightNum);
        //runs FlashingHue doInBackground function
        flash.execute();
    }


    public static void putHueColorCheck(int lightNum){
        URL url;
        String response = "";
        try{
            //creates a new url and client
            url = new URL(String.format("http://%s/api/%s/lights/%s/state", DefaultHost, DefaultUsername, lightNum));
            System.out.println(url);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);
            client.setRequestMethod("PUT");

            //writes to the bridge to turn off the LED bulb
            OutputStreamWriter send = new OutputStreamWriter(client.getOutputStream());
            send.write(hueColorRed);
            System.out.println(hueColorRed);
            send.close();

            //reads the message sent by the Hue Bridge
            Scanner in = new Scanner(client.getInputStream());
            while (in.hasNextLine()){
                response += (in.nextLine());
            }
            System.out.println("in huecontroller puttesthuecolor");
            System.out.println(response);
            client.disconnect();
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
            System.out.println("in network on main thread exception");
        }
        catch (IOException e){
            System.out.println("in ioexception");
            e.printStackTrace();
        }
    }

    //are all of the connected Hue lights configured?
    public static boolean allLightsConfigured(){
        int lightList = 0;
        while (lightList != HueController.Lights.size()) {
            if (HueController.Lights.get(lightList)[9].equals("false")) {
                return false;
            }
            lightList++;
        }
        return true;
    }

    //save default values for incoming calls
    public static void defaultIncomingCall(String light, String flashPattern, String flashRate){
        defaultIncomingLight = light;
        defaultIncomingFlashPattern = flashPattern;
        defaultIncomingFlashRate = flashRate;
    }

    //save default values for missed calls
    public static void defaultMissedCall(String light, String flashPattern, String flashRate){
        defaultMissedLight = light;
        defaultMissedFlashPattern = flashPattern;
        defaultMissedFlashRate = flashRate;
    }

    public static void newContact(String firstName, String lastName, String phoneNumber,
                                  String incomingCallLight,
                                  String incomingCallFlashPattern,
                                  String incomingCallFlashRate,
                                  String missedCallLight,
                                  String missedCallFlashPattern,
                                  String missedCallFlashRate){

        ContactName = new String[2];
        ContactName[0] = firstName;
        ContactName[1] = lastName;

        ContactPhoneNumber = new String[1];
        ContactPhoneNumber[0] = phoneNumber;

        ContactIncomingCallLight = new String[1];
        ContactIncomingCallLight[0] = incomingCallLight;

        ContactIncomingCallFlash = new String[2];
        ContactIncomingCallFlash[0] = incomingCallFlashPattern;
        ContactIncomingCallFlash[1] = incomingCallFlashRate;

        ContactMissedCallLight = new String[1];
        ContactMissedCallLight[0] = missedCallLight;

        ContactMissedCallFlash = new String[2];
        ContactMissedCallFlash[0] = missedCallFlashPattern;
        ContactMissedCallFlash[1] = missedCallFlashRate;

        //create a new contact
        Contact = new ArrayList<>();
        Contact.add(ContactName);
        Contact.add(ContactPhoneNumber);
        Contact.add(ContactIncomingCallLight);
        Contact.add(ContactIncomingCallFlash);
        Contact.add(ContactMissedCallLight);
        Contact.add(ContactMissedCallFlash);

        //add to the list of contacts
        Contacts.add(Contact);

        System.out.println("New Contact : " +
                "\nFirst Name : " + Contact.get(0)[0] +
                "\nLast Name : " + Contact.get(0)[1] +
                "\nPhone Number : " + Contact.get(1)[0] +
                "\nIncoming Call Light : " + Contact.get(2)[0] +
                "\nIncoming Call Flash Pattern : " + Contact.get(3)[0] +
                "\nIncoming Call Flash Rate : " + Contact.get(3)[1] +
                "\nMissed Call Light : " + Contact.get(4)[0] +
                "\nMissed Call Flash Pattern : " + Contact.get(5)[0] +
                "\nMissed Call Flash Rate : " + Contact.get(5)[1]);
    }

    public static void saveCurrentInformation(String firstname, String lastname){
        oldFirstName = firstname;
        oldLastName = lastname;
        for (List<String[]> contact : Contacts) {
            if (contact.get(0)[0].equals(oldFirstName) && contact.get(0)[1].equals(oldLastName)) {
                oldPhoneNumber = contact.get(1)[0];
                oldIncomingCallLight = contact.get(2)[0];
                oldIncomingCallFlashPattern = contact.get(3)[0];
                oldIncomingCallFlashRate = contact.get(3)[1];
                oldMissedCallLight = contact.get(4)[0];
                oldMissedCallFlashPattern = contact.get(5)[0];
                oldMissedCallFlashRate = contact.get(5)[1];
            }
        }
    }

    public static void editContact(String firstName, String lastName, String phoneNumber,
                                   String incomingCallLight,
                                   String incomingCallFlashPattern,
                                   String incomingCallFlashRate,
                                   String missedCallLight,
                                   String missedCallFlashPattern,
                                   String missedCallFlashRate){
        //update the contact
        if (Contacts != null) {
            ContactName = new String[2];
            ContactName[0] = firstName;
            ContactName[1] = lastName;

            ContactPhoneNumber = new String[1];
            ContactPhoneNumber[0] = phoneNumber;

            ContactIncomingCallLight = new String[1];
            ContactIncomingCallLight[0] = incomingCallLight;

            ContactIncomingCallFlash = new String[2];
            ContactIncomingCallFlash[0] = incomingCallFlashPattern;
            ContactIncomingCallFlash[1] = incomingCallFlashRate;

            ContactMissedCallLight = new String[1];
            ContactMissedCallLight[0] = missedCallLight;

            ContactMissedCallFlash = new String[2];
            ContactMissedCallFlash[0] = missedCallFlashPattern;
            ContactMissedCallFlash[1] = missedCallFlashRate;


            for (List<String[]> contact : Contacts) {
                if (contact.get(0)[0].equals(oldFirstName) && contact.get(0)[1].equals(oldLastName)) {
                    contact.set(0, ContactName);
                    contact.set(1, ContactPhoneNumber);
                    contact.set(2, ContactIncomingCallLight);
                    contact.set(3, ContactIncomingCallFlash);
                    contact.set(4, ContactMissedCallLight);
                    contact.set(5, ContactMissedCallFlash);

                    System.out.println("Edit Contact : " +
                            "\nFirst Name : " + contact.get(0)[0] +
                            "\nLast Name : " + contact.get(0)[1] +
                            "\nPhone Number : " + contact.get(1)[0] +
                            "\nIncoming Call Light : " + contact.get(2)[0] +
                            "\nIncoming Call Flash Pattern : " + contact.get(3)[0] +
                            "\nIncoming Call Flash Rate : " + contact.get(3)[1] +
                            "\nMissed Call Light : " + contact.get(4)[0] +
                            "\nMissed Call Flash Pattern : " + contact.get(5)[0] +
                            "\nMissed Call Flash Rate : " + contact.get(5)[1]);
                }
            }
        }
    }
}
