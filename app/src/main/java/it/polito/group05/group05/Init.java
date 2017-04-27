package it.polito.group05.group05;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.DB_Manager;
import it.polito.group05.group05.Utility.EventClasses.CurrentUserChangedEvent;
import it.polito.group05.group05.Utility.ImageUtils;

public class Init extends AppCompatActivity {
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
/*FARE PARTE DEL LOGOUT SE PASSWORD CAMBIATA*/

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkAndRequestPermissions()) {
                    if (user == null) {
                        //Singleton.getInstance().clearGroups();
                        startActivity(new Intent(Init.this, LoginActivity.class));
                        finish();
                    } else {
                        //DB_Manager.getInstance().getDatabase();
                        //if(DFT != null) DFT.cancel(true);
                        //DFT = (DownloadFilesTask) new DownloadFilesTask().execute(null, null, null);
                        runTask();


                    }


                }
            }
        }, 1000);

        //finish();
    }

    private void runTask() {
       new setupFirebaseTask().execute();
    }

    public class setupFirebaseTask extends AsyncTask<Object, Object, Void> {

        @Override
        protected void onPreExecute(){
            DB_Manager.setDbContext(getApplicationContext());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(Init.this, HomeScreen.class));
            finish();
        }

        @Override
        protected Void doInBackground(Object... params) {
            DB_Manager.getInstance().getDatabase();
            DB_Manager.retriveFriends(getContacts());
            return null;
        }
    }

    private List<UserContact> getContacts() {
        // Run query
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
        Cursor query = managedQuery(uri, projection, selection, selectionArgs, sortOrder);


        int indexNumber = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int indexName = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexPhoto = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
        query.moveToFirst();
        if(query.getCount() == 0) {
            return null;
        }
        do {
            UserContact user = new UserContact();
            String number = query.getString(indexNumber);
            String name = query.getString(indexName);
            user.setUser_name(name);
            user.setPnumber(number);
            user.setEmail("user@example.com");
            String photoUri = query.getString(indexPhoto);
            if(photoUri != null)
                user.setProfile_image(ImageUtils.getBitmapFromUri(Uri.parse(photoUri), getApplicationContext()));
            else
                user.setProfile_image(ImageUtils.getBitmpapFromDrawable(getApplicationContext().getResources().getDrawable(R.drawable.user_placeholder)));
            result.add(user);

        }while(query.moveToNext());

        return result;
    }

    private  boolean checkAndRequestPermissions() {
        int permissionContacts = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();


        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSIONS_MULTIPLE_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        if (user == null) {
                            //Singleton.getInstance().clearGroups();
                            startActivity(new Intent(Init.this, HomeScreen.class));
                            finish();
                        } else {
                            //DB_Manager.getInstance().getDatabase();
                            //if(DFT != null) DFT.cancel(true);
                            //DFT = (DownloadFilesTask) new DownloadFilesTask().execute(null, null, null);
                           runTask();

                        }
                        //else any one or both the permissions are not granted
                    } else {

                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera,Contacts and Storage permissions needed.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}


