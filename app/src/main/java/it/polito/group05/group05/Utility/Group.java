package it.polito.group05.group05.Utility;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Marco on 24/03/2017.
 */

public class Group {

    private String name;
    private String balance;
    private ImageView groupProfile;

    public Group(String groupName, String currentBalance, ImageView groupProfile) {
        this.name = groupName;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public ImageView getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(ImageView groupProfile) {
        this.groupProfile = groupProfile;
    }



}
