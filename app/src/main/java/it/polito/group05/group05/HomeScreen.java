package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import it.polito.group05.group05.Utility.Group;
import it.polito.group05.group05.Utility.GroupAdapter;
import it.polito.group05.group05.Utility.ProfileImageActivity;
import it.polito.group05.group05.Utility.User;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Group> items = new ArrayList<>();
        GroupAdapter adapter = new GroupAdapter(this, items);
        Context context = getApplicationContext();
        ListView listView = (ListView)findViewById(R.id.groups_lv);
        listView.setAdapter(adapter);
        Group g = new Group();
        g.setName("Group ");
        g.setGroupProfile("http://images.clipartpanda.com/group-prayer-images-32.png");
        g.setLmTime(Calendar.getInstance().getTime().toString());
        g.setBadge(12);
        g.setBalance("Credit + 20");
        Random r = new Random();
        int m = r.nextInt(21 - 2) + 2;
        for(int j = 0; j < m; j++) {
            User u = new User();
            u.setAdministrator(j >2);
            u.setCardEnabled(j > 5);
            u.setBalance(String.valueOf( j -2 >9?"Credit " + j:"Debit " + j));
            u.setProfile_image("https://unsplash.it/300/300/?random");
            u.setUser_name("User " + j);
            u.setUser_group(g);
            u.setId(String.valueOf(j + 3));
            g.addUser(u);
        }
        adapter.add(g);
        adapter.addAll(generateRandomGroups(3));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
            g.setGroupProfile(Uri);
            g.setLmTime(c.getTime().toString());
            g.setBadge(i - 1>0?i:0);
            g.setBalance(String.valueOf( i+(n-1)>9?"Credit " + i+1:"Debit " + i));
            Random r = new Random();
            int m = r.nextInt(21 - 2) + 2;
            for(int j = 0; j < m; j++) {
                User u = new User();
                u.setAdministrator(j > i + 1);
                u.setCardEnabled(j > i+(j-1));
                u.setBalance(String.valueOf( j -2 >9?"Credit " + i:"Debit " + j));
                u.setProfile_image(Uri);
                u.setUser_name("User " + j);
                u.setUser_group(g);
                u.setId(String.valueOf(j + i));
                g.addUser(u);
            }

            ret.add(g);
        }

        return ret;
    }


}
