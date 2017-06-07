package it.polito.group05.group05;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.BottomNavBgMode;
import com.afollestad.aesthetic.BottomNavIconTextMode;
import com.afollestad.aesthetic.NavigationViewMode;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mvc.imagepicker.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import it.polito.group05.group05.Utility.Adapter.GroupAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ColorItem;
import it.polito.group05.group05.Utility.BaseClasses.CurrentUser;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Event.ReadyEvent;
import it.polito.group05.group05.Utility.HelperClasses.AnimUtils;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;
import it.polito.group05.group05.Utility.Holder.GroupHolder;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AestheticActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Map<View, String[]> map = new LinkedHashMap<View, String[]>();
    private static final int COMING_FROM_BALANCE_ACTIVITY = 123;
    private static int CUSTOM_THEME_OPTION = 0;
    private static int PREDEFINED_THEME_OPTION = 0;
    public  static int REQUEST_FROM_NEW_USER;
    private static String THEME_HELPER = "Primary";
    private static final String PRIMARY = "Primary";
    private static final String ACCENT = "Accent";
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    CircleImageView cv_user_drawer;
    Activity activity;
    Context context;
    ImageView iv_no_groups;
    TextView tv_no_groups;
    static GroupAdapter mAdapter;
    RecyclerView rv;
    ImageView iv_nav_header;
    int colors[] = new int[2];

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");
        if (requestCode == COMING_FROM_BALANCE_ACTIVITY) {
            drawer.openDrawer(Gravity.START);
        }
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


    @Subscribe
    public void groupStart(ReadyEvent cu) {
        Singleton.getInstance().setmCurrentGroup(cu.getGroupDatabase());
        Singleton.getInstance().setIdCurrentGroup(cu.getGroupDatabase().getId());
        Intent i = new Intent(context, GroupActivity.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            i.putExtras(bundle);
            getIntent().putExtra("groupId","");
        }
        context.startActivity(i);
    }

    public static void refresh(){

        mAdapter.notifyDataSetChanged();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            finish();
            return;
        }

        final String tkn = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("fcmToken").setValue(tkn);


        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        iv_no_groups = (ImageView)findViewById(R.id.iv_no_groups);
        tv_no_groups = (TextView)findViewById(R.id.tv_no_groups);
        rv = (RecyclerView) findViewById(R.id.groups_rv);
         ProgressBar pb = (ProgressBar)findViewById(R.id.pb_loading_groups);

        setSupportActionBar(toolbar);

        activity = this;
        FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("fcmToken").setValue(FirebaseInstanceId.getInstance().getToken());
        /**DEBUGG**/
        Singleton.getInstance().setCurrContext(getApplicationContext());
        context = this;
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent i =  new Intent(MainActivity.this, NewGroupActivity.class);
                    startActivity(i);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(final View drawerView, float slideOffset) {
                cv_user_drawer = (CircleImageView)findViewById(R.id.drawer_header_image);
                iv_nav_header = (ImageView) findViewById(R.id.background_nav_header);
                ImageUtils.LoadMyImageProfile(cv_user_drawer, context);
                ImageUtils.LoadMyImageProfile(iv_nav_header, context);
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
                map.clear();
                    map.put(cv_user_drawer, new String[]{"Profile Image", "Clicking on it you'll be able to change your profile image"});
                    ImageUtils.showTutorial(activity, map);


            }


            @Override
            public void onDrawerClosed(View drawerView) {
                toggle.syncState();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        Query ref = FirebaseDatabase.getInstance().getReference("groups").orderByChild("lmTime");
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userGroups");

        mAdapter = new GroupAdapter(this, tv_no_groups, iv_no_groups, pb);
        /*mAdapter = new FirebaseIndexRecyclerAdapter( GroupDatabase.class,
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
*/
        rv.setAdapter(mAdapter);
        checkBundle();
            map.put(fab, new String[]{"Floating Action Button", "With this you can create a new group with yout friends!"});
            ImageUtils.showTutorial(activity, map);



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


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
      //  getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_balance) {
            Pair<View, String> p = new Pair<>((View) cv_user_drawer, getResources().getString(R.string.transition_group_image));
            item.setChecked(false);
            AnimUtils.startActivityForResultWithAnimation(this, new Intent(this, UserBalanceActivity.class), COMING_FROM_BALANCE_ACTIVITY, p);
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
            item.setChecked(false);
        } else if (id == R.id.nav_share) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet's try out Share Cash! It's awesome!\n\n";
                sAux = sAux + "https://h5uqp.app.goo.gl/ZjWo \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
                item.setChecked(false);
            } catch(Exception e) {
                //e.toString();
            }
           /* Intent intent = new AppInviteInvitation.IntentBuilder("Share")
                    .setMessage("Let's try out Share Cash! It's awesome!")
                    .build();
            startActivityForResult(intent, 1);*/

        } else if (id == R.id.nav_logout) {
          AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                    finish();
                }
            });

            item.setChecked(false);
        } else if (id == R.id.nav_themes) {
                FastItemAdapter adapter = new FastItemAdapter<ColorItem>();
                adapter.withSelectable(true);
                adapter.add(generateThemes());
                final MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title("Select one Theme")
                        .titleColor(getResources().getColor(R.color.colorAccent))
                        .adapter(adapter, new LinearLayoutManager(context))
                        .positiveText("Custom Themes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull final DialogAction which) {
                                THEME_HELPER = "Primary";
                                customThemeMethod();
                                dialog.dismiss();
                            }
                        })
                        .show();

                adapter.withOnClickListener(new FastAdapter.OnClickListener() {
                    @Override
                    public boolean onClick(View view, IAdapter iAdapter, IItem item, int i) {
                        colors[0] = ((ColorItem)item).getAccentColor();
                        colors[1] = ((ColorItem)item).getPrimaryColor();
                        dialog.dismiss();
                        setupTheme(colors);
                        return true;
                    }
                });
            item.setChecked(false);
        } else if (id == R.id.nav_contacts) {
            Intent i = new Intent();
            i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
            i.setAction("android.intent.action.MAIN");
            i.addCategory("android.intent.category.LAUNCHER");
            i.addCategory("android.intent.category.DEFAULT");
            startActivity(i);
            item.setChecked(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }

    private void customThemeMethod() {
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose " + THEME_HELPER + " color")
                .initialColor(THEME_HELPER.equals(PRIMARY) ? getResources().getColor(R.color.blue_400) :
                        getResources().getColor(R.color.colorAccent))
                .showColorPreview(true)
                .lightnessSliderOnly()
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)

                .setPositiveButton(THEME_HELPER.equals(PRIMARY) ? "Select Accent" : "Let's Theme it!", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        if(THEME_HELPER.equals(PRIMARY)) {
                            THEME_HELPER = ACCENT;
                            colors[1] = selectedColor;
                            customThemeMethod();
                        } else {
                            colors[0] = selectedColor;
                            THEME_HELPER = PRIMARY;
                            CUSTOM_THEME_OPTION = 0;
                            setupTheme(colors);
                        }

                    }
                })
                .build()
                .show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    public void setupTheme(int[] colors) { //0 accent, 1 primary
        final int accent = colors[0]; /**Controllare se il color primary è scuro o no per settare i text colors **/
        final int primary = colors[1];
        int text_primary;
        int text_secondary;
        final boolean[] isdark = {false};
        if(isColorDark(primary)) {
            MaterialDialog dialog = new MaterialDialog.Builder(context)
                    .title("Choose Dark or Light Theme")
                    .positiveText("Light")
                    .negativeText("Dark")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            initializeAesthetic(primary, accent, false);
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            initializeAesthetic(primary, accent, true);
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            initializeAesthetic(primary, accent, false);
        }
    }

    private void initializeAesthetic(int primary, int accent ,boolean dark) {
        Singleton.getInstance().setColors(colors);
        if(dark) {
            Aesthetic.get()
                    .activityTheme(R.style.Theme_AppCompat_NoActionBar)
                    .isDark(true)
                    .colorPrimary(primary)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .colorAccent(accent)
                    .navigationViewMode(
                            NavigationViewMode.SELECTED_ACCENT
                    )
                    .bottomNavigationIconTextMode(
                            BottomNavIconTextMode.BLACK_WHITE_AUTO
                    )
                    .bottomNavigationBackgroundMode(
                            BottomNavBgMode.BLACK_WHITE_AUTO
                    )
                    .colorWindowBackground(Color.parseColor("#303030"))
                    .apply();
        }
        else {
            Aesthetic.get()
                    .activityTheme(R.style.Theme_AppCompat_Light_NoActionBar)
                    .isDark(false)
                    .colorPrimary(primary)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .colorAccent(accent)
                    .navigationViewMode(
                            NavigationViewMode.SELECTED_ACCENT
                    )
                    .bottomNavigationIconTextMode(
                            BottomNavIconTextMode.BLACK_WHITE_AUTO
                    )
                    .bottomNavigationBackgroundMode(
                            BottomNavBgMode.BLACK_WHITE_AUTO
                    )
                    .colorWindowBackground(Color.parseColor("#FAFAFA"))
                    .apply();
        }
        CUSTOM_THEME_OPTION = 0;
        PREDEFINED_THEME_OPTION = 0;
    }

    private void checkBundle() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        String groupId = getIntent().getStringExtra("groupId");
        if (groupId != null) {
            if(groupId.equals("")) return;
            FirebaseDatabase.getInstance().getReference("groups").child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) return;
                    //    if(!(dataSnapshot.getValue() instanceof GroupDatabase)) return ;
                    GroupDatabase g = dataSnapshot.getValue(GroupDatabase.class);
                    EventBus.getDefault().post(new ReadyEvent(g));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
    private List<ColorItem> generateThemes() {
        List<ColorItem> themes = new ArrayList<>();
        themes.add(new ColorItem(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), "Blue Wolf (Default)"));
        themes.add(new ColorItem(Color.parseColor("#ffd740"), Color.parseColor("#4a148c"), "Lakers Theme"));
        themes.add(new ColorItem(Color.parseColor("#607d8b"), Color.parseColor("#ff8f00"), "Robin Hood"));
        themes.add(new ColorItem(Color.parseColor("#e91e63"), Color.parseColor("#ffd740"), "Cake Piece"));
        themes.add(new ColorItem(Color.parseColor("#fafafa"), Color.parseColor("#64ffda"), "Alien"));
        themes.add(new ColorItem(Color.parseColor("#37474f"), Color.parseColor("#64ffda"), "Black Alien"));
        return themes;
    }

    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.7){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}
