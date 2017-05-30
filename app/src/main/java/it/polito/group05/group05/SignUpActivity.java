package it.polito.group05.group05;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mvc.imagepicker.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.CurrentUser;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Event.CurrentUserReadyEvent;
import it.polito.group05.group05.Utility.Event.NewUserEvent;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

import static com.facebook.FacebookSdk.getApplicationSignature;

public class SignUpActivity extends AppCompatActivity {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 100;
    // [END declare_auth]
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static int IMAGE_PICKER_CODE;

    private static final String TAG = "SignupActivity";
    private Activity activity;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private View user_info;
    private CircleImageView user_img;
    private CurrentUser ud;//= new CurrentUser();

    private EditText et_user_phone;

    @Subscribe
    public void currentUserReady(CurrentUserReadyEvent event) {
        // String gid = (String) getIntent().getStringExtra("groupId");
        // String eid = (String) getIntent().getStringExtra("expenseId");
        // String message = (String) getIntent().getStringExtra("message");
        Intent i = new Intent(this, MainActivity.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            i.putExtras(bundle);

        startActivity(i);

        //Intent intent = new Intent(getBaseContext(), MyService.class);
        // startService(intent);

        finish();
    }

    @Subscribe
    public void newUser(NewUserEvent event)
    {

        dialogSignUp();



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        ud = new CurrentUser();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        Singleton.getInstance().setCurrContext(this);
        String x = getApplicationSignature(getApplicationContext());
        // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        checkAndRequestPermissions();
        activity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            //finish();
            return;
        } else if(requestCode == IMAGE_PICKER_CODE) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            if(user_img != null && bitmap != null) {
                user_img.setImageBitmap(bitmap);
                ud.setImg_profile(bitmap);
            }


        }
    }




    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            mCurrentUser = mAuth.getCurrentUser();
            DB_Manager.getInstance().setContext(this).getCurrentUser();
        }
        else {
            mAuth.signOut();
            finish();
        }

    }


    /*private CurrentUser setCurrentUser() {
        CurrentUser ud = new CurrentUser();
        ud.setAuthKey(mCurrentUser.getUid());
        ud.setName(mCurrentUser.getDisplayName());
        ud.setEmail(mCurrentUser.getEmail());
        ud.setBalance(new Balance(0,0));
//        ud.setiProfile(mCurrentUser.getPhotoUrl().toString());

        return ud;
    }*/


    private  boolean checkAndRequestPermissions() {
        int permissionContacts = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS);
        int permissionCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();


        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSIONS_MULTIPLE_REQUEST);
            return false;
        } else
            startGoogleSignIn();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (!(perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
                    //else any one or both the permissions are not granted
                    {

                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                    } else startGoogleSignIn();
                }
            }
        }

    }

    private void startGoogleSignIn() {
        if (mCurrentUser != null) {
            DB_Manager.getInstance().setContext(this).getCurrentUser();
        } else {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.FirebaseLoginTheme)
                            .setLogo(R.drawable.logowithtext)
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);

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


    public void dialogSignUp(){


        ud.setAuthKey(mCurrentUser.getUid());
        ud.setName(mCurrentUser.getDisplayName());
        ud.setEmail(mCurrentUser.getEmail());
        ud.setBalance(new Balance(0,0));

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .backgroundColor(getResources().getColor(R.color.card_background))
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .contentColor(getResources().getColor(R.color.colorPrimary))
                .titleColor(getResources().getColor(R.color.colorPrimary))
                .title("Personal Informations")
                .customView(R.layout.dialog_view, true)
                .positiveText("Ok")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(user_img != null && et_user_phone.getText().length() > 0) {
                            ud.setTelNumber(et_user_phone.getText().toString());
                            Singleton.getInstance().setCurrentUser(ud);
                            DB_Manager.getInstance().setContext(activity).pushNewUser(ud);
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            dialog.dismiss();
                            finish();
                            return;
                        }
                    }
                });
        final MaterialDialog dialog = builder.build();
        dialog.show();
        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
        user_info = dialog.getCustomView();
        user_img = (CircleImageView)user_info.findViewById(R.id.iv_pick_user_image);
        et_user_phone = (EditText)user_info.findViewById(R.id.et_phone_number);
        et_user_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() >8 && charSequence.length() < 17)
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                else
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(activity, "Select Profile Image:");
                IMAGE_PICKER_CODE = ImagePicker.PICK_IMAGE_REQUEST_CODE;
            }
        });

    }






}



