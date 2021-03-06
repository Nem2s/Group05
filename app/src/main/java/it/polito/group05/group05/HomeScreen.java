package it.polito.group05.group05;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mvc.imagepicker.ImagePicker;
import com.pkmmte.view.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.GroupAdapter;
import it.polito.group05.group05.Utility.BaseClasses.User;

import it.polito.group05.group05.Utility.InvitedAdapter;
import it.polito.group05.group05.Utility.PicassoRoundTransform;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public  static int REQUEST_FROM_NEW_GROUP;
    public  static int REQUEST_FROM_NEW_USER;


    FloatingActionButton fab;
    DrawerLayout drawer;
    User currentUser;
    CircularImageView cv_user_drawer;
    NavigationView navigationView;
    LinearLayout ll_header;
    Activity activity;
    LinearLayout reveal_layout;
    Button addButton;
    MaterialEditText et_group_name;
    RecyclerView rv_invited;
    RecyclerView rv_groups;
    CircularImageView iv_new_group;
    RelativeLayout no_groups;


    //MAGARI SI POSSONO INSERIRE NEL SINGLETON

    Group newgroup = new Group();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");
        if(bitmap != null && REQUEST_FROM_NEW_GROUP == requestCode) {
            iv_new_group.setImageBitmap(bitmap);
            REQUEST_FROM_NEW_GROUP = -1;
        }
        else if(bitmap != null && REQUEST_FROM_NEW_USER == requestCode) {
            cv_user_drawer.setImageBitmap(bitmap);
            drawer.closeDrawers();
            REQUEST_FROM_NEW_USER = -1;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final List<Group> items = Singleton.getInstance().getmCurrentGroups();




        reveal_layout = (LinearLayout) findViewById(R.id.reveal_layout);
        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
        addButton = (Button)findViewById(R.id.add_to_group_button);
        et_group_name = (MaterialEditText)findViewById(R.id.group_name_add);
        iv_new_group = (CircularImageView) findViewById(R.id.iv_new_group);
        addButton.setVisibility(View.INVISIBLE);
        no_groups = (RelativeLayout)findViewById(R.id.no_groups_layout);
        activity = this;
        rv_groups = (RecyclerView)findViewById(R.id.groups_rv);

        LinearLayoutManager groupsManager = new LinearLayoutManager(this);
        final GroupAdapter groupAdapter = new GroupAdapter(items, this);
        rv_groups.setHasFixedSize(true); //La dimensione non cambia nel tempo, maggiori performance.
        rv_groups.setLayoutManager(groupsManager);
        rv_groups.setAdapter(groupAdapter);
        currentUser = new User("q" + 1, "User", new Balance(3, 1), ((BitmapDrawable)getResources().getDrawable(R.drawable.man_1)).getBitmap(), null, true, true);
        currentUser.setContacts(Singleton.getInstance().createRandomListUsers(61, getApplicationContext(), null));
        Singleton.getInstance().setId(currentUser.getId());

        final InvitedAdapter invitedAdapter = new InvitedAdapter(currentUser.getContacts(), this);
        LinearLayoutManager invitedManager = new LinearLayoutManager(this);
        rv_invited.setHasFixedSize(true);
        rv_invited.setLayoutManager(invitedManager);
        rv_invited.setAdapter(invitedAdapter);
        //all.addAll(retriveAllPeople());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_invited.getContext(),
                invitedManager.getOrientation());
        rv_invited.addItemDecoration(dividerItemDecoration);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(HomeScreen.this);
                Pair<View, String> p1 = Pair.create((View)fab, getResources().getString(R.string.transition_fab));
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1);
                startActivity(intent,options.toBundle());*/
                int cx = (fab.getLeft() + fab.getRight())/2;
                int cy = (fab.getBottom() + fab.getTop())/2;
                int dx = Math.max(cx, fab.getWidth() - cx);
                int dy = Math.max(cy, fab.getHeight() - cy);
                float radius = (float) Math.hypot(dx, dy);
                toogleCreateGroupView(cx, cy, radius, reveal_layout, 350);



            }
        });

        iv_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(activity, "Select Image:");
                REQUEST_FROM_NEW_GROUP = ImagePicker.PICK_IMAGE_REQUEST_CODE;

            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newgroup = new Group(et_group_name.getText().toString(), new Balance(0, 0), ((BitmapDrawable)iv_new_group.getDrawable()).getBitmap(), Calendar.getInstance().getTime().toString(), 1);
                newgroup.addMember(currentUser);
                Singleton.getInstance().createRandomListUsers(61, getApplicationContext(), newgroup);
                for (UserContact u : currentUser.getContacts()) {
                    if(u.isSelected()) {
                        newgroup.addMember(u);
                        u.setSelected(false);
                    }

                }

                if(newgroup.getMembers().isEmpty() || et_group_name.getText().toString().equals("")) {
                    Snackbar.make(view, "Missing some Informations!", Snackbar.LENGTH_LONG).show();

                }
                else {
                    items.add(newgroup);
                    groupAdapter.notifyDataSetChanged();
                    reinitializeNewGroupView(currentUser.getContacts());
                    no_groups.setVisibility(View.INVISIBLE);
                    onBackPressed();
                }
                invitedAdapter.notifyDataSetChanged();
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(isOpen)
                    addButton.setVisibility(View.INVISIBLE);
                else
                    addButton.setVisibility(View.VISIBLE);
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
                final TextView tv_username = (TextView)findViewById(R.id.drawer_username);
                final TextView tv_email = (TextView)findViewById(R.id.drawer_email);
                ll_header = (LinearLayout)findViewById(R.id.drawer_ll_header);
                ColorUtils.PaletteBuilder builder = new ColorUtils.PaletteBuilder();
                builder
                        .load(((BitmapDrawable)cv_user_drawer.getDrawable()).getBitmap())
                        .brighter(ColorUtils.bright.DARK)
                        .method(ColorUtils.type.MUTED)
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
        init();

    }

    private void init() {
        if(rv_groups.getAdapter().getItemCount() == 0) {

            no_groups.setVisibility(View.VISIBLE);
            AnimUtils.bounce(fab, 3000,getApplicationContext(), true);
        } else {
            AnimUtils.toggleOn(fab, 350, getApplicationContext());
        }
    }

    private void reinitializeNewGroupView(List<UserContact> users) {
        et_group_name.setText("");
        iv_new_group.setImageResource(R.drawable.network);
    }

    private List<UserContact> retriveAllPeople() {
        List<UserContact> res = new ArrayList<>();
        for(int i = 0; i < 76; i++) {
            UserContact u = new UserContact("p" + i, "User " + i, new Balance(i++, i), null, null, i==3, i==1);
            res.add(u);
        }
        return res;
    }

    public void toogleCreateGroupView (int cx, int cy, float radius, final View revealLayout, int duration) {
        Animator animator =
                ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, 0, radius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        Animator animator_reverse = ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, radius, 0);

        if (revealLayout.getVisibility() == View.INVISIBLE) {
            fab.setEnabled(false);
            revealLayout.setVisibility(View.VISIBLE);
            ((GroupAdapter)rv_groups.getAdapter()).isEnabled = false;
            AnimUtils.bounce((View)addButton,2000, this, false);
            animator.start();
        } else {
            animator_reverse.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    revealLayout.setVisibility(View.INVISIBLE);
                    fab.setEnabled(true);
                    ((GroupAdapter)rv_groups.getAdapter()).isEnabled = true;
                    addButton.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator_reverse.start();
        }
    }

    private void initDrawer() {

        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_balance);
        //item.setTitle("Balance: " + currentUser.getCurrentBalance() + " €");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fab.getVisibility() == View.INVISIBLE)
            AnimUtils.toggleOn(fab, 150, this);
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(reveal_layout.getVisibility() == View.VISIBLE) {
            int cx = (fab.getLeft() + fab.getRight())/2;
            int cy = (fab.getBottom() + fab.getTop())/2;
            int dx = Math.max(cx, fab.getWidth() - cx);
            int dy = Math.max(cy, fab.getHeight() - cy);
            float radius = (float) Math.hypot(dx, dy);
            toogleCreateGroupView(cx, cy, radius, reveal_layout, 350);
            return;
        }
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
