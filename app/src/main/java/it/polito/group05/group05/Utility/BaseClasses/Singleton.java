package it.polito.group05.group05.Utility.BaseClasses;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 04/05/2017.
 */

 public class Singleton {
    private static final Singleton ourInstance = new Singleton();
    private GroupDatabase mCurrentGroup;
    private CurrentUser currentUser;
    String currentGroupId;
    private String userId;
    private List<UserContact> contactList;
    private Map<String, Double> usersBalance;
    private Context currContext;


    private Singleton() {
        this.contactList = new ArrayList<>();
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

    private Double price_expense;



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

    public List<UserContact> getContactList(final Context context) {
        return contactList;
    }


    public void setContactList(List<UserContact> contactList) {
        this.contactList = contactList;
    }

    public void addContact(UserContact user) {
        if(this.contactList != null)
            this.contactList.add(user);
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


        List<UserContact> localList = new ArrayList<>();
        private DatabaseReference usernumberRef;
        private FirebaseDatabase database;
        private DatabaseReference userRef;


        @Override
        protected Void doInBackground(Void... voids) {

            List<UserContact> result = new ArrayList<>();
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
            do {
                UserContact user = new UserContact();
                String number = query.getString(indexNumber);
                if(number.startsWith("+"))
                    number=number.substring(3);
                if (number.length() >= 10) {
                    number = number.replace(" ", "");
                    number = number.replace("-", "");
                    number = number.substring(number.length() - 10);
                    String name = query.getString(indexName);
                    user.setName(name);
                    user.setTelNumber(number);
                    result.add(user);
                }
            } while (query.moveToNext());

            List<UserContact> result1 = new ArrayList<UserContact>();
            Set<String> titles = new HashSet<String>();

            for (UserContact item : result) {
                if (titles.add(item.getTelNumber())) {
                    localList.add(item);
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            database = FirebaseDatabase.getInstance();
            usernumberRef = database.getReference("usersNumber");
            userRef = database.getReference("users");
            for(UserContact UC : localList){

                usernumberRef.child(UC.getTelNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) return;
                        String s = dataSnapshot.getValue(String.class);
                        userRef.child(s).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()) return;
                                UserDatabase ud = dataSnapshot.getValue(UserDatabase.class);
                                UserContact U = new UserContact(ud);
                                if(contactList.contains(U)) return;
                                contactList.add(U);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
    }
}
