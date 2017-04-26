package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.DB_Manager;

public class Init extends AppCompatActivity {

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
            //Singleton.getInstance().clearGroups();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            //DB_Manager.getInstance().getDatabase();
            //if(DFT != null) DFT.cancel(true);
            //DFT = (DownloadFilesTask) new DownloadFilesTask().execute(null, null, null);
           new DownloadFilesTask().execute(null, null, null);

            startActivity(new Intent(this, HomeScreen.class));
        }
        //finish();
    }

    public class DownloadFilesTask extends AsyncTask<Object, Object, Void> {

        protected void onPreExecute(){
            DB_Manager.getInstance().currentUserInfo(HomeScreen.currentUser);
        }

        protected void onProgressUpdate(Integer... progress) {
        }


        protected void onPostExecute(Long result) {
        }

        @Override
        protected Void doInBackground(Object... params) {

            DB_Manager.getInstance().getDatabase();
            return null;
        }
    }

    public static Context getAppContext() {
        return Init.context;
    }


}


