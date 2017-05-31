package it.polito.group05.group05;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.AestheticActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.CustomDialogFragment;
import it.polito.group05.group05.Utility.CustomIncludedDialog;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;


public class Expense_activity extends AestheticActivity {

    private CoordinatorLayout parent;
    private RelativeLayout rel_file, moreLayout;
    private EditText et_name, et_cost;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private CircleImageView iv_group_image;
    private TextView tv_group_name;
    private FloatingActionButton fab;
    private ImageView image_network;
    private ImageView calendar1, graffetta;
    private TextView nomeFile, veroNF;
    private Button buttonUPLOAD;
    private String data = null;
    private String time = null;
    private String tmsp = null;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private boolean clicked_calendar = false;

    ////////////////////////////////////////
    private ExpenseDatabase expense;
    private Double expense_price, totalPriceActual;
    private String expense_name;
    private long timestamp;
    private DatabaseReference fdb;
    private List<User_expense> partecipants = new ArrayList<>();
    private Uri uri;
    private boolean newFile = false;
    private String nameFILE= null;
    private File fileUploaded;
    private Context context;
    private CustomIncludedDialog cid;
    private boolean clickedDetails;

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
        expense_price = 0.0;
        expense= new ExpenseDatabase();
        expense.setPrice(0.0);
        timestamp = 0;
        clickedDetails = false;

        expense.setOwner(Singleton.getInstance().getCurrentUser().getId());
        parent = (CoordinatorLayout)findViewById(R.id.parent_layout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        iv_group_image= (CircleImageView) findViewById(R.id.iv_group_image);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        image_network = (ImageView) findViewById(R.id.image_network);
        et_name = (EditText) findViewById(R.id.et_name_expense);
        et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_name.setSingleLine();
        et_cost = (EditText) findViewById(R.id.et_cost_expense);
        et_cost.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_cost.setSingleLine();
        veroNF = (TextView) findViewById(R.id.nome_file);
        nomeFile = (TextView) findViewById(R.id.tv_name_fil);
        buttonUPLOAD =(Button) findViewById(R.id.button_upload);

        calendar1 = (ImageView) findViewById(R.id.calendar);

        fab = (FloatingActionButton) findViewById(R.id.fab_id);
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
        }



        Calendar calendar = Calendar.getInstance();
        final java.util.Date now = calendar.getTime();
        timestamp = now.getTime();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expense.getName().toString().length() == 0 || expense.getPrice() == 0.0) {
                    Snackbar.make(v, "Set a valid Description", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if (expense.getPrice().toString().length() > 6)
                        Snackbar.make(v, "Price on max 6 characters", Snackbar.LENGTH_SHORT).show();
                    else {
                        if (clicked_calendar) {
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            Date date = null;
                            try {
                                date = dateFormat.parse(data);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long timeLong = date.getTime();
                            expense.setTimestamp(timeLong);
                            clicked_calendar = false;
                        } else {
                            expense.setTimestamp(timestamp);
                        }

                        for(User_expense e : partecipants){
                            e.setExcluded(false);
                        }
                        final FragmentManager fm = getFragmentManager();
                        cid = new CustomIncludedDialog(partecipants, expense, uri);
                        cid.show(fm, "TV_tag");
                    }
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
                if (s.length() > 0) {
                    expense.setPrice(Double.parseDouble(s.toString().replace(',', '.')));
                    expense_price = Double.parseDouble(s.toString().replace(',', '.'));
                }
            }
        });

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_calendar = true;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                int month1 = month + 1;
                                if (month < 10) {
                                    String mese = "0" + (month1);
                                    data = dayOfMonth + "/" + mese + "/" + year + " " + mHour + ":" + mMinute;
                           //         nomedata.setText(dayOfMonth + "/" + mese + "/" + year);
                                } else {
                                    data = dayOfMonth + "/" + month1 + "/" + year + " " + mHour + ":" + mMinute;
                            //        nomedata.setText(dayOfMonth + "/" + month1 + "/" + year);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        buttonUPLOAD.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                if (!newFile) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                    startActivityForResult(intent,0);
                }else{
                    veroNF.setText("FileName");
                    buttonUPLOAD.setText("UPLOAD");
                    newFile = false;
                    // upLoadFile(uri);
                }
            }
        });

   /*     moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedDetails = true;
                cdf.show(fm,"TV_tag");
            }
        });
*/

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
                fileUploaded = new File(uri.toString());
                //fileUploaded = new File(String.valueOf(uri));
            }
            if(uri.getScheme().equals("content")){
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        nameFILE = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        if(nameFILE != null){
                            veroNF.setText(nameFILE);
                            buttonUPLOAD.setText("DELETE");
                            expense.setFile(nameFILE);
                        }

                      /*  if (nameFILE != null) {
                            expense.setFile(nameFILE);
                            upLoadFile(uri);
                       } */
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