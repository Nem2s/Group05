package it.polito.group05.group05;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.pkmmte.view.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.GroupAdapter;
import it.polito.group05.group05.Utility.BaseClasses.User;

import it.polito.group05.group05.Utility.InvitedAdapter;
import it.polito.group05.group05.Utility.PicassoRoundTransform;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    DrawerLayout drawer;
    User currentUser;
    NavigationView navigationView;
    LinearLayout ll_header;
    Activity activity;
    CoordinatorLayout reveal_layout;
    Button addButton;
    MaterialEditText et_group_name;
    RecyclerView rv_invited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<Group> items = new ArrayList<>();
        final Context context = getApplicationContext();
        reveal_layout = (CoordinatorLayout) findViewById(R.id.reveal_layout);
        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
        addButton = (Button)findViewById(R.id.add_to_group_button);
        et_group_name = (MaterialEditText)findViewById(R.id.group_name_add);
        addButton.setVisibility(View.INVISIBLE);
        RecyclerView rv = (RecyclerView)findViewById(R.id.groups_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        final GroupAdapter groupAdapter = new GroupAdapter(items, this);
        rv.setHasFixedSize(true); //La dimensione non cambia nel tempo, maggiori performance.
        rv.setLayoutManager(llm);
        rv.setAdapter(groupAdapter);
        final List<UserContact> all = new ArrayList<>();
        final InvitedAdapter invitedAdapter = new InvitedAdapter(all, this);
        LinearLayoutManager llmi = new LinearLayoutManager(this);
        rv_invited.setHasFixedSize(true);
        rv_invited.setLayoutManager(llmi);
        rv_invited.setAdapter(invitedAdapter);
        all.addAll(retriveAllPeople());
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*DEBUG*/
        currentUser = new User("12", "Bob", new Balance(88, 21), String.valueOf(R.drawable.boy), null, false, false);
        Group g = new Group("Group 1", new Balance(120, 61.1), String.valueOf(R.drawable.hills), Calendar.getInstance().getTime().toString(), 9);
        Random r = new Random();
        int m = r.nextInt(21 - 2) + 2;
        for(int j = 0; j < m; j++) {
            User u = new User("q" + j, "User " + j, new Balance(j++, j), String.valueOf(R.drawable.man), g, j==3, j==1);
            u.setTot_expenses((float) (j*0.75));
            g.addMember(u);
        }
        items.add(g);
        for(int i = 0; i < 8; i++) {
            g = new Group("Group 2", new Balance(12, 0), String.valueOf(R.drawable.beach), Calendar.getInstance().getTime().toString(), 1);
            r = new Random();
            m = r.nextInt(21 - 2) + 2;
            for (int j = 0; j < m; j++) {
                User u = new User("q" + j, "User " + j, new Balance(j++, j), String.valueOf(R.drawable.girl_1), g, j == 3, j == 1);
                u.setTot_expenses((float) (j * 2.5));
                g.addMember(u);
            }
            items.add(g);
        }
        //adapter.addAll(generateRandomGroups(3));

        /*END DEBUG */

        activity = this;
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_group_name.getText().toString().equals("")) {
                    Group g = new Group("Group NEW!", new Balance(14, 12), String.valueOf(R.drawable.money), Calendar.getInstance().getTime().toString(), 1);
                    for (UserContact u : all) {
                        if(u.isSelected()) {
                            g.addMember(u);
                            u.setSelected(false);
                        }
                    }
                    if(g.getMembers().isEmpty())
                        Snackbar.make(view, "Missing some Informations!", BaseTransientBottomBar.LENGTH_LONG).show();
                    else {
                        items.add(g);
                        groupAdapter.notifyDataSetChanged();
                        reinitializeNewGroupView(all);
                        onBackPressed();
                    }
                } else {
                    Snackbar.make(view, "Missing some Informations!", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        initDrawer();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final CircularImageView cv = (CircularImageView)findViewById(R.id.drawer_header_image);
                final TextView tv_username = (TextView)findViewById(R.id.drawer_username);
                final TextView tv_email = (TextView)findViewById(R.id.drawer_email);
                ll_header = (LinearLayout)findViewById(R.id.drawer_ll_header);
                Picasso
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


    }

    private void reinitializeNewGroupView(List<UserContact> users) { ///DA RIVEDERE!
        rv_invited.invalidate();
        rv_invited.scrollToPosition(users.size());
        rv_invited.scrollToPosition(0);
        et_group_name.setText("");
    }

    private List<UserContact> retriveAllPeople() {
        List<UserContact> res = new ArrayList<>();
        for(int i = 0; i < 76; i++) {
            UserContact u = new UserContact("q" + i, "User " + i, new Balance(i++, i), String.valueOf(R.drawable.boy), null, i==3, i==1);
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
                fab.setClickable(false);
                revealLayout.setVisibility(View.VISIBLE);
                AnimUtils.bounce((View)addButton, this);
                animator.start();
            } else {
                animator_reverse.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        revealLayout.setVisibility(View.INVISIBLE);
                        fab.setClickable(true);
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
        item.setTitle("Balance: " + currentUser.getCurrentBalance() + " €");
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
