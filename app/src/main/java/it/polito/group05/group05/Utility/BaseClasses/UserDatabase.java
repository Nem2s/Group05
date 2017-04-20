package it.polito.group05.group05.Utility.BaseClasses;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDatabase {

    public String id;
    public String authKey;
    public String telNumber;
    public String email;
    public Map<String, Object> userGroups;

    public UserDatabase(String id, String authKey, String telNumber, String email) {
        this.id = id;
        this.authKey = authKey;
        this.telNumber = telNumber;
        this.email = email;
    }

    public UserDatabase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getUserGroups() {
        return userGroups;
    }

    public List<String> getUserGruopsList() {


        return new ArrayList<>(userGroups.keySet());

    }

    public void setUserGroups(Map<String, Object> userGroups) {
        this.userGroups = userGroups;
    }
}
