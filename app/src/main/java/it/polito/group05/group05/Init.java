package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        Singleton.getInstance().setCurrentUser(HomeScreen.currentUser);
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

    private static DownloadFilesTask DFT;
    private static Context context;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Init.context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();

/*FARE PARTE DEL LOGOUT SE PASSWORD CAMBIATA*/
        if(user == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
            Singleton.getInstance().clearGroups();
            if(HomeScreen.currentUser!=null) {
                HomeScreen.currentUser = null;
            }
            finish();
        }
        else {
            //DB_Manager.getInstance().getDatabase();
            //if(DFT != null) DFT.cancel(true);
            //DFT = (DownloadFilesTask) new DownloadFilesTask().execute(null, null, null);
            if(HomeScreen.currentUser==null) {
                DB_Manager.getInstance().getDatabase();
                DB_Manager.getInstance().getCurrentUserID();
            }
            else
            startActivity(new Intent(this, HomeScreen.class));
        }
        //finish();
    }

    public class DownloadFilesTask extends AsyncTask<Object, Object, Void> {

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
    }

    public static Context getAppContext() {
        return Init.context;
    }


}


