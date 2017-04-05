package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by Marco on 28/03/2017.
 */

public class User {
    private String id;
    private String user_name;
    private Balance balance;
    private Bitmap profile_image;
    private Group user_group;
    private int user_color;
    private float tot_expenses;
    private boolean isAdministrator;
    private boolean isCardEnabled;
public User(){}
    public User(String id, String user_name, Balance balance, Bitmap profile_image, Group user_group, boolean isAdministrator, boolean isCardEnabled) {
        this.id = id;
        this.user_name = user_name;
        this.balance = balance;
        this.profile_image = profile_image;
        this.user_group = user_group;
        this.isAdministrator = isAdministrator;
        this.isCardEnabled = isCardEnabled;
        this.user_color = generateRandomColor();
    }

    public User() {

    }

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

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Bitmap getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(Bitmap profile_image) {
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

    public double getCurrentBalance(){
        return balance.getCredit() - balance.getDebit();
    }

    public int generateRandomColor() {
        Random mRandom = new Random();
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);

    }

    public float getTot_expenses() {
        return tot_expenses;
    }

    public void setTot_expenses(float tot_expenses) {
        this.tot_expenses = tot_expenses;
    }

    public int getUser_color() {
        return user_color;
    }
}
