package it.polito.group05.group05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.FirebaseAdapterExtension;

import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;

import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Holder.GroupHolder;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static  Context HomeScreenContext;

    public  static int REQUEST_FROM_NEW_GROUP;
    static public FirebaseAdapterExtension groupAdapter;
    public  static int REQUEST_FROM_NEW_USER;

   // private static FirebaseDatabase database1;

    private static final String TAG = "AnonymousAuth";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]


    FloatingActionButton fab;
    DrawerLayout drawer;
    UserDatabase currentUser;
    CircleImageView cv_user_drawer;
    NavigationView navigationView;
    LinearLayout ll_header;
    Activity activity;
    RecyclerView rv_groups;
    RecyclerView rv;

    RelativeLayout no_groups;
    public static Context context;
    private List<GroupDatabase> groups = new ArrayList<>();
    //MAGARI SI POSSONO INSERIRE NEL SINGLETON

    GroupDatabase newgroup = new GroupDatabase();
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

   /* @Subscribe
    public void onObjectAdded(ObjectChangedEvent event) {
        Log.d("Details", "New member in the group!");
        Group g = (Group)event.retriveObject();
        Singleton.getInstance().addGroup(g);
        groups.add(g);
        groupAdapter.notifyDataSetChanged();
    }*/



    /*@Subscribe
    public void onCurrentUserChanged(CurrentUserChangedEvent e) {
        currentUser = (User)e.getUser();
        Singleton.getInstance().setCurrentUser(currentUser);
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_screen);
        rv = (RecyclerView) findViewById(R.id.groups_rv);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        FirebaseAdapterExtension adapter = new FirebaseAdapterExtension(this, GroupDatabase.class,
        R.layout.member_item_sample, GroupHolder.class,ref, new ArrayList<Object>());
        rv.setAdapter(adapter);

    }





    private void initDrawer() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_balance);
        //item.setTitle("Balance: " + currentUser.getCurrentBalance() + " €");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {


            finish();

        } else if (id == R.id.nav_share) {

                Intent intent = new AppInviteInvitation.IntentBuilder("ciao")
                        .setMessage("ciao ciao ciao")
                        .build();
                startActivityForResult(intent, 1);
        } else if (id == R.id.nav_contacts) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
