package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.EventClasses.ObjectChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.SelectionChangedEvent;
import it.polito.group05.group05.Utility.InvitedAdapter;

public class AddMember extends AppCompatActivity {

    TextView tv_group_name;
    TextView partecipants ;
    CircularImageView iv_group_image;
    TextView Tv_group_name;
    private MenuItem mSearchItem;
    private MenuItem mConfirmItem;
    RecyclerView rv_invited;
    AppBarLayout appBar;
    private Toolbar mToolbar;
    private TextView no_people;
    private SearchView searchView;
    private final User currentUser = Singleton.getInstance().getCurrentUser();
    private InvitedAdapter invitedAdapter;
    private Context context;
    private List<UserContact> contacts;

    final Group currentGroup = Singleton.getInstance().getmCurrentGroup();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
        context = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_group_name = (TextView)findViewById(R.id.tv_group_name);
        partecipants = (TextView)findViewById(R.id.tv_partecipants);
        no_people = (TextView)findViewById(R.id.no_people);
        iv_group_image = (CircularImageView)findViewById(R.id.iv_group_image);
        tv_group_name = (TextView)findViewById(R.id.tv_group_name);
        appBar = (AppBarLayout)findViewById(R.id.appbar);
        iv_group_image.setImageBitmap(currentGroup.getGroupProfile());
        tv_group_name.setText(currentGroup.getName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contacts = new ArrayList<>();

        /**DA AGGIUSTARE **/
        for (UserContact u : currentUser.getContacts()) {
            boolean found = false;
            for (User user : currentGroup.getMembers()) {
                if (user.getUser_name().equals(u.getUser_name())) {
                    found = true;
                    break;
                }
            }
            if(!found){ contacts.add(u);}

        }


        invitedAdapter = new InvitedAdapter(contacts, this);
        LinearLayoutManager invitedManager = new LinearLayoutManager(this);

        rv_invited.setHasFixedSize(true);
        rv_invited.setLayoutManager(invitedManager);
        rv_invited.setAdapter(invitedAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_invited.getContext(),
                invitedManager.getOrientation());
        rv_invited.addItemDecoration(dividerItemDecoration);

    }



    @Subscribe public void onSelectionChangedEvent(SelectionChangedEvent event) {
        if(event.isValid())
            mConfirmItem.setVisible(true);
        else
            mConfirmItem.setVisible(false);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.m_confirm) {
            List<UserContact> newMembers = new ArrayList<>();
            for (UserContact u : contacts) {
                if(u.isSelected()) {
                    newMembers.add(u);
                    EventBus.getDefault().post(new ObjectChangedEvent(u));
                    u.setSelected(false);
                }
            }
            if(!newMembers.isEmpty()) {
                finish();
            }
            else {
                Snackbar.make(findViewById(R.id.parent_layout), "Select someone!", Snackbar.LENGTH_LONG).show();
                return false;

            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchItem = menu.findItem(R.id.m_search);
        mConfirmItem = menu.findItem(R.id.m_confirm);
        mConfirmItem.setVisible(false);
        searchView = (SearchView)mSearchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                no_people.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ( TextUtils.isEmpty ( newText ) ) {
                    ((InvitedAdapter)rv_invited.getAdapter()).getFilter().filter("");
                    no_people.setVisibility(View.GONE);

                } else {
                    ((InvitedAdapter)rv_invited.getAdapter()).getFilter().filter(newText);
                    if(((InvitedAdapter)rv_invited.getAdapter()).getItemCount() == 0)
                        no_people.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    animateSearchToolbar(1, false, false);
                    mConfirmItem.setIcon(R.drawable.ic_action_tick_white);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                animateSearchToolbar(1, true, true);
                mConfirmItem.setIcon(R.drawable.ic_action_tick);
                return true;
            }
        });

        return true;
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        RevealFrameLayout reveal = (RevealFrameLayout)findViewById(R.id.reveal_parent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.quantum_grey_600));
        }

        if (show) {
            int width = mToolbar.getWidth() -
                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
            createCircularReveal.setDuration(250);
            createCircularReveal.start();

        } else {

            int width = mToolbar.getWidth() -
                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, (float) width, 0.0f);
            createCircularReveal.setDuration(250);
            createCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mToolbar.setBackgroundColor(getThemeColor(AddMember.this, R.attr.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getThemeColor(AddMember.this, R.attr.colorPrimaryDark));
                    }
                }
            });
            createCircularReveal.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getThemeColor(AddMember.this, R.attr.colorPrimaryDark));
            }
        }
    }

    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }
}
