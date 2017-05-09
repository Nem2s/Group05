package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Marco on 28/03/2017.
 */

public class CurrentUser extends UserDatabase{ //This will be the CurrentUser class
    private List<String> groups;
    private List<UserContact> userContactList;
    private Bitmap img_profile;

    public CurrentUser(UserDatabase u) {

        super(u);
        this.id = u.id;
    }

    public List<UserContact> getUserContactList() {
        return userContactList;
    }
    public void setUserContactList(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }

    public CurrentUser(){}


    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void addGroup(String gId) {
        if(this.groups.contains(gId)) return;
        this.groups.add(gId);
    }

    public String removeGroup(String gId) {
        if(this.groups.contains(gId)) {
            this.groups.remove(gId);
            return gId;
        }
        return null;
    }

    public Bitmap getImg_profile() {
        return img_profile;
    }

    public void setImg_profile(Bitmap img_profile) {
        this.img_profile = img_profile;
    }
    public void settingInfoUser(UserDatabase ud){
        this.id = ud.id;
        this.name = ud.name;
        this.authKey = ud.authKey;
        this.telNumber = ud.telNumber;
        this.email = ud.email;
        this.iProfile = ud.iProfile;
    }
    /*
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

    public List<UserContact> getRegcontacts() {
        return regcontacts;
    }

    public void setRegcontacts(List<UserContact> regcontacts) {
        this.regcontacts = regcontacts;
    }

    public void addRegcontacts(UserContact u) {
        if(this.regcontacts == null ) this.regcontacts = new ArrayList<>();
        if(this.regcontacts.contains(u)) return;
        this.regcontacts.add(u);
    } */
}



