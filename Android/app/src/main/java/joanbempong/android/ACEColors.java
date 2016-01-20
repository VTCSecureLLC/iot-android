package joanbempong.android;

import org.linphone.R;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joan Bempong on 10/19/2015.
 */
public class ACEColors {
    private static ACEColors instance = null;
    private Map colorsList = new HashMap();

    private ACEColors(){
        //uses gamut B
        //red hues
        Double[] red = {0.674,0.322};
        colorsList.put("red", red);
        Double[] crimson = {0.6417,0.304};
        colorsList.put("crimson", crimson);
        Double[] darkRed = {0.674,0.322};
        colorsList.put("darkRed", darkRed);
        Double[] fireBrick = {0.6566,0.3123};
        colorsList.put("fireBrick", fireBrick);

        //orange hues
        Double[] orange = {0.5562,0.4084};
        colorsList.put("orange", orange);
        Double[] coral = {0.5763,0.3486};
        colorsList.put("coral", coral);
        Double[] darkOrange = {0.5916,0.3824};
        colorsList.put("darkOrange", darkOrange);
        Double[] orangeRed = {0.6733,0.3224};
        colorsList.put("orangeRed", orangeRed);

        //yellow hues
        Double[] yellow = {0.4317,0.4996};
        colorsList.put("yellow", yellow);
        Double[] gold = {0.4859,0.4599};
        colorsList.put("gold", gold);
        Double[] lemonChiffon = {0.3608,0.3756};
        colorsList.put("lemonChiffon", lemonChiffon);
        Double[] lightYellow = {0.3436,0.3612};
        colorsList.put("lightYellow", lightYellow);

        //green hues
        Double[] green = {0.408,0.517};
        colorsList.put("green", green);
        Double[] darkOliveGreen = {0.3908,0.4829};
        colorsList.put("darkOliveGreen", darkOliveGreen);
        Double[] seaGreen = {0.3602,0.4223};
        colorsList.put("seaGreen", seaGreen);
        Double[] lightGreen = {0.3682,0.438};
        colorsList.put("lightGreen", lightGreen);
        Double[] springGreen = {0.3882,0.4777};
        colorsList.put("springGreen", springGreen);

        //blue hues
        Double[] blue = {0.168,0.041};
        colorsList.put("blue", blue);
        Double[] aqua = {0.2858,0.2747};
        colorsList.put("aqua", aqua);
        Double[] deepSkyBlue = {0.2428,0.1893};
        colorsList.put("deepSkyBlue", deepSkyBlue);
        Double[] dodgerBlue = {0.2115,0.1273};
        colorsList.put("dodgerBlue", dodgerBlue);
        Double[] midnightBlue = {0.1825,0.0697};
        colorsList.put("midnightBlue", midnightBlue);
        Double[] royalBlue = {0.2047,0.1138};
        colorsList.put("royalBlue", royalBlue);
        Double[] skyBlue = {0.2807,0.2645};
        colorsList.put("skyBlue", skyBlue);
        Double[] steelBlue = {0.248,0.1997};
        colorsList.put("steelBlue", steelBlue);

        //purple hues
        Double[] purple = {0.2725,0.1096};
        colorsList.put("purple", purple);
        Double[] blueViolet = {0.251,0.1056};
        colorsList.put("blueViolet", blueViolet);
        Double[] darkMagenta = {0.3824,0.1601};
        colorsList.put("darkMagenta", darkMagenta);
        Double[] darkOrchid = {0.2986,0.1341};
        colorsList.put("darkOrchid", darkOrchid);
        Double[] plum = {0.3495,0.2545};
        colorsList.put("plum", plum);
        Double[] rebeccaPurple = {0.2703,0.13981};
        colorsList.put("rebeccaPurple", rebeccaPurple);
        Double[] violet = {0.3644,0.2133};
        colorsList.put("violet", violet);
        Double[] lavender = {0.3085,0.3071};
        colorsList.put("lavender", lavender);

        //pink hues
        Double[] pink = {0.3944,0.3093};
        colorsList.put("pink", pink);
        Double[] deepPink = {0.5386,0.2468};
        colorsList.put("deepPink", deepPink);
        Double[] fuchsia = {0.3824,0.1601};
        colorsList.put("fuchsia", fuchsia);
        Double[] hotPink = {0.4682,0.2452};
        colorsList.put("hotPink", hotPink);
        Double[] lightPink = {0.4112,0.3091};
        colorsList.put("lightPink", lightPink);

        //brown hues
        Double[] brown = {0.6399,0.3041};
        colorsList.put("brown", brown);
        Double[] chocolate = {0.6009,0.3684};
        colorsList.put("chocolate", chocolate);
        Double[] peru = {0.5305,0.3911};
        colorsList.put("peru", peru);
        Double[] saddleBrown = {0.5993,0.369};
        colorsList.put("saddleBrown", saddleBrown);
        Double[] sienna = {0.5714,0.3559};
        colorsList.put("sienna", sienna);

        //black
        Double[] black = {0.168,0.041};
        colorsList.put("black", black);

        //gray hues
        Double[] gray = {0.3227,0.329};
        colorsList.put("gray", gray);
        Double[] slateGray = {0.2944,0.2918};
        colorsList.put("slateGray", slateGray);
        Double[] lightSlateGray = {0.2924,0.2877};
        colorsList.put("lightSlateGray", lightSlateGray);


        //white
        Double[] white = {0.3227,0.329};
        colorsList.put("white", white);

    }

    public static ACEColors getInstance() {
        if(instance == null) {
            instance = new ACEColors();
        }

        return instance;
    }

    public static ACEColors create() {
        return getInstance();
    }

    public Map<String, Double[]> getColorsList(){
        return this.colorsList;
    }
}
