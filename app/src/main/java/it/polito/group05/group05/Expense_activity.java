package it.polito.group05.group05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.Event.ExpenseDividerEvent;
import it.polito.group05.group05.Utility.Event.PartecipantsNumberChangedEvent;
import it.polito.group05.group05.Utility.Event.PriceChangedEvent;
import it.polito.group05.group05.Utility.Event.PriceErrorEvent;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;


public class Expense_activity extends AppCompatActivity {

    private CoordinatorLayout parent;
    private MaterialEditText et_name, et_description, et_cost;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal, cb_details, cb_policy;
    private Spinner  spinner_deadline;
    private RecyclerView recyclerView;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private CircleImageView iv_group_image;
    private CardView cardView;
    private TextView tv_group_name;
    private FloatingActionButton fab;
    private ImageView image_network, info;
    private CardView card_recycler;
    private LinearLayout layout_policy;

    ////////////////////////////////////////
    private ExpenseDatabase expense;
    private String expense_name;
    private Double expense_price;
    private String expense_description;
    private int expense_deadline;
    private TYPE_EXPENSE expense_type;
    private UserDatabase expense_owner;
    private GroupDatabase actual_group;
    private RelativeLayout rel_info;
    private Double costPerUser = 0.0;
    private DatabaseReference fdb;
    private List<User_expense> partecipants = new ArrayList<>();
    private Map<String, User_expense> list = new TreeMap<>();
    private boolean isEqualPart=true;
    private Uri uri;
    private boolean newFile = false;
    private String nameFILE= null;
    private File fileUploaded;
    private Context context;
    private UploadTask uploadTask;
    private InputStream stream;
    FirebaseStorage storage;
    final StorageReference fileRef = null;


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


    @Subscribe
    public void onPartecipantsChanged(PartecipantsNumberChangedEvent event) {
        int n_custom = 0;
        double p_custom = 0;
        if(event.hasExpenseEvent()) {
            n_custom = event.getEvent().getNPeople();
            p_custom = event.getEvent().getTotal();
        }
        if(event.getN() < 0) {
            list.remove(event.getUser().getId());
            event.getUser().getExpense().getMembers().remove(event.getUser().getId());
            Toast.makeText(getApplicationContext(), "Removed " + event.getUser().getName(), Toast.LENGTH_SHORT).show();
        }
        else {
            list.put(event.getUser().getId(), event.getUser());
            Toast.makeText(getApplicationContext(), "Added " + event.getUser().getName(), Toast.LENGTH_SHORT).show();
        }

        costPerUser = ((expense.getPrice() - p_custom)/(list.size() - n_custom));
        DecimalFormat df=new DecimalFormat("0.00");
        String formate = df.format(costPerUser);
        double finalValue = Double.parseDouble(formate.replace(',', '.'));
        EventBus.getDefault().post(new PriceChangedEvent(finalValue));
        // EventBus.getDefault().post(new PriceChangedEvent(costPerUser));
    }

    @Subscribe
    public void onCustomValueSetted(ExpenseDividerEvent event) {
        if(event.getTotal() > expense.getPrice()) {
            Snackbar.make(parent, "Defined cost is higher than the total expense cost!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            costPerUser =  (expense.getPrice()/(list.size()));
                            DecimalFormat df=new DecimalFormat("0.00");
                            String formate = df.format(costPerUser);
                            double finalValue = Double.parseDouble(formate.replace(',', '.'));
                            EventBus.getDefault().post(new PriceErrorEvent());
                            EventBus.getDefault().post(new PriceChangedEvent(finalValue));
                        }
                    })
                    .show();
        }
        double total = expense.getPrice() - event.getTotal();
        costPerUser =  (total/(list.size() - event.getNPeople()));
        DecimalFormat df=new DecimalFormat("0.00");
        String formate = df.format(costPerUser);
        double finalValue = Double.parseDouble(formate.replace(',', '.'));
        EventBus.getDefault().post(new PriceChangedEvent(finalValue));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_expense_v2);
        costPerUser = 0.0;
        expense_price = 0.0;
        expense= new ExpenseDatabase();
        expense.setPrice(0.0);
        expense.setOwner(Singleton.getInstance().getCurrentUser().getId());
        parent = (CoordinatorLayout)findViewById(R.id.parent_layout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        iv_group_image= (CircleImageView) findViewById(R.id.iv_group_image);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        image_network = (ImageView) findViewById(R.id.image_network);
        et_name = (MaterialEditText) findViewById(R.id.et_name_expense);
        et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_cost = (MaterialEditText) findViewById(R.id.et_cost_expense);
        cb_details= (CheckBox) findViewById(R.id.more_details);
        cardView = (CardView) findViewById(R.id.card_view2_toshow);
        cardView.setVisibility(View.GONE);
        cb_description = (CheckBox) findViewById(R.id.cb1_description);
        et_description = (MaterialEditText) findViewById(R.id.et_description_expense);
        et_description.setVisibility(View.GONE);
        cb_addfile = (CheckBox) findViewById(R.id.cb2_addfile);
        cb_adddeadline = (CheckBox) findViewById(R.id.cb3_deadline);
        spinner_deadline = (Spinner) findViewById(R.id.spinner);
        spinner_deadline.setVisibility(View.GONE);
        cb_proposal = (CheckBox) findViewById(R.id.cb4_proposal);
        expense_type=TYPE_EXPENSE.MANDATORY;
        cb_policy = (CheckBox) findViewById(R.id.cb_policy);
        cb_policy.setVisibility(View.VISIBLE);
        layout_policy = (LinearLayout) findViewById(R.id.layout_policy);
        layout_policy.setVisibility(View.VISIBLE);
        //rel_info= (RelativeLayout) findViewById(R.id.relative_info);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_members);
        card_recycler = (CardView) findViewById(R.id.card_recycler);
        card_recycler.setVisibility(View.GONE);
        //info = (ImageView) findViewById(R.id.InfoButton);
        // rel_info= (RelativeLayout) findViewById(R.id.relative_info);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        iv_group_image.setImageResource(R.drawable.network);
        tv_group_name.setText(Singleton.getInstance().getmCurrentGroup().getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        for(String s : Singleton.getInstance().getmCurrentGroup().getMembers().keySet()){
            if(!(Singleton.getInstance().getmCurrentGroup().getMembers().get(s)instanceof UserDatabase)) return;
            User_expense ue=new User_expense((UserDatabase)Singleton.getInstance().getmCurrentGroup().getMembers().get(s));
            ue.setExpense(expense);
            partecipants.add(ue);
            list.put(ue.getId(),ue);
        }

        final MemberExpandedAdapter memberAdapter = new MemberExpandedAdapter(partecipants, this);
        LinearLayoutManager lin_members = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lin_members);
        recyclerView.setAdapter(memberAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                lin_members.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        final java.sql.Timestamp expense_timestamp = new java.sql.Timestamp(now.getTime());

        expense.setTimestamp(expense_timestamp.toString());
        cb_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_details.isChecked()){
                    cardView.setVisibility(View.VISIBLE);
                }
                else {
                    cardView.setVisibility(View.GONE);
                    if(cb_policy.isChecked()){
                        card_recycler.setVisibility(View.GONE);
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if( expense.getName().toString().length() == 0 /*|| expense.getPrice()==0.0 */) {
                    Snackbar.make(view,"Invalid name",Snackbar.LENGTH_SHORT).show();
                }
                else if(expense.getPrice().toString().length()>6) Snackbar.make(view,"Price on max 6 characters",Snackbar.LENGTH_SHORT).show();
                else {
                    fdb =   FirebaseDatabase.getInstance()
                            .getReference("expenses")
                            .child(Singleton.getInstance().getmCurrentGroup().getId())
                            .push();
                    DatabaseReference fdbgroup = FirebaseDatabase.getInstance().getReference("groups").child(Singleton.getInstance().getmCurrentGroup().getId())
                            .child("lmTime");
                    expense.setId(fdb.getKey());
                    expense.setOwner(Singleton.getInstance().getCurrentUser().getId());
                    Double x;
                    boolean isOwner = false;
                    if(!expense.isMandatory()){
                        for(String s : Singleton.getInstance().getmCurrentGroup().getMembers().keySet()){
                            expense.getMembers().put(s, 0.0);
                        }
                    }
                    else {
                        Map<String,Double> map = new HashMap<>();


                        for (String s : expense.getMembers().keySet()) {
                            x = expense.getMembers().get(s);
                            if (s == expense.getOwner())
                                expense.getMembers().put(s, expense_price - x);
                             else
                                expense.getMembers().put(s, (-1.00)*x);
                            DB_Manager.getInstance().updateGroupFlow(s,-1.00*expense.getMembers().get(s));
                        }
                        if(!expense.getMembers().containsKey(expense.getOwner()))
                        {
                            expense.getMembers().put(expense.getOwner(), expense_price);
                            DB_Manager.getInstance().updateGroupFlow(Singleton.getInstance().getCurrentUser().getId(),(-1.00)*expense_price);
                        }

                    if(nameFILE != null){
                        expense.setFile(nameFILE);
                        upLoadFile(uri);
                      /* try{
                        DB_Manager.getInstance().setContext(context).fileUpload(expense.getId(), nameFILE, uri.toString());
                       } catch (IOException e) {
                           e.printStackTrace();
                       }*/
                    }


                    }

                    fdbgroup.setValue(expense.getTimestamp());
                    fdb.setValue(expense);
                    finish();
                }
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                expense.setName(et_name.getText().toString());
                expense_name=et_name.getText().toString();

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  expense.setPrice(9.00);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s != null) {
                    try {
                        expense.setPrice(Double.parseDouble(s.toString().replace(',', '.')));
                        expense_price =  Double.parseDouble(s.toString().replace(',', '.'));
                        costPerUser = (expense.getPrice()/(list.size()));
                        for(UserDatabase u : partecipants) {
                     /*       if(u.getId().compareTo(expense.getOwner())==0)
                                expense.getMembers().put(u.getId(), expense.getPrice()-costPerUser);
                            else*/
                            expense.getMembers().put(u.getId(), costPerUser);
                        }
                        DecimalFormat df = new DecimalFormat("0.00");
                        String formate = df.format(costPerUser);
                        double finalValue = Double.parseDouble(formate.replace(',', '.'));
                        //et_cost.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        EventBus.getDefault().post(new PriceChangedEvent(finalValue));
                    } catch (NumberFormatException e) {
                        expense.setPrice(0.0);
                        expense_price = 0.0;
                    }
                }
            }
        });

        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                expense.setDescription(et_description.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinner_deadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense.setDeadline( 12 + 12*position); //Measured in hours
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cb_proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_proposal.isChecked()){
                    expense.setType(1); //; = TYPE_EXPENSE.NOTMANDATORY;
                    cb_policy.setVisibility(View.INVISIBLE);
                    layout_policy.setVisibility(View.GONE);
                    layout_policy.invalidate();
                    card_recycler.setVisibility(View.INVISIBLE);
                }
                else {
                    expense.setType(0); //expense_type = TYPE_EXPENSE.MANDATORY;
                    cb_policy.setVisibility(View.VISIBLE);
                    layout_policy.setVisibility(View.VISIBLE);
                    layout_policy.invalidate();
                }
            }
        });

        cb_addfile.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent,0);
            }
        });
        cb_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_policy.isChecked()){
                    card_recycler.setVisibility(View.VISIBLE);
                    isEqualPart = false;
                }
                else{
                    card_recycler.setVisibility(View.GONE);
                    isEqualPart = true;
                }
            }
        });

    }

    private void upLoadFile(Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://group05-16e97.appspot.com")
                                            .child("expenses")
                                            .child(expense.getId())
                                            .child(nameFILE);

        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Expense_activity.this, "FAIL", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Expense_activity.this, "Uploading Done!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void description_handler(View v){
        et_description.setVisibility(et_description.isShown() ? View.GONE : View.VISIBLE);
    }

    public void deadline_handler(View v){
        spinner_deadline.setVisibility(spinner_deadline.isShown() ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            newFile = true;
            uri =  data.getData();
            if(uri != null){
                //uri.getPath()
                fileUploaded = new File(uri.toString());
                //fileUploaded = new File(String.valueOf(uri));
            }
            if(uri.getScheme().equals("content")){
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        nameFILE = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            }
            if (nameFILE== null) {
                nameFILE= uri.getPath();
                int cut = nameFILE.lastIndexOf('/');
                if (cut != -1) {
                    nameFILE= nameFILE.substring(cut + 1);
                }
            }
        }
    }



}