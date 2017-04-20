package it.polito.group05.group05;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.polito.group05.group05.Utility.DB_Manager;

/**
 * Created by andre on 11-Apr-17.
 */

public class Init extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
/*FARE PARTE DEL LOGOUT SE PASSWORD CAMBIATA*/
        if(user == null)
        {
            startActivity(new Intent(this, GeneralAuthentication.class));
            finish();
        }
        else {

            //DB_Manager.getInstance().getDatabase();
            new DownloadFilesTask().execute(null, null, null);

            startActivity(new Intent(this, HomeScreen.class));
        }
        //finish();
    }

    private class DownloadFilesTask extends AsyncTask<Object, Object, Void> {


        protected void onProgressUpdate(Integer... progress) {
        }


        protected void onPostExecute(Long result) {
            startActivity(new Intent(getBaseContext(),HomeScreen.class));
        }

        @Override
        protected Void doInBackground(Object... params) {
            DB_Manager.getInstance().getDatabase();
            return null;
        }
    }

}


