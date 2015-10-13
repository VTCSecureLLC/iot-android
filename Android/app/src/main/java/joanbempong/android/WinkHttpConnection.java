package joanbempong.android;

import com.philips.lighting.hue.sdk.utilities.impl.PHLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Joan Bempong on 10/13/2015.
 *
 * Adapted from Philips Hue's PHHueHttpConnection.class
 */
public class WinkHttpConnection {
    private static final String TAG = "WinkHttpConnection";
    static final String GET = "GET";
    static final String DELETE = "DELETE";
    static final String PUT = "PUT";
    static final String POST = "POST";
    private int timeoutConnection = 8000;
    private HttpURLConnection connection;

    public WinkHttpConnection() {
    }

    public void openConnection(String url, String method) throws MalformedURLException, IOException {
        PHLog.d("PHHueHttpConnection", "openConnection with URL:" + url);
        URL uri = new URL(url);
        this.connection = (HttpURLConnection)uri.openConnection();
        this.connection.setRequestMethod(method);
        this.connection.setConnectTimeout(this.timeoutConnection);
        this.connection.setReadTimeout(this.timeoutConnection);
    }

    public int getTimeoutConnection() {
        return this.timeoutConnection;
    }

    public void setTimeoutConnection(int timeoutConnection) {
        this.timeoutConnection = timeoutConnection;
    }

    public String getData(String url) {
        BufferedReader in = null;
        String returnString = null;

        label192: {
            StringBuffer sb;
            try {
                this.openConnection(url, "GET");
                Object e;
                if(this.connection == null) {
                    e = null;
                    return (String)e;
                }

                this.connection.connect();
                e = null;
                if(this.connection.getResponseCode() == 200) {
                    e = this.connection.getContent();
                }

                if(e != null) {
                    in = new BufferedReader(new InputStreamReader((InputStream)e, "UTF8"));
                    sb = new StringBuffer("");
                    String newLine = System.getProperty("line.separator");

                    String line;
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        sb.append(newLine);
                        if(!in.ready()) {
                            break;
                        }
                    }

                    in.close();
                    returnString = sb.toString();
                    break label192;
                }

                sb = null;
            } catch (Exception var18) {
                if(PHLog.isLoggable()) {
                    PHLog.e("PHHueHttpConnection", "GetData Exception: " + var18);
                }
                break label192;
            } finally {
                if(in != null) {
                    try {
                        in.close();
                        this.connection.disconnect();
                    } catch (IOException var17) {
                        if(PHLog.isLoggable()) {
                            PHLog.e("PHHueHttpConnection", "GetData IOException: " + var17);
                        }
                    }
                }

            }

            return String.valueOf(sb);
        }

        PHLog.d("PHHueHttpConnection", "Bridge Response : " + returnString);
        return returnString;
    }

    public String postData(String data, String url) {
        BufferedReader in = null;
        String returnString = null;

        label215: {
            OutputStreamWriter out;
            try {
                this.openConnection(url, "POST");
                OutputStream e;
                if(this.connection == null) {
                    e = null;
                    return String.valueOf(e);
                }

                PHLog.d("PHHueHttpConnection", "DATA : " + data);
                this.connection.setDoOutput(true);
                e = this.connection.getOutputStream();
                if(e != null) {
                    out = new OutputStreamWriter(e, "UTF-8");
                    out.write(data);
                    out.flush();
                    out.close();
                    this.connection.connect();
                    Object content = this.connection.getContent();
                    StringBuffer sb;
                    if(content == null) {
                        sb = null;
                        return String.valueOf(sb);
                    }

                    in = new BufferedReader(new InputStreamReader((InputStream)content, "UTF-8"));
                    sb = new StringBuffer("");
                    String newLine = System.getProperty("line.separator");

                    String line;
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        sb.append(newLine);
                        if(!in.ready()) {
                            break;
                        }
                    }

                    in.close();
                    returnString = sb.toString();
                    break label215;
                }

                out = null;
            } catch (Exception var22) {
                if(PHLog.isLoggable()) {
                    PHLog.e("PHHueHttpConnection", "PostData Exception: " + var22);
                }
                break label215;
            } finally {
                if(in != null) {
                    try {
                        in.close();
                        this.connection.disconnect();
                    } catch (IOException var21) {
                        if(PHLog.isLoggable()) {
                            PHLog.e("PHHueHttpConnection", "PostData IOException: " + var21);
                        }
                    }
                }

            }

            return String.valueOf(out);
        }

        PHLog.d("PHHueHttpConnection", "Bridge Response : " + returnString);
        return returnString;
    }

    public String deleteData(String url) {
        String str = null;
        BufferedReader in = null;

        try {
            this.openConnection(url, "DELETE");
            Object e;
            if(this.connection == null) {
                e = null;
                return (String)e;
            }

            this.connection.connect();
            e = this.connection.getContent();
            if(e == null) {
                String sb1 = str;
                return sb1;
            }

            in = new BufferedReader(new InputStreamReader((InputStream)e, "UTF-8"));
            StringBuffer sb = new StringBuffer("");
            String newLine = System.getProperty("line.separator");

            String line;
            while((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(newLine);
                if(!in.ready()) {
                    break;
                }
            }

            in.close();
            str = sb.toString();
        } catch (Exception var18) {
            if(PHLog.isLoggable()) {
                PHLog.e("PHHueHttpConnection", "DeleteData Exception: " + var18);
            }
        } finally {
            if(in != null) {
                try {
                    in.close();
                    this.connection.disconnect();
                } catch (IOException var17) {
                    if(PHLog.isLoggable()) {
                        PHLog.e("PHHueHttpConnection", "DeleteData IOException: " + var17);
                    }
                }
            }

        }

        PHLog.d("PHHueHttpConnection", "Bridge Response : " + str);
        return str;
    }

    public String putData(String data, String url) {
        BufferedReader in = null;
        String returnString = null;

        label219: {
            StringBuffer sb;
            try {
                this.openConnection(url, "PUT");
                OutputStream e;
                if(this.connection == null) {
                    e = null;
                    return String.valueOf(e);
                }

                PHLog.d("PHHueHttpConnection", "DATA :  " + data);
                this.connection.setDoOutput(true);
                this.connection.connect();
                e = this.connection.getOutputStream();
                OutputStreamWriter out;
                if(e == null) {
                    out = null;
                    return String.valueOf(out);
                }

                out = new OutputStreamWriter(e, "UTF-8");
                out.write(data);
                out.close();
                Object content = this.connection.getContent();
                if(content != null) {
                    in = new BufferedReader(new InputStreamReader((InputStream)content, "UTF-8"));
                    sb = new StringBuffer("");
                    String newLine = System.getProperty("line.separator");

                    String line;
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        sb.append(newLine);
                        if(!in.ready()) {
                            break;
                        }
                    }

                    in.close();
                    returnString = sb.toString();
                    break label219;
                }

                sb = null;
            } catch (Exception var22) {
                if(PHLog.isLoggable()) {
                    PHLog.e("PHHueHttpConnection", "PutData Exception: " + var22);
                }
                break label219;
            } finally {
                if(in != null) {
                    try {
                        in.close();
                        this.connection.disconnect();
                    } catch (IOException var21) {
                        if(PHLog.isLoggable()) {
                            PHLog.e("PHHueHttpConnection", "PutData Exception: " + var21);
                        }
                    }
                }

            }

            return String.valueOf(sb);
        }

        PHLog.d("PHHueHttpConnection", "Bridge Response : " + returnString);
        return returnString;
    }
}
