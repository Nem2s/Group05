package it.polito.group05.group05;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
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
    private MaterialEditText et_name, et_cost;
    private CheckBox cb_addfile, cb_details, cb_policy;
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
    private ExpandableRelativeLayout exp_ll;
    private ImageView plus;
    private TextView nomeFile;
    private int click;

    ////////////////////////////////////////
    private ExpenseDatabase expense;
    private Double expense_price;
    private TYPE_EXPENSE expense_type;
    private String expense_name;
    private UserDatabase expense_owner;
    private GroupDatabase actual_group;
    private Double costPerUser = 0.0;
    private DatabaseReference fdb;
    private List<User_expense> partecipants = new ArrayList<>();
    private Map<String, User_expense> list = new TreeMap<>();
    private Uri uri;
    private boolean newFile = false;
    private String nameFILE= null;
    private MemberExpandedAdapter memberAdapter;
    private File fileUploaded;
    private Context context;
    private UploadTask uploadTask;
    private InputStream stream;
    FirebaseStorage storage;
    final StorageReference fileRef = null;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_expense_v2);
        costPerUser = 0.0;
        expense_price = 0.0;
        click=0;
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
        // cardView = (CardView) findViewById(R.id.card_view2_toshow);
        nomeFile = (TextView) findViewById(R.id.nomeFile);
        cb_addfile = (CheckBox) findViewById(R.id.cb2_addfile);
        expense_type=TYPE_EXPENSE.MANDATORY;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_members);
        recyclerView.setVisibility(View.GONE);
        // card_recycler = (CardView) findViewById(R.id.card_recycler);
       // card_recycler.setVisibility(View.GONE);

        plus = (ImageView) findViewById(R.id.plus);
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

        memberAdapter = new MemberExpandedAdapter(partecipants, this, expense.getPrice());
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
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if( expense.getName().toString().length() == 0 || expense.getPrice()==0.0 ) {
                    Snackbar.make(view,"Invalid name",Snackbar.LENGTH_SHORT).show();
                }
                else if(expense.getPrice().toString().length()>6) Snackbar.make(view,"Price on max 6 characters",Snackbar.LENGTH_SHORT).show();
                else {
                    fdb = FirebaseDatabase.getInstance()
                            .getReference("expenses")
                            .child(Singleton.getInstance().getmCurrentGroup().getId())
                            .push();
                    DatabaseReference fdbgroup = FirebaseDatabase.getInstance().getReference("groups").child(Singleton.getInstance().getmCurrentGroup().getId())
                            .child("lmTime");
                    expense.setId(fdb.getKey());
                    expense.setOwner(Singleton.getInstance().getCurrentUser().getId());
                    if (nameFILE != null) {
                        expense.setFile(nameFILE);
                        upLoadFile(uri);
                    }
                        double price;
                        double toSubtractOwner = 0.0;
                        for (int i = 0; i < partecipants.size(); i++) {
                            if (partecipants.get(i).getId() != expense.getOwner()) {
                                toSubtractOwner += partecipants.get(i).getCustomValue();
                            }
                        }
                        for (int i = 0; i < partecipants.size(); i++) {
                            price = partecipants.get(i).getCustomValue();
                            if (partecipants.get(i).getId() == expense.getOwner()) {
                                expense.getMembers().put(partecipants.get(i).getId(), toSubtractOwner);
                            } else {
                                expense.getMembers().put(partecipants.get(i).getId(), (-1.00) * price);
                            }
                        }
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            if(s.length()>0){
                expense.setPrice(Double.parseDouble(s.toString().replace(',', '.')));
                expense_price = Double.parseDouble(s.toString().replace(',', '.'));
                memberAdapter.changeTotal(expense_price);
                memberAdapter.notifyDataSetChanged();
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
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(recyclerView.getVisibility() == View.GONE){
                  recyclerView.setVisibility(View.VISIBLE);
                  plus.setImageResource(R.drawable.ic_expand_less);
              }else {
                  recyclerView.setVisibility(View.GONE);
                  plus.setImageResource(R.drawable.ic_expand_more);
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
                        nomeFile.setText(nameFILE);

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