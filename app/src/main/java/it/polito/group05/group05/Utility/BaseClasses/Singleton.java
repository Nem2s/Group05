package it.polito.group05.group05.Utility.BaseClasses;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
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
    private Map<String, UserContact> localContactsList;
    private Map<String, UserContact> regContactsList;
    private Context currContext;

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

    public Map<String, UserContact> getLocalContactsList() {
        return localContactsList;
    }

    public void setLocalContactsList(Map<String, UserContact> localContactsList) {
        this.localContactsList = localContactsList;
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

    class getRegContactsTask extends AsyncTask<Void, Void, Void> {


        Map<String, UserContact> localList = new HashMap<>();
        private DatabaseReference usernumberRef;
        private FirebaseDatabase database;
        private DatabaseReference userRef;


        @Override
        protected Void doInBackground(Void... voids) {

            Map<String, UserContact> result = new HashMap<>();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

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
            query.moveToFirst();
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
                        localList.put(number, user);
                    }
                } while (query.moveToNext());

            } else
                ((Activity) currContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(currContext, "No contacts stored", Toast.LENGTH_SHORT).show();
                    }
                });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            localContactsList = new HashMap<>(localList);
            DB_Manager.getInstance().checkContacts();
        }
    }
}
