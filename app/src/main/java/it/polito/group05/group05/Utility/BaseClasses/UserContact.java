package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;

/**
 * Created by Marco on 04/04/2017.
 */

public class UserContact extends User {
    private boolean selected;
    private String id;
    private String name;
    private String email;
    private String pnumber;

    public UserContact(String id, String name, String email, String pnumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pnumber = pnumber;
        selected = false;
    }

    public UserContact() {

    }

    public UserContact(String id, String user_name, Balance balance, Bitmap profile_image, Group user_group, boolean isAdministrator, boolean isCardEnabled) {
        super(id, user_name, balance, profile_image, user_group, isAdministrator, isCardEnabled);
        selected = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public boolean isSelected() {
        return selected;
    }



    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
