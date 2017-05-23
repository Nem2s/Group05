package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mvc.imagepicker.ImagePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.Adapter.MemberInvitedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

/**
 * Created by Marco on 05/05/2017.
 */

public class NewMemberActivity extends AppCompatActivity {
    private static DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("groups");

    private static final String TAG = "Error";
    public  static int REQUEST_FROM_NEW_GROUP, INVITE;
    FloatingActionButton fab;
    CircleImageView iv_new_group;
    MaterialEditText et_group_name;
    RecyclerView rv_invited;
    List<UserContact> contacts;
    SwipeRefreshLayout mSwipeLayout;
    private MenuItem mSearchItem;
    public static MenuItem mConfirmItem;
    public static boolean isNameEmpty;
    private Toolbar mToolbar;
    private SearchView searchView;
    private TextView no_people;
    private TextView tv_partecipants;
    private final UserDatabase currentUser = Singleton.getInstance().getCurrentUser();
    private Context context;
    private String[] ids;
    MemberInvitedAdapter invitedAdapter;
    private boolean formIsValid = false;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        if(requestCode==INVITE)
        {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                ids= AppInviteInvitation.getInvitationIds(resultCode, data);
                if( ids != null && ids.length > 0) {
                    no_people.setText("You've invited " + ids.length + " people. Let's create your group!");
                    et_group_name.setEnabled(true);
                    iv_new_group.setEnabled(true);
                    tv_partecipants.setVisibility(View.INVISIBLE);
                    if(mSearchItem != null)
                        mSearchItem.setEnabled(true);
                }


            }
        }
        if (bitmap != null && REQUEST_FROM_NEW_GROUP == requestCode) {
            iv_new_group.setImageBitmap(bitmap);
            REQUEST_FROM_NEW_GROUP = -1;
        }
        else {
            iv_new_group.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.network));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        isNameEmpty = false;
        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
        et_group_name = (MaterialEditText)findViewById(R.id.group_name_add);
        iv_new_group = (CircleImageView) findViewById(R.id.iv_new_group);
        et_group_name.setVisibility(View.GONE);
        iv_new_group.setVisibility(View.GONE);
        no_people = (TextView)findViewById(R.id.no_people);
        context = this;
        //mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_partecipants = (TextView)findViewById(R.id.tv_partecipants);
        setSupportActionBar(mToolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab_invite);
        contacts = new ArrayList<>();
        invitedAdapter = new MemberInvitedAdapter(contacts, context);
        for(UserContact i : Singleton.getInstance().getRegContactsList().values())
            if(!Singleton.getInstance().getmCurrentGroup().getMembers().containsKey(i.getId()))
                contacts.add(i);
       /* mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contacts.clear();
                for(UserContact i : Singleton.getInstance().getRegContactsList().values())
                    if(!Singleton.getInstance().getmCurrentGroup().getMembers().containsKey(i.getId()))
                        contacts.add(i);
                invitedAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);
            }

        });
        */
        if(invitedAdapter.getItemCount() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent_layout), "No contacts stored in your phone, Start invite your friends!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.show();
            final View snackbarView = snackbar.getView();
            snackbarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    snackbarView.getViewTreeObserver().removeOnPreDrawListener(this);
                    ((CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams()).setBehavior(null);
                    return true;
                }
            });

            et_group_name.setEnabled(false);
            iv_new_group.setEnabled(false);

            no_people.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager invitedManager = new LinearLayoutManager(this);
        rv_invited.setHasFixedSize(true);
        rv_invited.setLayoutManager(invitedManager);
        rv_invited.setAdapter(invitedAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_invited.getContext(),
                invitedManager.getOrientation());
        rv_invited.addItemDecoration(dividerItemDecoration);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new AppInviteInvitation.IntentBuilder("")
                        .setMessage("")
                        .setDeepLink(Uri.parse("https://h5uqp.app.goo.gl/"))
                        .build();
                startActivityForResult(intent, INVITE);

            }
        });


        iv_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage((Activity) context, "Select Image:");
                REQUEST_FROM_NEW_GROUP = ImagePicker.PICK_IMAGE_REQUEST_CODE;

            }
        });
        /**TROVARE METODO ALTERNATIVO **/
        //checkSelected();

        et_group_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                EventBus.getDefault().post(SelectionChangedEvent.onTextChangedEvent(charSequence.length() > 0));
            }

            @Override
            public void afterTextChanged(Editable editable){

            }
        });

    }

    /*
        @Subscribe
        public void onTextChangedEvent(TextChangedEvent event) {
            this.textIsValid = event.isValid();
            if(textIsValid && selectionIsValid)
                mConfirmItem.setVisible(true);
            else
                mConfirmItem.setVisible(false);
        }

        @Subscribe public void onSelectionChangedEvent(SelectionChangedEvent event) {
            this.selectionIsValid = event.isValid();
            if(textIsValid && selectionIsValid)
                mConfirmItem.setVisible(true);
            else
                mConfirmItem.setVisible(false);
        }


        @Override
        protected void onStop() {
            EventBus.getDefault().unregister(this);
            super.onStop();
        }

        @Override
        protected void onStart() {
            super.onStart();
            if(!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchItem = menu.findItem(R.id.m_search);
        mConfirmItem = menu.findItem(R.id.m_confirm);
        mConfirmItem.setVisible(false);
        searchView = (SearchView)mSearchItem.getActionView();

        if(!et_group_name.isEnabled())
            mSearchItem.setEnabled(false);
        else
            mSearchItem.setEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                no_people.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ( TextUtils.isEmpty ( newText ) ) {
                    ((MemberInvitedAdapter)rv_invited.getAdapter()).getFilter().filter("");
                    no_people.setVisibility(View.GONE);
                } else {
                    ((MemberInvitedAdapter)rv_invited.getAdapter()).getFilter().filter(newText);
                    if(((MemberInvitedAdapter)rv_invited.getAdapter()).getItemCount() == 0)
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

    @Subscribe public void onSelectionChangedEvent(SelectionChangedEvent event) {
        SelectionChangedEvent.getValues(this);
        this.formIsValid = event.isValidForNewMember();
        if(formIsValid) {
            mConfirmItem.setVisible(true);
        }
        else
            mConfirmItem.setVisible(false);
    }


    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        SelectionChangedEvent.resetValues();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (UserContact u : contacts) {
            if(u.isSelected()) {
                u.setSelected(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.m_confirm) {
            for (UserContact u : contacts) {
                if(u.isSelected()) {
                    Singleton.getInstance().getmCurrentGroup().members.put(u.getId(), u);
                    DB_Manager.getInstance().addUserToGroup(u.getId(),Singleton.getInstance().getmCurrentGroup().getId());
                    u.setSelected(false);
                }

            }
                invitedAdapter.notifyDataSetChanged();
                finish();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
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
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getMeasuredHeight() / 2, (float) width, 0.0f);
            createCircularReveal.setDuration(250);
            createCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mToolbar.setBackgroundColor(getThemeColor(NewMemberActivity.this, R.attr.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getThemeColor(NewMemberActivity.this, R.attr.colorPrimaryDark));
                    }
                }
            });
            createCircularReveal.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getThemeColor(NewMemberActivity.this, R.attr.colorPrimaryDark));
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
