package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.DB_Manager;
import it.polito.group05.group05.Utility.EventClasses.CurrentUserChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.ObjectChangedEvent;

public class Init extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        Log.d("Details", "Registered for " + this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.d("Details", "Unregistered for " + this);
        super.onDestroy();
    }

    @Subscribe
    public void onCurrentUserChanged(CurrentUserChangedEvent e) {
        HomeScreen.currentUser = (User)e.getUser();
        User U = (User)e.getUser();
        Singleton.getInstance().setCurrentUser(U);
        //currentUser = new User("q" + 1, "User", new Balance(3, 1), ((BitmapDrawable)getResources().getDrawable(R.drawable.man_1)).getBitmap(), null, true, true);
        U.setContacts(Singleton.getInstance().createRandomListUsers(61, getApplicationContext(), null));
        Singleton.getInstance().setId(U.getId());
        Singleton.getInstance().setCurrentUser(HomeScreen.currentUser);
        Receiveinvite();

            // Check for App Invite invitations and launch deep-link activity if possible.
            // Requires that an Activity is registered in AndroidManifest.xml to handle
            // deep-link URLs.


    }


    @Subscribe
    public void onObjectAdded(ObjectChangedEvent event) {
        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
        Group g = (Group)event.retriveObject();
        //DB_Manager.getInstance().CurrentUserGroupMonitor();
        startActivity(new Intent(this, HomeScreen.class));
        //new DownloadFilesTask().execute(null, null, null);
        //Singleton.getInstance().addGroup(g);
    }

    private static Context context;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage(this, null)
                    .build();
        }
        Init.context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
        DB_Manager.getInstance().getDatabase();

/*FARE PARTE DEL LOGOUT SE PASSWORD CAMBIATA*/
        if(user == null)
        {
            EventBus.getDefault().unregister(this);
            startActivity(new Intent(this, LoginActivity.class));
            Singleton.getInstance().clearGroups();
            if(Singleton.getInstance().getCurrentUser()!=null) {
                Singleton.getInstance().setCurrentUser(null);
            }
            finish();
        }
        else {
            //DB_Manager.getInstance().getDatabase();
            //if(DFT != null) DFT.cancel(true);
            //DFT = (DownloadFilesTask) new DownloadFilesTask().execute(null, null, null);
            if(HomeScreen.currentUser==null) {
                //DB_Manager.getInstance().getDatabase();
                DB_Manager.getInstance().getCurrentUserID();
            }
            else {
                Receiveinvite();
                startActivity(new Intent(this, HomeScreen.class));
                EventBus.getDefault().unregister(this);
            }
        }
        //finish();
    }

    public void Receiveinvite() {
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                // Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                if (result.getStatus().isSuccess()) {
                                    // Extract information from the intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String invitationId = AppInviteReferral.getInvitationId(intent);
                                    DB_Manager.getInstance().getGroupInvited(invitationId);

                                    // Because autoLaunchDeepLink = true we don't have to do anything
                                    // here, but we could set that to false and manually choose
                                    // an Activity to launch to handle the deep link here.
                                    // ...
                                }
                            }
                        });
    }
 /*   public class DownloadFilesTask extends AsyncTask<Object, Object, Void> {

        protected void onPreExecute(){
            //DB_Manager.getInstance().currentUserInfo(HomeScreen.currentUser);
        }

        protected void onProgressUpdate(Integer... progress) {
        }


        protected void onPostExecute(Long result) {
        }

        @Override
        protected Void doInBackground(Object... params) {

            DB_Manager.getInstance().CurrentUserGroupMonitor();
           // DB_Manager.getInstance().getDatabase();
            return null;
        }
    }*/

    public static Context getAppContext() {
        return Init.context;
    }


}


