package joanbempong.android;

/**
 * Created by Joan Bempong on 10/7/2015.
 */
public class SetupController {
    //declaring variables
    private static SetupController instance = null;
    private static boolean setupCompleted = false;

    private SetupController() {
    }

    public static SetupController getInstance() {
        if(instance == null) {
            instance = new SetupController();
        }

        return instance;
    }

    public static SetupController create() {
        return getInstance();
    }

    public boolean getSetupCompleted() {
        return setupCompleted;
    }

    public void setSetupCompleted(boolean b) {
        setupCompleted = b;
    }
}
