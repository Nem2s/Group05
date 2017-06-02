package it.polito.group05.group05.Utility.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.CurrentUser;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.HistoryClass;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Event.CurrentUserReadyEvent;
import it.polito.group05.group05.Utility.Event.ExpenseCountEvent;
import it.polito.group05.group05.Utility.Event.GroupInfoChartEvent;
import it.polito.group05.group05.Utility.Event.LeaveGroupEvent;
import it.polito.group05.group05.Utility.Event.NewUserEvent;

import static android.widget.Toast.LENGTH_SHORT;


/**
 * Created by andre on 05-May-17.
 */

public class DB_Manager {

    private static DB_Manager mInstance = null;
    private Context context;
    private FirebaseAuth mAuth;

    public String currentUserID;
    private List<String> groupUser = new ArrayList<>();

    private static FirebaseDatabase database;

    private DatabaseReference userRef,
            groupRef,
            expenseRef,
            usernumberRef,
            inviteRef;

    private FirebaseStorage storage;
    private StorageReference storageGroupRef,
            storageUserRef,
            storageExpenseRef;

    private String userInfo = new String("userInfo");
    private String userGroups = new String("userGroups");

    private File localFile;
    FileOutputStream outputStream;
    BufferedOutputStream buff;

    private DB_Manager() {

        //Realtime Database Init
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        userRef = database.getReference("users");
        userRef.keepSynced(true);
        usernumberRef = database.getReference("usersNumber");
        usernumberRef.keepSynced(true);
        groupRef = database.getReference("groups");
        groupRef.keepSynced(true);
        expenseRef = database.getReference("expenses");
        expenseRef.keepSynced(true);
        inviteRef = database.getReference("invites");
        inviteRef.keepSynced(true);

        //Storege Init\
        storage = FirebaseStorage.getInstance();
        storageGroupRef = storage.getReference("groups");
        storageUserRef = storage.getReference("users");
        storageExpenseRef = storage.getReference("expenses");

        //Authentication Init
        mAuth = FirebaseAuth.getInstance();

    }

   /* public  void signOut(){
        currentUserID = null;
        Singleton.getInstance().clearGroups();
        mAuth.signOut();
    }*/

    public static DB_Manager getInstance() {
        if (mInstance == null) {
            mInstance = new DB_Manager();

        }
        return mInstance;
    }

    public DB_Manager setContext(Context context) {
        this.context = context;
        return mInstance;
    }

    public DatabaseReference getExpensesRef() {
        return expenseRef;
    }


   /* public  void signOut(){
        currentUserID = null;
        Singleton.getInstance().clearGroups();
        mAuth.signOut();
    }*/

 /*   public void checkContacts() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!data.exists()) continue;
                    UserDatabase user = (UserDatabase) data.child("userInfo").getValue(UserDatabase.class);
                    Map<String, UserContact> lmap = Singleton.getInstance().getLocalContactsList();
                    Map<String, UserContact> rmap = Singleton.getInstance().getRegContactsList();
                    if(!rmap.containsKey(user.getTelNumber()))
                        Singleton.getInstance().removeRegContact(user);
                    if (lmap.containsKey(user.getTelNumber()))
                        Singleton.getInstance().addRegContact(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/

    public void checkContacts() {
        Map<String, UserContact> lmap = Singleton.getInstance().getLocalContactsList();
        for (String number : lmap.keySet()) {
            if (number.contains("\\.") || number.contains("#")) continue;
            usernumberRef.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userid = dataSnapshot.getValue(String.class);
                    if (userid == null) return;
                    userRef.child(userid).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) return;
                            UserDatabase user = dataSnapshot.getValue(UserDatabase.class);
                            Singleton.getInstance().addRegContact(user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public void retriveExpenses() {
        final List<DataSnapshot> snapshots = new ArrayList<>();
        expenseRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren())
                    snapshots.add(data);
                setupEntries(snapshots);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void setupEntries(List<DataSnapshot> snapshots) {
        final Map<Long, Entry> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        for (DataSnapshot ex_data : snapshots) {
            for (DataSnapshot data : ex_data.getChildren()) {
                ExpenseDatabase e = data.getValue(ExpenseDatabase.class);
                if (!(e.getOwner().equals(Singleton.getInstance().getCurrentUser().getId())))
                    continue;
                long t = 0;
                //try {
                t = e.getTimestamp();
                // t = format.parse(e.getTimestamp()).getTime();
                //} catch (ParseException e1) {
                //   e1.printStackTrace();
                //}
                Calendar today = Calendar.getInstance();
                Calendar sixMonthsAhead = Calendar.getInstance();
                sixMonthsAhead.add(Calendar.MONTH, 6);
                long differenceInMilis = sixMonthsAhead.getTimeInMillis() - today.getTimeInMillis();
                long difference = today.getTimeInMillis() - t;
                if (difference < differenceInMilis) //older than 6 months
                    t = TimeUnit.MILLISECONDS.toDays(t);
                else
                    continue;

                int i = 0;
                if (map.containsKey(t)) {
                    map.get(t).setY(map.get(t).getY() + 1);
                } else {
                    Entry entry = new Entry(t, 1);
                    entry.setData(e);
                    map.put(t, entry);
                }


            }
        }
        EventBus.getDefault().postSticky(new ExpenseCountEvent(new ArrayList<Entry>(map.values())));
    }


    public void retriveGroups() {
        final List<String> groupsId = new ArrayList<>();
        final Map<Long, Integer> expenses = new HashMap<>();
        userRef.child(Singleton.getInstance().getCurrentUser().getId()).child("userGroups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!data.getKey().equals("00"))
                        groupsId.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }

        });

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PieEntry> list = new ArrayList<PieEntry>(0);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (groupsId.contains(data.getKey())) {
                        GroupDatabase g = (GroupDatabase) data.getValue(GroupDatabase.class);
                        float value = Float.valueOf(g.getMembers().get(Singleton.getInstance().getCurrentUser().getId()).toString());
                        if (value != 0) {
                            if (value < 0)
                                value = -value;
                            final PieEntry entry = new PieEntry(value, g.getName());
                            entry.setData(g);
                            list.add(entry);
                        }

                    }

                }
                EventBus.getDefault().postSticky(new GroupInfoChartEvent(list));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    public String pushNewGroup(GroupDatabase groupDatabase, Bitmap bitmap) {
        DatabaseReference ref = groupRef.push();
        groupDatabase.setId(ref.getKey());
        String uuid = UUID.randomUUID().toString();
        groupDatabase.setPictureUrl(uuid);
        imageProfileUpload(2, groupDatabase.getId(), uuid, bitmap);
        ref.setValue(groupDatabase);
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put(groupDatabase.getId(), true);
        for (String s : groupDatabase.getMembers().keySet()) {
            if (s == null) continue;
            userRef.child(s).child(userGroups).updateChildren(temp);
        }

        newhistory(groupDatabase.getId(), groupDatabase);
        return groupDatabase.getId();
    }

    public void pushNewExpense(ExpenseDatabase expenseDatabase) {
        DatabaseReference ref = expenseRef.push();
        expenseDatabase.setId(ref.getKey());
        ref.setValue(expenseDatabase);
    }

    public void imageProfileUpload(int type, String Id, String name, Bitmap bitmap) {

        StorageReference ref;
        switch (type) {
            case (1):
                ref = storageUserRef;
                break;
            case (2):
                ref = storageGroupRef;
                break;
            case (3):
                ref = storageExpenseRef;
                break;
            default:
                return;
        }

        final File localdir = new File(context.getFilesDir(), Id);

        if (!localdir.exists())
            localdir.mkdir();

        final File localFile = new File(context.getFilesDir(), Id + "/" + name);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.child(Id).child(name).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

    }

    public void fileDownload(String expenseID, String nomeFile) throws FileNotFoundException {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://group05-16e97.appspot.com")
                .child("expenses")
                .child(expenseID)
                .child(nomeFile);

        File folder = new File(Environment.getExternalStorageDirectory() + "/FileAppPoli");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File filelocal = new File(folder, nomeFile);
        storageRef.getFile(filelocal).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Download failed. Try again!", LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "File downloaded", LENGTH_SHORT).show();
            }
        });
    }

    public void updateGroupFlow(String s, final Double d) {

        updateGroupFlow(Singleton.getInstance().getmCurrentGroup().getId(), s, d);
    }

    public void updateGroupFlow(String gid, String s, final Double d) {
        final DatabaseReference fdb = FirebaseDatabase.getInstance().getReference("groups").child(gid).child("members").child(s);

        fdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) return;
                Double tmp = Double.parseDouble(dataSnapshot.getValue().toString());

                tmp = tmp + ((-1.00) * d);
                fdb.setValue(tmp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateGroupFlow(final Map<String, Double> map) {

        final DatabaseReference fdb = FirebaseDatabase.getInstance().getReference("groups").child(Singleton.getInstance().getmCurrentGroup().getId()).child("members");
        fdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;
                for (String s : map.keySet()) {
                    if (dataSnapshot.hasChild(s)) {
                        Double tmp = Double.parseDouble(dataSnapshot.child(s).getValue().toString());
                        tmp -= map.get(s);
                        fdb.child(s).setValue(tmp);

                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public boolean checkUserDebtRemoving() {
        DatabaseReference dbref = groupRef.child(Singleton.getInstance().getmCurrentGroup().getId()).child("members").child(Singleton.getInstance().getCurrentUser().getId());
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;

                EventBus.getDefault().post(new LeaveGroupEvent(Double.parseDouble(dataSnapshot.getValue().toString())));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return false;
    }

    public void removeUserFromGroup(String userId, String groupId) {
        userRef.child(userId).child("userGroups").child(groupId).removeValue();
        groupRef.child(groupId).child("members").child(userId).removeValue();
    }

    public void addUserToGroup(String userId, String groupId) {

        userRef.child(userId).child("userGroups").child(groupId).setValue(false);
        groupRef.child(groupId).child("members").child(userId).setValue(0.0);
    }

    public void getCurrentUser() {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        userRef.orderByChild("email")
                .equalTo(mAuth.getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()) {
                            EventBus.getDefault().post(new NewUserEvent());
                            return;
                        }
                        CurrentUser currentUser = new CurrentUser();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            for (DataSnapshot child2 : child.getChildren()) {
                                if (child2.getKey().equals(userInfo)) {
                                    UserDatabase ud = child2.getValue(UserDatabase.class);
                                    currentUser.settingInfoUser(ud);

                                } else if (child2.getKey().equals(userGroups)) {
                                    Map<String, Object> tmp = (Map<String, Object>) child2.getValue();
                                    tmp.remove("00");
                                    currentUser.setGroups(new ArrayList<>(tmp.keySet()));
                                }
                            }
                        }
                        Singleton.getInstance().setCurrentUser(currentUser);
                        EventBus.getDefault().post(new CurrentUserReadyEvent());
                        userRef.child(Singleton.getInstance().getCurrentUser().getId()).child("fcmToken").setValue(refreshedToken);
                        /*DOWNLOAD DELL'IMMAGINE????*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public void pushNewUser(CurrentUser currentUser) {
        UserDatabase userDatabase = new UserDatabase((UserDatabase) currentUser);

        DatabaseReference ref = userRef.push();
        //currentUserID = ref.getKey();
        userDatabase.setId(ref.getKey());
        currentUser.setId(ref.getKey());
        String uuid = UUID.randomUUID().toString();
        userDatabase.setiProfile(uuid);
        currentUser.setiProfile(uuid);

        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("00", true);
        currentUser.setGroups(new ArrayList<String>());
        ref.child(userInfo).setValue(userDatabase);
        ref.child(userGroups).setValue(tmp);
        tmp.clear();
        tmp.put("email", userDatabase.getEmail());
        ref.updateChildren(tmp);
        if (currentUser.getTelNumber().startsWith("+"))
            currentUser.setTelNumber(currentUser.getTelNumber().substring(3));
        usernumberRef.child(currentUser.getTelNumber()).setValue(currentUser.getId());

        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        userRef.child(Singleton.getInstance().getCurrentUser().getId()).child("fcmToken").setValue(refreshedToken);

        if (currentUser.getImg_profile() == null)
            currentUser.setImg_profile(BitmapFactory.decodeResource(context.getResources(), R.drawable.user_placeholder));

        imageProfileUpload(1, userDatabase.getId(), uuid, currentUser.getImg_profile());
        Singleton.getInstance().setCurrentUser(currentUser);
    }

    public void newhistory(String GroupID, Object o) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("history/" + GroupID).push();

        HistoryClass h;
        String data = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY);
        Date date = new Date();
        if (o instanceof ExpenseDatabase) {
            ExpenseDatabase e = (ExpenseDatabase) o;
            ref = FirebaseDatabase.getInstance().getReference("history/" + GroupID).child(e.getId());
            h = new HistoryClass(
                    Singleton.getInstance().getCurrentUser().getName(),
                    "added " + e.getName() + " of " + e.getPrice().toString() + "€",
                    e.getTimestamp(),
                    0);
            groupRef.child(Singleton.getInstance().getmCurrentGroup().getId()).child("lmTime").setValue(date.getTime());
        } else if (o instanceof GroupDatabase) {
            GroupDatabase g = (GroupDatabase) o;
            h = new HistoryClass(
                    Singleton.getInstance().getCurrentUser().getName(),
                    "created " + g.getName(),
                    date.getTime(),
                    1);
            groupRef.child(g.getId()).child("lmTime").setValue(date.getTime());
        } else if (o instanceof UserDatabase) {
            UserDatabase u = (UserDatabase) o;
            h = new HistoryClass(
                    Singleton.getInstance().getCurrentUser().getName(),
                    "added " + u.getName(),
                    date.getTime(),
                    2);
            groupRef.child(Singleton.getInstance().getmCurrentGroup().getId()).child("lmTime").setValue(date.getTime());
        } else return;

        ref.setValue(h);
    }

    public void notifyPayment(String gid, String eid, String id) {
        FirebaseDatabase.getInstance().getReference("history").child(gid).child(eid).child("notify").child(id).setValue(true);

    }

    public void expensesPayment(String userId, String groupID, List<ExpenseDatabase> expensePayed) {
        for (ExpenseDatabase e : expensePayed) {
            //payDone(groupID, e.getId(), userId, e.getMembers().get(userId));
            notifyPayment(groupID, e.getId(), userId);
        }
    }


    public void payDone(String gid, String eid, String id, double debit) {
        payDone(gid, eid, id);
        updateGroupFlow(gid, id, debit);
        updateGroupFlow(gid, Singleton.getInstance().getCurrentUser().getId(), (-1.00) * debit);


    }

    public void payDone(String gid, String eid, String id) {
        FirebaseDatabase.getInstance().getReference("history").child(gid).child(eid).child("payment").child(id).setValue(true);
        expenseRef.child(gid).child(eid).child("payed").child(id).setValue(true);
        FirebaseDatabase.getInstance().getReference("history").child(gid).child(eid).child("notify").child(id).removeValue();
    }

    public void payUnDone(String gid, String eid, String id) {
        expenseRef.child(gid).child(eid).child("payed").child(id).setValue(false);
        FirebaseDatabase.getInstance().getReference("history").child(gid).child(eid).child("payment").child(id).setValue(false);
    }

    public void reminderToAll(String gid, String expenseId, String s) {
        FirebaseDatabase.getInstance().getReference("history").child(gid).child(expenseId).child("notifyToAll").child(s).setValue(true);
    }

    public void reminderTo(String gid, String s, String uid, Double d) {
        FirebaseDatabase.getInstance().getReference("history").child(gid).child("notifyTo").child(s).child(uid).setValue(d);
    }

    public void leaveGroup(final UserDatabase user, final Context context) {
        FirebaseDatabase.getInstance().getReference("expenses")
                .child(Singleton.getInstance().getmCurrentGroup().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double value = 0;
                        int n = 0;
                        MaterialDialog.Builder builder;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            ExpenseDatabase e = data.getValue(ExpenseDatabase.class);
                            if (e.getMembers().containsKey(user.getId())) {
                                n++;
                                value += e.getMembers().get(user.getId());
                            }
                        }
                        if (n > 0 && value != 0) {
                            builder =
                                    new MaterialDialog.Builder(context)
                                            .title("Error")
                                            .content("You cannot leave the group.\n" +
                                                    "You have " + n + " pending " + (n > 1 ? "Expenses " : "Expense ") +
                                                    "for a total of " + (value < 0 ? -value + " €'s debit" : value + " €'s credit"))
                                            .negativeText("Cancel")
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            }).canceledOnTouchOutside(false);


                        } else {
                            builder =
                                    new MaterialDialog.Builder(context)
                                            .title("Leaving Group")
                                            .content("Are you sure?")
                                            .negativeText("Cancel")
                                            .positiveColor(context.getResources().getColor(R.color.colorSecondaryText))
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    FirebaseDatabase.getInstance().getReference("expenses")
                                                            .child(Singleton.getInstance().getmCurrentGroup().getId())
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                        ExpenseDatabase e = data.getValue(ExpenseDatabase.class);
                                                                        if (e.getMembers().containsKey(user.getId())) {
                                                                            data.child("members").child(user.getId()).getRef().removeValue();
                                                                        }
                                                                    }
                                                                    ((Activity) context).onBackPressed();
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                    FirebaseDatabase.getInstance().getReference("groups")
                                                            .child(Singleton.getInstance().getmCurrentGroup().getId())
                                                            .child("members")
                                                            .child(user.getId())
                                                            .removeValue();
                                                    FirebaseDatabase.getInstance().getReference("users")
                                                            .child(user.getId())
                                                            .child("userGroups")
                                                            .child(Singleton.getInstance().getmCurrentGroup().getId())
                                                            .removeValue();

                                                }
                                            })
                                            .positiveText("Yes");
                        }
                        builder.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}