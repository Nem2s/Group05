package it.polito.group05.group05;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mvc.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.FirebaseAdapterExtension;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Holder.GroupHolder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public  static int REQUEST_FROM_NEW_USER;
    DrawerLayout drawer;
    CircleImageView cv_user_drawer;
    Activity activity;
    Context context;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");

        if(bitmap != null && REQUEST_FROM_NEW_USER == requestCode) {
            cv_user_drawer.setImageBitmap(bitmap);
            //currentUser.setProfile_image(bitmap);
            //DB_Manager.getInstance().photoMemoryUpload(1, currentUser.getId(), bitmap);

            drawer.closeDrawers();
            REQUEST_FROM_NEW_USER = -1;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        /**DEBUGG**/
        Singleton.getInstance().setCurrContext(getApplicationContext());
        /*Singleton.getInstance().setCurrentUser(new UserDatabase("-KioOGqwrdiD3fyAvBet", "Marco Di Rosa",
                "BOqsYAFaDgRtAhuGlIRVHyhoh5S2", "3351671175", "marco@fire.it", null));*/
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Singleton.getInstance().getCurrentUser().getTelNumber() == null) {
                    final String[] number = new String[1];
                    new MaterialDialog.Builder(context)
                            .backgroundColor(context.getResources().getColor(R.color.colorPrimaryLight))
                            .positiveColor(context.getResources().getColor(R.color.colorAccent))
                            .contentColor(context.getResources().getColor(R.color.colorPrimary))
                            .titleColor(context.getResources().getColor(R.color.colorPrimary))
                            .title("Insert your phone Number")
                            .content("If you want to connect with your friends just add your mobile phone!")
                            .inputType(InputType.TYPE_CLASS_PHONE)
                            .inputRangeRes(10 , 17, R.color.colorPrimary)
                            .input("Phone Number", null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    if(input.length() > 3 && input.length() < 12)
                                        number[0] = input.toString();
                                }
                            })
                            .alwaysCallInputCallback()
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Singleton.getInstance().getCurrentUser().setTelNumber(number[0]);
                                    //Toast.makeText(context, "Number: " + Singleton.getInstance().getCurrentUser().getTelNumber(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, NewGroupActivity.class));
                                }
                            })
                            .show();
                } else
                    startActivity(new Intent(MainActivity.this, NewGroupActivity.class));

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                cv_user_drawer = (CircleImageView)findViewById(R.id.drawer_header_image);
                //cv_user_drawer.setImageBitmap(currentUser.getProfile_image());
                final TextView tv_username = (TextView)findViewById(R.id.drawer_username);
                tv_username.setText(Singleton.getInstance().getCurrentUser().getName());
                final TextView tv_email = (TextView)findViewById(R.id.drawer_email);
                tv_email.setText(Singleton.getInstance().getCurrentUser().getEmail());
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView rv = (RecyclerView) findViewById(R.id.groups_rv);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("userGroups");
        FirebaseRecyclerAdapter x = new FirebaseIndexRecyclerAdapter( GroupDatabase.class,
                                                            R.layout.group_item_sample,
                                                            GroupHolder.class,ref, groupRef){

            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {
                ((GroupHolder)viewHolder).setData(model,context);
            }
        };
        //FirebaseAdapterExtension adapter = new FirebaseAdapterExtension(this, GroupDatabase.class,
                //R.layout.member_item_sample, GroupHolder.class,ref, new ArrayList<Object>());
        rv.setAdapter(x);
       if(rv.getAdapter().getItemCount() == 0)
           findViewById(R.id.no_groups_layout).setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                    finish();
                }
            });


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
