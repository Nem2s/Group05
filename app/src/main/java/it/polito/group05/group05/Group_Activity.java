package it.polito.group05.group05;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.NavigationViewMode;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Event.LeaveGroupEvent;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

public class Group_Activity extends AestheticActivity implements  ChatFragment.OnFragmentInteractionListener,ExpenseFragment.OnFragmentInteractionListener {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Context context;
    static Toolbar toolbar;

    static AppBarLayout appBar;
    static CoordinatorLayout main_content;
    static List<Expense> expenses;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_);
        if (Aesthetic.isFirstTime()) {
            int[] colors = Singleton.getInstance().getColors();
            Aesthetic.get()
                    .colorPrimary(colors[1])
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .colorAccent(colors[0])
                    .navigationViewMode(
                            NavigationViewMode.SELECTED_ACCENT
                    )
                    .apply();
        }
        context = this;
        final CircleImageView c = (CircleImageView)findViewById(R.id.iv_group_image);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance()
                        .getReference("groups").child(Singleton.getInstance().getmCurrentGroup().getId())
                        .child(Singleton.getInstance().getmCurrentGroup().getPictureUrl()))
                .centerCrop()
                //.placeholder(R.drawable.group_profile)
                .crossFade()
                .into(c);
        appBar = (AppBarLayout)findViewById(R.id.appbar);
        final TextView tv = (TextView)findViewById(R.id.tv_group_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        main_content = (CoordinatorLayout)findViewById(R.id.main_content);
        tv.setText(Singleton.getInstance().getmCurrentGroup().getName());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Subscribe
   public void leaveGroupEventMethod(LeaveGroupEvent lge){
        try {
            if (lge.canLeave()) {
                DB_Manager.getInstance().removeUserFromGroup(Singleton.getInstance().getCurrentUser().getId(), Singleton.getInstance().getmCurrentGroup().getId());
                finish();
            } else
                if(lge.isCredit())
                    Toast.makeText(this, "Could you loose money?", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "You have debits not payed", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){}

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_, menu);
        return true;
    }


    private static void hideViews() {

        //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
        //int fabBottomMargin = lp.bottomMargin;
        //fab.animate().translationY(fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }







    private static void showViews() {

        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
      //  fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

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
        switch(item.getItemId()){

            case R.id.add_member:
                Intent i = new Intent(this,NewMemberActivity.class);
                startActivity(i);
                break;
            case R.id.leave_group:
                DB_Manager.getInstance().checkUserDebtRemoving();

                break;
            case R.id.action_settings:
                break;
            case R.id.changeBackground:
                //(findViewById(R.id.activity_main)).setBackground(R.drawable.haring1);

                break;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment =null;
            switch (position){
                case 0:
                    fragment=ExpenseFragment.newInstance();
                    break;
                case 1:
                    fragment =ChatFragment.newInstance();
                    break;
                case 2:

                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Expenses";
                case 1:
                    return "Chat";
                case 2:
                    return "Details";
            }
            return null;
        }

    }


}
