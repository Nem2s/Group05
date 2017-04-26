package it.polito.group05.group05;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mvc.imagepicker.ImagePicker;
import com.pkmmte.view.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.DB_Manager;
import it.polito.group05.group05.Utility.EventClasses.CurrentUserChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.GroupAddedEvent;
import it.polito.group05.group05.Utility.EventClasses.ObjectChangedEvent;
import it.polito.group05.group05.Utility.GroupAdapter;

import it.polito.group05.group05.Utility.InvitedAdapter;
public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static  Context HomeScreenContext;

    public  static int REQUEST_FROM_NEW_GROUP;
    static public  GroupAdapter groupAdapter;
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
    static public User currentUser;
    CircularImageView cv_user_drawer;
    NavigationView navigationView;
    LinearLayout ll_header;
    Activity activity;
    RecyclerView rv_groups;

    RelativeLayout no_groups;
    public static Context context;
    private List<Group> groups = new ArrayList<>();
    //MAGARI SI POSSONO INSERIRE NEL SINGLETON

    Group newgroup = new Group();
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
    public void onObjectAdded(ObjectChangedEvent event) {
        Log.d("Details", "New member in the group!");
        Group g = (Group)event.retriveObject();
        Singleton.getInstance().addGroup(g);
        groups.add(g);
        groupAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onGroupAdded(GroupAddedEvent e) {
        groupAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onCurrentUserChanged(CurrentUserChangedEvent e) {
        currentUser = (User)e.getUser();
        Singleton.getInstance().setCurrentUser(currentUser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");

        if(bitmap != null && REQUEST_FROM_NEW_USER == requestCode) {
            cv_user_drawer.setImageBitmap(bitmap);
            currentUser.setProfile_image(bitmap);
            DB_Manager.getInstance().photoMemoryUpload(1, currentUser.getId(), bitmap);

            drawer.closeDrawers();
            REQUEST_FROM_NEW_USER = -1;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        HomeScreenContext = this;

        //--------------------------------------------------------------------

       // final DB_Manager db1 = new DB_Manager();
        DB_Manager.getInstance();
    //---------------------------------------------------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final List<Group> items = Singleton.getInstance().getmCurrentGroups();




        context = this;
        no_groups = (RelativeLayout)findViewById(R.id.no_groups_layout);
        activity = this;
        rv_groups = (RecyclerView)findViewById(R.id.groups_rv);
        mAuth = FirebaseAuth.getInstance();
        LinearLayoutManager groupsManager = new LinearLayoutManager(this);
        groupAdapter = new GroupAdapter(items, this);
        rv_groups.setHasFixedSize(true); //La dimensione non cambia nel tempo, maggiori performance.
        rv_groups.setLayoutManager(groupsManager);
        rv_groups.setAdapter(groupAdapter);

        //currentUser = new User("q" + 1, "User", new Balance(3, 1), ((BitmapDrawable)getResources().getDrawable(R.drawable.man_1)).getBitmap(), null, true, true);
        currentUser.setContacts(Singleton.getInstance().createRandomListUsers(61, getApplicationContext(), null));
        //Singleton.getInstance().setId(currentUser.getId());
        Singleton.getInstance().setCurrentUser(currentUser);



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setTransitionName(getResources().getString(R.string.transition_fab));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), NewGroup.class);
                startActivity(i);


            }
        });



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        initDrawer();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                cv_user_drawer = (CircularImageView)findViewById(R.id.drawer_header_image);
                cv_user_drawer.setImageBitmap(currentUser.getProfile_image());
                final TextView tv_username = (TextView)findViewById(R.id.drawer_username);
                tv_username.setText(currentUser.getUser_name());
                final TextView tv_email = (TextView)findViewById(R.id.drawer_email);
                tv_email.setText(currentUser.getEmail());
                ll_header = (LinearLayout)findViewById(R.id.drawer_ll_header);
                ColorUtils.PaletteBuilder builder = new ColorUtils.PaletteBuilder();
                builder
                        .load(((BitmapDrawable)cv_user_drawer.getDrawable()).getBitmap())
                        .brighter(ColorUtils.bright.DARK)
                        .method(ColorUtils.type.VIBRANT)
                        .set(ll_header)
                        .set(tv_username)
                        .set(tv_email)
                        .build();
                /*Picasso
                        .with(getApplicationContext())
                        .load(Integer.parseInt(currentUser.getProfile_image()))
                        .transform(new PicassoRoundTransform())
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.ic_visibility_off)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                ColorUtils.PaletteBuilder b = new ColorUtils.PaletteBuilder();
                                b.load(bitmap)
                                        .brighter(ColorUtils.bright.DARK)
                                        .method(ColorUtils.type.MUTED)
                                        .set(ll_header)
                                        .set(tv_username)
                                        .set(tv_email)
                                        .build();
                                cv.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

*/
                cv_user_drawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImagePicker.pickImage(activity, "Select Imageqweqeqwe:");
                        REQUEST_FROM_NEW_USER = ImagePicker.PICK_IMAGE_REQUEST_CODE;
                    }
                });


            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        groupAdapter.notifyDataSetChanged();

    }

    private void init() {
        if(rv_groups.getAdapter().getItemCount() == 0) {
            no_groups.setVisibility(View.VISIBLE);
            AnimUtils.bounce(fab, 3000,getApplicationContext(), true);
        } else {
            no_groups.setVisibility(View.INVISIBLE);
            //AnimUtils.toggleOn(fab, 350, getApplicationContext());
        }
    }

    private List<UserContact> retriveAllPeople() {
        List<UserContact> res = new ArrayList<>();
        for(int i = 0; i < 76; i++) {
            UserContact u = new UserContact("p" + i, "User " + i, new Balance(i++, i), null, null, i==3, i==1);
            res.add(u);
        }
        return res;
    }

    private void initDrawer() {

        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_balance);
        //item.setTitle("Balance: " + currentUser.getCurrentBalance() + " €");
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        rv_groups.getAdapter().notifyDataSetChanged();
        if(fab.getVisibility() == View.INVISIBLE)
            AnimUtils.toggleOn(fab, 150, this);
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
            DB_Manager.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_share) {

        } /*else if (id == R.id.nav_logout) {
            DB_Manager.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        } */else if (id == R.id.nav_share) {

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

    private List<Group> generateRandomGroups(int n) {
        final String Uri = "https://unsplash.it/300/300/?random";
        final Calendar c = Calendar.getInstance();
        List<Group> ret = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            Group g = new Group();
            g.setName("Group " + i);
            Random r = new Random();
            int m = r.nextInt(21 - 2) + 2;
            for(int j = 0; j < m; j++) {

            }

            ret.add(g);
        }

        return ret;
    }


}
