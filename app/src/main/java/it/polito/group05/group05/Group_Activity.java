package it.polito.group05.group05;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;

public class Group_Activity extends AppCompatActivity implements  ChatFragment.OnFragmentInteractionListener,ExpenseFragment.OnFragmentInteractionListener, GroupDetailsFragment.OnFragmentInteractionListener {


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
    static FloatingActionButton fab;
    static AppBarLayout appBar;
    static CoordinatorLayout main_content;
    static List<Expense> expenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_);

        context = this;
        final CircleImageView c = (CircleImageView)findViewById(R.id.iv_group_image);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("groups").child(Singleton.getInstance().getmCurrentGroup().getId()).child(Singleton.getInstance().getmCurrentGroup().getPictureUrl()))
                .centerCrop()
                //.placeholder(R.drawable.group_profile)
                .crossFade()
                .into(c);
        appBar = (AppBarLayout)findViewById(R.id.appbar);
        final TextView tv = (TextView)findViewById(R.id.tv_group_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        main_content = (CoordinatorLayout)findViewById(R.id.main_content);
        tv.setText(Singleton.getInstance().getmCurrentGroup().getName());
        //  tv.setText("CIAO");
        /*toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<View, String> p1 = Pair.create((View)appBar, getString(R.string.transition_appbar));
                Pair<View, String> p2 = Pair.create((View)toolbar, getString(R.string.transition_toolbar));
                Pair<View, String> p3 = Pair.create((View)c, getString(R.string.transition_group_image));
                Pair<View, String> p4 = Pair.create((View)tv, getString(R.string.transition_text));
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1, p2, p3, p4);
                Intent intent = new Intent(getBaseContext(), GroupDetailsActivity.class);
                startActivity(intent, options.toBundle());
            }
        });*/
        setSupportActionBar(toolbar);
        // c.setImageBitmap(Singleton.getInstance().getmCurrentGroup().getGroupProfile());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Pair<View, String> p1 = Pair.create((View)appBar, getString(R.string.transition_appbar));
                Pair<View, String> p2 = Pair.create((View)toolbar, getString(R.string.transition_toolbar));
                Pair<View, String> p3 = Pair.create((View)c, getString(R.string.transition_group_image));
                Pair<View, String> p4 = Pair.create((View)tv, getString(R.string.transition_text));
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1, p2, p3, p4);*/

                Intent i = new Intent(getBaseContext(), Expense_activity.class);

                startActivity(i);
                //startActivity(i, options.toBundle());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_, menu);
        return true;
    }*/


    private static void hideViews() {

        //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
        //int fabBottomMargin = lp.bottomMargin;
        //fab.animate().translationY(fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }


    private static void showViews() {

        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

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
    protected void onResume() {
        super.onResume();
        //getSupportFragmentManager().getFragments().get(1).onResume();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

/*
    public static class PlaceholderFragment extends Fragment {
        ExpenseAdapter ea;
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int s) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, s);
            //args.putString(ARG_SECTION_NUMBER,s);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER)==0) {
                final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
                View rootView = inflater.inflate(R.layout.fragment_group_, container, false);
                RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.expense_rv);
                rv.setOnScrollListener(new HideScrollListener() {
                    @Override
                    public void onHide() {
                        hideViews();
                    }
                    @Override
                    public void onShow() {
                        showViews();
                    }
                });
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                //textView.setText(getArguments().getString(ARG_SECTION_NUMBER));
                expenses =new ArrayList<>(currentGroup.getExpenses());
                ea = new ExpenseAdapter(getContext(),expenses);
                LinearLayoutManager llm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                rv.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
                rv.setLayoutManager(llm);
                rv.setAdapter(ea);
                return rootView;
        }
        @Override
        public void onResume() {
            myOnResume();
        }
        public void myOnResume() {
            super.onResume();
            if(ea!=null) {
                expenses = new ArrayList<>(Singleton.getInstance().getmCurrentGroup().getExpenses());
                ea.notifyDataSetChanged();
            }
        }
    }
*/
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
                case 2:
                    fragment =GroupDetailsFragment.newInstance();
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