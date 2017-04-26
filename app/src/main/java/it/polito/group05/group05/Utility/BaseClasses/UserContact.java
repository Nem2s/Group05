package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;

/**
 * Created by Marco on 04/04/2017.
 */

public class UserContact extends User {
    private boolean selected;



    public UserContact(String id, String user_name, Balance balance, Bitmap profile_image, Group user_group, boolean isAdministrator, boolean isCardEnabled) {
        super();
        super.setId(id);
        super.setUser_name(user_name);
        super.setProfile_image(profile_image);
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
