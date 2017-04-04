package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.GroupAdapter;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.activity_expense;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
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

        Group g = new Group("Group 1", new Balance(120, 61.1), String.valueOf(R.drawable.hills), Calendar.getInstance().getTime().toString(), 9);
        Random r = new Random();
        int m = r.nextInt(21 - 2) + 2;
        for(int j = 0; j < m; j++) {
            User u = new User("q" + j, "User " + j, new Balance(j++, j), String.valueOf(R.drawable.man), g, j==3, j==1);
            u.setTot_expenses((float) (j*0.75));
            g.addMember(u);
        }
        adapter.add(g);
        g = new Group("Group 2", new Balance(12, 0), String.valueOf(R.drawable.beach), Calendar.getInstance().getTime().toString(), 1);
        r = new Random();
        m = r.nextInt(21 - 2) + 2;
        for(int j = 0; j < m; j++) {
            User u = new User("q" + j, "User " + j, new Balance(j++, j), String.valueOf(R.drawable.girl_1), g, j==3, j==1);
            u.setTot_expenses((float) (j*2.5));
            g.addMember(u);
        }
        adapter.add(g);
        //adapter.addAll(generateRandomGroups(3));
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, activity_expense.class);
                startActivity(i);
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
    protected void onResume() {
        super.onResume();
        if(fab.getVisibility() == View.INVISIBLE)
            AnimUtils.toggleOn(fab, 150, this);
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
            Random r = new Random();
            int m = r.nextInt(21 - 2) + 2;
            for(int j = 0; j < m; j++) {

            }

            ret.add(g);
        }

        return ret;
    }


}
