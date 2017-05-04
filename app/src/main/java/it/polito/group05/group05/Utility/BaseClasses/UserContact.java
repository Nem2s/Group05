package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;

/**
 * Created by Marco on 04/04/2017.
 */

public class UserContact extends UserDatabase {
    private boolean selected;

    public UserContact() {
    }

    public UserContact(UserDatabase udb) {
        super(udb);
        selected=false;


    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
