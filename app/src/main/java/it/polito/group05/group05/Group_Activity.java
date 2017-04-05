package it.polito.group05.group05;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import it.polito.group05.group05.Utility.activity_expense;

public class Group_Activity extends AppCompatActivity implements  ChatFragment.OnFragmentInteractionListener,ExpenseFragment.OnFragmentInteractionListener {


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(Singleton.getInstance().getmCurrentGroup().getName());
        toolbar.setNavigationIcon(R.drawable.left_arrow );
        toolbar.setNavigationContentDescription("back" +
                "");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //toolbar.setLogo(Integer.parseInt(Singleton.getInstance().getmCurrentGroup().getGroupProfile()));

        //toolbar.setTitle(Singleton.getInstance().getmCurrentGroup().getName());
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GroupDetailsActivity.class);
                startActivity(intent);

            }
        });
*/

//        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(), activity_expense.class);
                startActivity(i);
                
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_, menu);
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



                final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
                View rootView = inflater.inflate(R.layout.fragment_group_, container, false);
                RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.expense_rv);

                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);

                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                //textView.setText(getArguments().getString(ARG_SECTION_NUMBER));
                List<Expense> expenses =new ArrayList<>(currentGroup.getExpenses());

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
            super.onResume();

                ea.notifyDataSetChanged();

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
                    fragment = ChatFragment.newInstance();
                break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Expenses";
                case 1:
                    return "Chat";

            }
            return null;
        }

    }
}
