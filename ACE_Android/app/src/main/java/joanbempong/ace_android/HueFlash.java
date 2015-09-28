package joanbempong.ace_android;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joan Bempong on 9/28/2015.
 */
public class HueFlash extends AsyncTask<Void, Void, Void> {
    public String host;
    public String username;
    public int lightNum;
    public long sleep;

    //Constructor
    HueFlash(String DefaultHost, String DefaultUsername, int light, long len){
        host = DefaultHost;
        username = DefaultUsername;
        lightNum = light;
        sleep = len;
    }

    //method required to perform async task -- runs in the background while there is a pop up window active
    //flashes a light
    @Override
    protected Void doInBackground(Void... params) {
        URL url;
        HttpURLConnection client = null;
        String response;
        OutputStreamWriter send;
        Scanner in;
        try {
            while (HueController.testFlash || HueController.popup) {
                response = "";

                //creates a new url and client
                url = new URL(String.format("http://%s/api/%s/lights/%s/state", host, username, lightNum));
                System.out.println(url);
                client = (HttpURLConnection) url.openConnection();
                client.setDoOutput(true);
                client.setRequestMethod("PUT");

                //writes to the bridge to turn on the LED bulb
                send = new OutputStreamWriter(client.getOutputStream());
                send.write(HueController.hueCommandOn);
                send.flush();
                send.close();

                //read the message sent by the Hue Bridge
                in = new Scanner(client.getInputStream());
                while (in.hasNextLine()) {
                    response += (in.nextLine());
                }
                System.out.println(response);

                TimeUnit.MILLISECONDS.sleep(sleep);

                response = "";

                //creates a new url and client
                url = new URL(String.format("http://%s/api/%s/lights/%s/state", host, username, lightNum));
                System.out.println(url);
                client = (HttpURLConnection) url.openConnection();
                client.setDoOutput(true);
                client.setRequestMethod("PUT");

                //writes to the bridge to turn off the LED bulb
                send = new OutputStreamWriter(client.getOutputStream());
                send.write(HueController.hueCommandOff);
                send.flush();
                send.close();

                //read the message sent by the Hue Bridge
                in = new Scanner(client.getInputStream());
                while (in.hasNextLine()) {
                    response += (in.nextLine());
                }
                System.out.println("in testhueflash");
                System.out.println(response);

                TimeUnit.MILLISECONDS.sleep(sleep);
            }
            if (client != null){
                client.disconnect();
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
            System.out.println("in network on main thread exception");
        }
        catch (IOException e){
            System.out.println("in ioexception");
            e.printStackTrace();
        }
        return null;
    }
}
