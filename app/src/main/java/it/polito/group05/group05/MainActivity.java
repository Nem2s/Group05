package it.polito.group05.group05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mvc.imagepicker.ImagePicker;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.BaseClasses.CurrentUser;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.Holder.GroupHolder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public  static int REQUEST_FROM_NEW_USER;
    DrawerLayout drawer;
    CircleImageView cv_user_drawer;
    Activity activity;
    Context context;
    ImageView iv_no_groups;
    TextView tv_no_groups;
    FirebaseIndexRecyclerAdapter mAdapter;
    RecyclerView rv;
    ImageView iv_nav_header;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");

        if(bitmap != null && REQUEST_FROM_NEW_USER == requestCode) {
            cv_user_drawer.setImageBitmap(bitmap);
            //currentUser.setProfile_image(bitmap);
            //DB_Manager.getInstance().photoMemoryUpload(1, currentUser.getId(), bitmap);
            String uuid = UUID.randomUUID().toString();
            Singleton.getInstance().getCurrentUser().setiProfile(uuid);
            DB_Manager.getInstance().setContext(context)
                    .imageProfileUpload(1, Singleton.getInstance().getCurrentUser().getId(), uuid,  bitmap);
            FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userInfo").child("iProfile").setValue(uuid);
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
        iv_no_groups = (ImageView)findViewById(R.id.iv_no_groups);
        tv_no_groups = (TextView)findViewById(R.id.tv_no_groups);
        rv = (RecyclerView) findViewById(R.id.groups_rv);
        final ProgressBar pb = (ProgressBar)findViewById(R.id.pb_loading_groups);
        if(((CurrentUser)Singleton.getInstance().getCurrentUser())!=null)
            if(((CurrentUser)Singleton.getInstance().getCurrentUser()).getGroups().size()==0)
                pb.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        activity = this;
        /**DEBUGG**/
        Singleton.getInstance().setCurrContext(getApplicationContext());
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(MainActivity.this, NewGroupActivity.class));
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(final View drawerView, float slideOffset) {
                cv_user_drawer = (CircleImageView)findViewById(R.id.drawer_header_image);
                iv_nav_header = (ImageView) findViewById(R.id.background_nav_header);
                Glide.with(context).using(new FirebaseImageLoader())
                        .load(FirebaseStorage.getInstance().getReference("users")
                                .child(Singleton.getInstance().getCurrentUser().getId())
                                .child(Singleton.getInstance().getCurrentUser().getiProfile()))
                        .placeholder(R.drawable.user_placeholder)
                        .centerCrop()
                        .crossFade()
                        .into(cv_user_drawer);
                Glide.with(context).using(new FirebaseImageLoader())
                        .load(FirebaseStorage.getInstance().getReference("users")
                                .child(Singleton.getInstance().getCurrentUser().getId())
                                .child(Singleton.getInstance().getCurrentUser().getiProfile()))
                        .placeholder(R.drawable.user_placeholder)
                        .centerCrop()
                        .into(iv_nav_header);
                final TextView tv_username = (TextView)findViewById(R.id.drawer_username);
                tv_username.setText(Singleton.getInstance().getCurrentUser().getName());
                final TextView tv_email = (TextView)findViewById(R.id.drawer_email);
                tv_email.setText(Singleton.getInstance().getCurrentUser().getEmail());
                cv_user_drawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImagePicker.pickImage(activity, "Select Image:");
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

        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userGroups");
        mAdapter = new FirebaseIndexRecyclerAdapter( GroupDatabase.class,
                                                            R.layout.group_item_sample,
                                                            GroupHolder.class, groupRef, ref){


            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {
                ((GroupHolder)viewHolder).setData(model,context);
            }

            @Override
            protected void onChildChanged(ChangeEventListener.EventType type, int index, int oldIndex) {
                super.onChildChanged(type, index, oldIndex);
                tv_no_groups.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                iv_no_groups.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                pb.setVisibility(View.GONE);

            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                tv_no_groups.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                iv_no_groups.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };

        rv.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

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
