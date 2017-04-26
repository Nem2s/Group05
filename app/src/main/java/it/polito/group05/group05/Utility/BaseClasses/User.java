package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Marco on 28/03/2017.
 */

public class User {
    private String id;
    private String user_name;
    private String email;
    private Balance balance;
    private Bitmap profile_image;
    private Group user_group;
    private int user_color;
    private float tot_expenses;
    private boolean isAdministrator;
    private boolean isCardEnabled;
    private boolean isSelected;
    private List<UserContact> contacts;


    private double customValue;

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
        this.contacts = new ArrayList<>();
        isSelected = false;
        this.customValue = 0;
    }

    public User(String id, String user_name, Balance balance, Group user_group, boolean isAdministrator, boolean isCardEnabled) {
        this.id = id;
        this.user_name = user_name;
        this.balance = balance;
        this.user_group = user_group;
        this.isAdministrator = isAdministrator;
        this.isCardEnabled = isCardEnabled;
        this.user_color = generateRandomColor();
        this.contacts = new ArrayList<>();
        isSelected = false;
        this.customValue = 0;
    }

    public User(String id, String user_name )
    {
        this.id = id;
        this.user_name = user_name;
        this.profile_image = profile_image;
    }

    public boolean hasCustomValue() {
        return customValue > 0;
    }
    public double getCustomValue() {
        return customValue;
    }
    public void setCustomValue(double customValue) {
        this.customValue = customValue;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        // This is the base color which will be mixed with the generated one
        return 3;

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

    public List<UserContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserContact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(UserContact user) {
        contacts.add(user);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}



