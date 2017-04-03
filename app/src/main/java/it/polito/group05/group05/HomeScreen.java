package it.polito.group05.group05;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.GroupAdapter;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.PicassoRoundTransform;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    DrawerLayout drawer;
    User currentUser;
    NavigationView navigationView;
    LinearLayout ll_header;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Group> items = new ArrayList<>();
        GroupAdapter adapter = new GroupAdapter(this, items, this);
        final Context context = getApplicationContext();
        ListView listView = (ListView)findViewById(R.id.groups_lv);
        listView.setAdapter(adapter);
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
        adapter.add(g);
        for(int i = 0; i < 25; i++) {
            g = new Group("Group 2", new Balance(12, 0), String.valueOf(R.drawable.beach), Calendar.getInstance().getTime().toString(), 1);
            r = new Random();
            m = r.nextInt(21 - 2) + 2;
            for (int j = 0; j < m; j++) {
                User u = new User("q" + j, "User " + j, new Balance(j++, j), String.valueOf(R.drawable.girl_1), g, j == 3, j == 1);
                u.setTot_expenses((float) (j * 2.5));
                g.addMember(u);
            }
            adapter.add(g);
        }
        //adapter.addAll(generateRandomGroups(3));

        /*END DEBUG */


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
