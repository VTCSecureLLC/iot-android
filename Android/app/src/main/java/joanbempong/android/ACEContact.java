package joanbempong.android;

/**
 * Created by Joan Bempong on 10/8/2015.
 */
public class ACEContact {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String flashPattern;
    private String flashRate;
    private String color;
    private Boolean useNotification;
    private Boolean useDefaultValues;

    public ACEContact(String firstName, String lastName, String phoneNumber, String flashPattern,
                      String flashRate, String color, Boolean useNotification, Boolean useDefaultValues){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.flashPattern = flashPattern;
        this.flashRate = flashRate;
        this.color = color;
        this.useNotification = useNotification;
        this.useDefaultValues = useDefaultValues;
    }

    //getter methods
    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getFlashPattern(){
        return this.flashPattern;
    }

    public String getFlashRate(){
        return this.flashRate;
    }

    public String getColor(){
        return this.color;
    }

    public Boolean getUseNotification(){
        return this.useNotification;
    }

    public Boolean getUseDefaultValues(){
        return this.useDefaultValues;
    }

    //setter methods
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setFlashPattern(String flashPattern){
        this.flashPattern = flashPattern;
    }

    public void setFlashRate(String flashRate){
        this.flashRate = flashRate;
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setUseNotification(Boolean useNotification){
        this.useNotification = useNotification;
    }

    public void setUseDefaultValues(Boolean useDefaultValues){
        this.useDefaultValues = useDefaultValues;
    }

    public String toString() {
        return "First Name: " + this.getFirstName() + ", " +
                "Last Name: " + this.getLastName() + ", " +
                "Phone Number: " + this.getPhoneNumber() + ", " +
                "Flash Pattern: " + this.getFlashPattern() + ", " +
                "Flash Rate: " + this.getFlashRate() + ", " +
                "Color: " + this.getColor() + ", " +
                "Notification Turned On?: " + this.getUseNotification() +
                "Use Default Values?: " + this.getUseDefaultValues() + "\n";
    }
}
