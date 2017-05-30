package it.polito.group05.group05.Utility.BaseClasses;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.firebase.ui.auth.ui.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

/**
 * Created by user on 04/05/2017.
 */

public class Singleton {
    private static final Singleton ourInstance = new Singleton();
    private GroupDatabase mCurrentGroup;
    private CurrentUser currentUser;
    String currentGroupId;
    private String userId;
    private List<UserContact> localContactsList;
    private Map<String, UserContact> regContactsList;
    private Context currContext;
    private Map<String, Double> usersBalance;
    private int THRESHOLD = 10;
    private LinkedHashMap<String, UserContact> localContactsMap;

    private int[] colors;

    public int[] getColors() {
        return colors;
    }



    public void setColors(int[] colors) {
        this.colors = colors;
    }

    private Singleton() {
        regContactsList = new HashMap<>();
    }

    public Context getCurrContext() {
        return currContext;
    }

    public void setCurrContext(Context currContext) {
        this.currContext = currContext;
    }

    public GroupDatabase getmCurrentGroup() {
        return mCurrentGroup;
    }

    public void setmCurrentGroup(GroupDatabase mCurrentGroup) {
        this.mCurrentGroup = mCurrentGroup;
    }

    public UserDatabase getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
        new getRegContactsTask().execute();
    }
    public void addRegContact(UserDatabase u) {
        this.regContactsList.put(u.getId(), new UserContact(u));
    }

    public Map<String, UserContact> getRegContactsList() {
        return regContactsList;
    }

    public void setRegContactsList(Map<String, UserContact> regContactsList) {
        this.regContactsList = regContactsList;
    }

    private Double price_expense;

    public List<UserContact> getLocalContactsList(int from) {
        List<UserContact> res = new ArrayList<>();
        for(int i = from; i < from + THRESHOLD && i< localContactsList.size(); i++) {
            res.add(localContactsList.get(i));
        }
        return res;
    }

    public LinkedHashMap<String, UserContact> getLocalContactsList() {
        return localContactsMap;
    }

    public void setLocalContactsList(LinkedHashMap<String, UserContact> localContactsList) {
        this.localContactsMap = localContactsList;
    }

    public Double getPrice_expense() {
        return price_expense;
    }

    public void setPrice_expense(Double price_expense) {
        this.price_expense = price_expense;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    static public Singleton getInstance() {
        return ourInstance;
    }


    public String getIdCurrentGroup(){
        return currentGroupId;
    }

    public void setIdCurrentGroup(String currentGroupId){
        this.currentGroupId=currentGroupId;
    }

    public UserContact removeRegContact(UserDatabase userContact) {
        return regContactsList.remove(userContact.getId());
    }

    public Map<String, Double> getUsersBalance() {
        if(this.usersBalance == null)
            this.usersBalance = new HashMap<>();
        return usersBalance;
    }

    public void setUsersBalance(Map<String, Double> usersBalance) {
        this.usersBalance = usersBalance;
    }

    class getRegContactsTask extends AsyncTask<Void, Void, Void> {


        Map<String, UserContact> localList = new LinkedHashMap<>();
        private DatabaseReference usernumberRef;
        private FirebaseDatabase database;
        private DatabaseReference userRef;


        @Override
        protected Void doInBackground(Void... voids) {

            /*Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

            String[] projection =
                    new String[]{
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
                    };
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                    " COLLATE LOCALIZED ASC";
            Cursor query = currContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);


            int indexNumber = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int indexName = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexPhoto = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
*/
            Cursor phones = currContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                    null, null);
            if(phones.getCount() > 0) {
                while (phones.moveToNext()) {

                    String Name = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String Number = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (Number.startsWith("+"))
                        Number = Number.substring(3);
                    Number = Number.replace(" ", "");

                    Number = Number.replace("-", "");

                    String image_uri = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                    UserContact user = new UserContact();
                    user.setName(Name);
                    user.setTelNumber(Number);
                    user.setiProfile(image_uri);

                    localList.put(Number, user);
                }
            }else
                ((Activity) currContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(currContext, "No contacts stored", Toast.LENGTH_SHORT).show();
                    }
                });

            return null;

            /* query.moveToFirst();
            if (query.getCount() > 0) {
                do {
                    UserContact user = new UserContact();
                    String number = query.getString(indexNumber);
                    if (number.startsWith("+"))
                        number = number.substring(3);
                    number = number.replace(" ", "");

                    number = number.replace("-", "");
                    if (number.length() > 8 && number.length() <= 10) {

                        String name = query.getString(indexName);
                        user.setName(name);
                        user.setTelNumber(number);
                        if(query.getString(indexPhoto) != null)
                            user.setiProfile(query.getString(indexPhoto));
                        localList.put(number, user);
                    }
                } while (query.moveToNext());

            }*/
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            localContactsMap = new LinkedHashMap<>(localList);
            localContactsList = new ArrayList<>(localContactsMap.values());
            Collections.sort(localContactsList, new Comparator<UserContact>() {
                @Override
                public int compare(UserContact userContact, UserContact t1) {
                    return userContact.getName().compareTo(t1.getName());
                }
            });
            DB_Manager.getInstance().checkContacts();
        }
    }
}