package it.polito.group05.group05;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.ui.User;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.UndoHelper;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.mikepenz.materialize.util.UIUtils;
import com.mvc.imagepicker.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codetail.animation.ViewAnimationUtils;

import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;
import it.polito.group05.group05.Utility.Holder.MemberContactItem;
import it.polito.group05.group05.Utility.Holder.MemberInvitedItem;
import it.polito.group05.group05.Utility.Interfaces.Namable;

import static it.polito.group05.group05.R.id.recyclerView;


/**

 * Created by Marco on 05/05/2017.

 */


public class NewGroupActivity extends AestheticActivity {

    private static DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("groups");
    private FastItemAdapter<MemberContactItem> fastContactAdapter;
    private FooterAdapter<ProgressItem> footerAdapter;

    private static final String TAG = "Error";

    public  static int REQUEST_FROM_NEW_GROUP, INVITE;

    FloatingActionButton fab;

    CircleImageView iv_new_group;

    EditText et_group_name;
    TextView tv_contacts;
    RecyclerView rv_invited;
    RecyclerView rv_contacts;
    List<UserContact> contacts;
    List<UserContact> local_contacts = new ArrayList<>(Singleton.getInstance().getLocalContactsList(0));
    SwipeRefreshLayout mSwipeLayout;
    NestedScrollView nsv;
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

    private boolean formIsValid = false;
    private Activity activity;
    private FastItemAdapter<MemberInvitedItem> fastInvitedAdapter;


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

            iv_new_group.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.group_placeholder));

        }


    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_group);
        activity = this;
        et_group_name = (EditText) findViewById(R.id.group_name_add);
        iv_new_group = (CircleImageView) findViewById(R.id.iv_new_group);
        no_people = (TextView)findViewById(R.id.no_people);
        context = this;
        tv_contacts = (TextView)findViewById(R.id.tv_contacts);
        //mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        rv_contacts = (RecyclerView)findViewById(R.id.contacts_people_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_partecipants = (TextView)findViewById(R.id.tv_partecipants);
        setSupportActionBar(mToolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab_invite);

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






        //create our FastAdapter which will manage everything
        fastContactAdapter = new FastItemAdapter<>();
        fastInvitedAdapter = new FastItemAdapter<>();

        LinearLayoutManager contactsManager = new LinearLayoutManager(getApplicationContext());
        contactsManager.setAutoMeasureEnabled(true);
        LinearLayoutManager invitedManager = new LinearLayoutManager(getApplicationContext());
        //create our FooterAdapter which will manage the progress items
        footerAdapter = new FooterAdapter<>();
        rv_contacts.setLayoutManager(contactsManager);
        rv_contacts.setHasFixedSize(false);
        //rv_contacts.setNestedScrollingEnabled(false);
        //configure the itemAdapter
        fastContactAdapter.withFilterPredicate(new IItemAdapter.Predicate<MemberContactItem>() {
            @Override
            public boolean filter(MemberContactItem item, CharSequence constraint) {
                //return true if we should filter it out
                //return false to keep it
                return !item.name.toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });
        fastContactAdapter.add(MemberContactItem.fromUserContactList(local_contacts));
        rv_contacts.setAdapter(footerAdapter.wrap(fastContactAdapter));
        rv_contacts.addOnScrollListener(new EndlessRecyclerOnScrollListener(footerAdapter) {
            @Override
            public void onLoadMore(final int currentPage) {
                footerAdapter.clear();
                        int from = fastContactAdapter.getAdapterItemCount();
                        fastContactAdapter.add(from,
                                MemberContactItem.fromUserContactList(Singleton.getInstance().getLocalContactsList(from -1)));
                        //fastContactAdapter.notifyItemRangeInserted(from ,to);

                    }
                });

        fastInvitedAdapter.withSavedInstanceState(savedInstanceState);
        setupInvited();


    }

    private void setupInvited() {
        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);

        //dialog = ProgressDialog.show(activity, "Loading", "Loading contacts...", true, false);

        fastInvitedAdapter.withFilterPredicate(new IItemAdapter.Predicate<MemberInvitedItem>() {
            @Override
            public boolean filter(MemberInvitedItem item, CharSequence constraint) {
                //return true if we should filter it out
                //return false to keep it
                return !item.name.toLowerCase().startsWith(constraint.toString().toLowerCase());
            }
        });
        LinearLayoutManager invitedManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_invited.getContext(),

                invitedManager.getOrientation());

        rv_invited.setLayoutManager(invitedManager);
        rv_invited.addItemDecoration(dividerItemDecoration);

        contacts = new ArrayList<>(Singleton.getInstance().getRegContactsList().values());

        Collections.sort(contacts, new Comparator<UserContact>() {
            @Override
            public int compare(UserContact u1, UserContact u2) {
                return u1.getName().compareTo(u2.getName());
            }
        });
        fastInvitedAdapter.add(MemberInvitedItem.fromUserContactList(contacts));
        fastInvitedAdapter.setHasStableIds(true);
        fastInvitedAdapter.withSelectable(true);
        fastInvitedAdapter.withMultiSelect(true);

        rv_invited.setAdapter(fastInvitedAdapter);

        fastInvitedAdapter.withOnClickListener(new FastAdapter.OnClickListener<MemberInvitedItem>() {
            @Override
            public boolean onClick(View view, IAdapter<MemberInvitedItem> iAdapter, MemberInvitedItem item, int i) {
                item.setSelected(!item.isSelected());
                ((MemberInvitedItem.ViewHolder)item.getViewHolder(view)).handleButton(!item.isSelected(), context);

                return true;
            }
        });


        if(fastInvitedAdapter.getAdapterItemCount() == 0) {

            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent_layout), "No contacts have ShareCash installed, Start invite your friends!", Snackbar.LENGTH_INDEFINITE)

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


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundel
        outState = fastInvitedAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.new_group_menu, menu);

        mSearchItem = menu.findItem(R.id.m_search);

        mConfirmItem = menu.findItem(R.id.m_confirm);

        searchView = (SearchView)mSearchItem.getActionView();
        mConfirmItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DatabaseReference ref = groupRef.push();

                GroupDatabase group = new GroupDatabase();

                group.setMembers(new HashMap<String,Object>());

                group.setId(ref.getKey());

                group.setName(et_group_name.getText().toString());

                group.setCreator(Singleton.getInstance().getCurrentUser().getId());

                for (UserContact u : contacts) {

                    if(u.isSelected()) {

                        group.getMembers().put(u.getId(), 0.0);

                        u.setSelected(false);

                    }


                }

                if(!group.getMembers().isEmpty() && et_group_name.getText().length() > 0) {

                    group.getMembers().put(currentUser.getId(), 0.0);
                    DB_Manager.getInstance().setContext(context).pushNewGroup(group, ((BitmapDrawable)iv_new_group.getDrawable()).getBitmap());

                    finish();

                } else {

                    Snackbar.make(findViewById(R.id.parent_layout), "Missing some Informations!", Snackbar.LENGTH_LONG).show();


                }

                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                                              @Override

                                              public boolean onQueryTextSubmit(String query) {
                                                  getWindow().setSoftInputMode(
                                                          WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                                  );
                                                  return false;

                                              }

                                              @Override

                                              public boolean onQueryTextChange(String newText) {


                                                  if (TextUtils.isEmpty(newText)) {

                                                      fastInvitedAdapter.filter("");
                                                      fastContactAdapter.filter("");
                                                      tv_partecipants.setVisibility(View.VISIBLE);
                                                      tv_contacts.setVisibility(View.VISIBLE);
                                                      no_people.setVisibility(View.GONE);

                                                  } else {

                                                      fastInvitedAdapter.filter(newText);
                                                      fastContactAdapter.filter(newText);
                                                      if (fastInvitedAdapter.getAdapterItemCount() == 0 &&
                                                              fastContactAdapter.getAdapterItemCount() == 0) {
                                                          no_people.setVisibility(View.VISIBLE);
                                                          tv_partecipants.setVisibility(View.GONE);
                                                          tv_contacts.setVisibility(View.GONE);
                                                      } else {
                                                          tv_partecipants.setVisibility(View.VISIBLE);
                                                          tv_contacts.setVisibility(View.VISIBLE);
                                                      }

                                                      return true;
                                                  }

                                                  rv_contacts.smoothScrollToPosition(0);
                                                  rv_invited.smoothScrollToPosition(0);

                                                  return false;
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

        mToolbar.setTitle("Let's create a new Group!");
        return true;

    }


    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {


        mToolbar.setBackgroundColor(Aesthetic.get().colorPrimary().take(1).blockingFirst());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(Aesthetic.get().colorStatusBar().take(1).blockingFirst());

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

                    mToolbar.setBackgroundColor(Aesthetic.get().colorPrimary().take(1).blockingFirst());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(Aesthetic.get().colorStatusBar().take(1).blockingFirst());

                    }

                }

            });

            createCircularReveal.start();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                getWindow().setStatusBarColor(Aesthetic.get().colorStatusBar().take(1).blockingFirst());

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (UserContact u : contacts) {
            if (u.isSelected()) {
                u.setSelected(false);
            }
        }
        finish();
    }



    }

