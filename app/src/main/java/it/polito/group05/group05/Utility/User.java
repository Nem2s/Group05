package it.polito.group05.group05.Utility;

/**
 * Created by Marco on 28/03/2017.
 */

public class User {
    private String id;
    private String user_name;
    private String balance;
    private String profile_image;
    private Group user_group;
    private boolean isAdministrator;
    private boolean isCardEnabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public Group getUser_group() {
        return user_group;
    }

    public void setUser_group(Group user_group) {
        this.user_group = user_group;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    public boolean isCardEnabled() {
        return isCardEnabled;
    }

    public void setCardEnabled(boolean cardEnabled) {
        isCardEnabled = cardEnabled;
    }
}
