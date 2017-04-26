package it.polito.group05.group05.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDataLoadProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pkmmte.view.CircularImageView;

import junit.framework.TestResult;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.polito.group05.group05.HomeScreen;
import it.polito.group05.group05.Init;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Group1;
import it.polito.group05.group05.Utility.BaseClasses.GroupColor;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.EventClasses.CurrentUserChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.GroupAddedEvent;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static it.polito.group05.group05.Utility.ColorUtils.context;

/**
 * Created by andre on 08-Apr-17.
 */

public class DB_Manager {

    private static DB_Manager mInstance = null;

    private static FirebaseAuth mAuth;
    public static String currentUserID;
    private static List<String> groupUser = new ArrayList<>();
    private static FirebaseDatabase database;
    private static DatabaseReference userRef;
    private static DatabaseReference groupRef;
    private static DatabaseReference expenseRef;
    private static DatabaseReference usernumberRef;
    private static FirebaseStorage storage;
    private static StorageReference storageGroupRef;
    private static StorageReference storageUserRef;
    private static StorageReference storageExpenseRef;

    private static ValueEventListener groupListener;
    private static ChildEventListener userListener;

    public DB_Manager() {
    }

    public static void signOut(){

        currentUserID = null;
        Singleton.getInstance().clearGroups();
//        DB_Manager.getInstance().removeuserlistener();

        mAuth.signOut();
    }

    public static  void testuser(){
        while (currentUserID==null);
    }

    public static DB_Manager getInstance(){
        if(mInstance == null)
        {
            mInstance = new DB_Manager();
        }
        return mInstance;
    }

    public static void removeInstance(){
        mInstance = null;
        return;
    }


    public static FirebaseDatabase getDatabase() {


        if (database == null) {

            //Realtime Database Init

            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            userRef = database.getReference("users");
            userRef.keepSynced(true);
            usernumberRef = database.getReference("usersNumber");
            usernumberRef.keepSynced(true);
            groupRef = database.getReference("groups");
            groupRef.keepSynced(true);
            //expenseRef = database.getReference("expense");
            //expenseRef.keepSynced(true);

            //Storege Init
            storage = FirebaseStorage.getInstance();
            storageGroupRef = storage.getReference("groups");
            storageUserRef = storage.getReference("users");
            storageExpenseRef = storage.getReference("expenses");

            //Authentication Init
            mAuth = FirebaseAuth.getInstance();

        }
        if(currentUserID == null) {
            Singleton.getInstance().clearGroups();
            HomeScreen.currentUser = new User();
            getCurrentUserID(HomeScreen.currentUser);
        }
        else {
            currentUserInfo(HomeScreen.currentUser);
        }

        return database;
    }



    public static void PushGroupToDB(Group G){
        DatabaseReference ref = groupRef.push();
        GroupDatabase grouptopush = new GroupDatabase(ref.getKey(), G.getName(), G.getBalance(), G.getLmTime(), new GroupColor(1,2,3));
        grouptopush.setMembers(G.getMembers(), currentUserID);
        for(String s : grouptopush.getMembers().keySet()){
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put(grouptopush.getId(), true);
            userRef.child(s).child("userGroups").updateChildren(temp);
        }
        photoMemoryUpload(2, grouptopush.getId(), G.getGroupProfile());
        grouptopush.setPictureUrl(storageGroupRef +"/"+ grouptopush.getId() +"/grouprofile.png");
        ref.setValue(grouptopush);
        G.setGroupID(ref.getKey());
        return;
    }

    /*
    *   Listener sul singolo gruppo per tenerlo aggirnato
     */

    public static Group MonitorOnGroup(Group G){
        final Group g1 = G;
        DatabaseReference ref = database.getReference("groups");
        groupListener = ref.orderByKey()
                 .equalTo(G.getGroupID())
                 .limitToFirst(1)
                 .addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for(DataSnapshot child : dataSnapshot.getChildren()) {
                             GroupDatabase g = child.getValue(GroupDatabase.class);
                             g1.setName(g.getName());
                             g1.setBalance(g.getBalance());
                             g1.setLmTime(g.getLmTime());
                             g1.setGroupColor(g.getGroupColor());
                             g1.setBadge(1);
                             try {
                                 photoGroupDownload(g1);
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             //fare aggiunta di un membroal gruppo
                             for(String s : g.getMembers().keySet())
                             {
                                 g1.addMember(s);
                             }
                             EventBus.getDefault().post(new GroupAddedEvent());

                         }
                     }
                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                 });
        return g1;
    }

    public static void removegrouplistener(){
        database.getReference("groups").removeEventListener(groupListener);
    }
    public static void removeuserlistener(){
        userRef.child(currentUserID).child("userGroups").removeEventListener(userListener);
    }

    /*
     *   Listener singolo per Inizializzazione utenti dentro il gruppo
     */

    public static void retriveUsersInfo(Group G, final ArrayList<User> users){
        for (String s : G.getMembersId()) {
            userRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserDatabase ud = dataSnapshot.getValue(UserDatabase.class);
                    User u;
                    if(ud.getId().equals(HomeScreen.currentUser.getId())) {
                        u = new User(ud.getId(), ud.getName(), new Balance(3, 2), null, true, true);
                    }
                    else{
                        u = new User(ud.getId(), ud.getName(), new Balance(3, 2), null, true, false);
                    }
                    try {
                        photoUserDownload(u);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    users.add(u);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /*
     *   Listener singolo per Inizializzazione gruppi
     */

  /*  public static void PullGroupFromDBWithUserId(){

        for(String s : groupUser){
            final Group g;
            groupRef.orderByKey()
                    .equalTo(s)
                    .limitToFirst(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()) {
                                GroupDatabase gd = child.getValue(GroupDatabase.class);
                                Group g = new Group(gd.getName(), gd.getId(), gd.getBalance(), gd.getLmTime(), 1);
                                try {
                                    photoMemoryDownload(gd.getId(), g);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Singleton.getInstance().addGroup(g);
                                MonitorOnGroup(g);
                                HomeScreen.groupAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }

    }*/


    public static void pushExpensetoDB(Expense e){

    }

    /*
    *   Listener singolo per ricavare id e gruppi utente
    */

    public static void getCurrentUserID(final User U){
        userRef.orderByChild("authKey")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    UserDatabase u = child.getValue(UserDatabase.class);
                    U.setId(u.getId());
                    U.setUser_name(u.getName());
                    U.setEmail(u.getEmail());
                    U.setBalance(new Balance(3, 1));
                    try {
                        photoUserDownload(U);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    U.setUser_group(null);
                    U.setAdministrator(true);
                    U.setCardEnabled(true);
                    currentUserID = u.getId();
                    groupUser = u.UserGruopsList();
                   // Singleton.getInstance().clearGroups();
                   // DB_Manager.getInstance().PullGroupFromDBWithUserId();
                    DB_Manager.getInstance().CurrentUserGroupMonitor();
                    EventBus.getDefault().post(new CurrentUserChangedEvent(HomeScreen.currentUser));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*
    * Listeners che rimangono attivi per monitorare userGroups all'interno dell'utente
    */

    private void CurrentUserGroupMonitor() {
        //Singleton.getInstance().clearGroups();
        userListener = userRef
            .child(currentUserID)
            .child("userGroups")
            .orderByKey()
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String gId = dataSnapshot.getKey();
                    DatabaseReference ref = database.getReference("groups");
                    ref.orderByKey()
                            .equalTo(gId)
                            .limitToFirst(1)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                                        GroupDatabase gd = child.getValue(GroupDatabase.class);
                                        Group g = new Group(gd.getName(), gd.getId(), gd.getBalance(), gd.getLmTime(), 1);
                                        try {
                                            photoGroupDownload(g);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Singleton.getInstance().addGroup(g);
                                        MonitorOnGroup(g);
                                        EventBus.getDefault().post(new GroupAddedEvent());
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                  /*  for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String gId = child.getKey();
                        Group g = new Group();
                        g.setGroupID(gId);
                    try {
                        photoMemoryDownload(g.getGroupID(), g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        Singleton.getInstance().addGroup(g);
                        MonitorOnGroup(g);
                        HomeScreen.groupAdapter.notifyDataSetChanged();
                    }*/

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    String gId = dataSnapshot.getKey();
                    int i = Singleton.getInstance().getPositionGroup(gId);
                    Singleton.getInstance().getmCurrentGroups().remove(i);
                    EventBus.getDefault().post(new GroupAddedEvent());
                    //groupRef.child(gId).removeEventListener(null);

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }


    public static void pushNewUser(String email, String name, String cID)
    {
        DatabaseReference ref = userRef.push();
        currentUserID = ref.getKey();
        UserDatabase newuser = new UserDatabase(ref.getKey(), name, cID, "380xxxxxx2", email);

        Map<String, Object> memb = new HashMap<String, Object>();
        memb.put("00", true);
        newuser.setUserGroups(memb);
        ref.setValue(newuser);
        usernumberRef.child(newuser.getTelNumber()).setValue(newuser.getAuthKey());
        DB_Manager.getInstance().photoMemoryUpload(1, newuser.getId(), BitmapFactory.decodeResource(Init.getAppContext().getResources(), R.drawable.man_1));
        DB_Manager.getInstance().CurrentUserGroupMonitor();
    }

    public static void currentUserInfo(final User U)
    {
        if(currentUserID != null) {
            userRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserDatabase ud = dataSnapshot.getValue(UserDatabase.class);
                    U.setId(ud.getId());
                    U.setUser_name(ud.getName());
                    U.setEmail(ud.getEmail());
                    U.setBalance(new Balance(3, 1));
                    try {
                        photoUserDownload(U);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    U.setUser_group(null);
                    U.setAdministrator(true);
                    U.setCardEnabled(true);
                    EventBus.getDefault().post(new CurrentUserChangedEvent(HomeScreen.currentUser));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /*
    type:
    1: user profile
    2: group profile
    3: expense profile
    4: expense attached

     */

    public static void photoMemoryUpload(int type, String Id, Bitmap bitmap)
    {
        StorageReference ref;
        String name;
        switch(type) {
            case (1):
                ref = storageUserRef;
                name = new String("userprofile.jpg");
                break;
            case (2):
                ref = storageGroupRef;
                name = new String("grouprofile.jpg");
                break;
            case (3):
                ref = storageExpenseRef;
                name = new String("expenseprofile.jpg");
                break;
            case (4):
                ref = storageExpenseRef;
                name = new String("expenseattached.jpg");
                break;
            default:
                return;
        }

        final File localdir = new File(Init.getAppContext().getFilesDir(), Id);

        if(!localdir.exists())
            localdir.mkdir();

        final File localFile = new File(Init.getAppContext().getFilesDir(), Id + "/" + name);
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

    public static void photoGroupDownload(final Group G) throws IOException {

        final File localdir = new File(Init.getAppContext().getFilesDir(), G.getGroupID());

        if(!localdir.exists())
            localdir.mkdir();

        final File localFile = new File(Init.getAppContext().getFilesDir(), G.getGroupID() + "/grouprofile.jpg");
        if(localFile.exists()) {
             G.setGroupProfile(BitmapFactory.decodeStream(new FileInputStream(localFile)));
            EventBus.getDefault().post(new GroupAddedEvent());
        }
        //final File localFile = File.createTempFile("images", "jpg");

            StorageReference islandRef = storageGroupRef.child(G.getGroupID()).child("grouprofile.jpg");

            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        G.setGroupProfile(BitmapFactory.decodeStream(new FileInputStream(localFile)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(new GroupAddedEvent());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    public static void photoUserDownload(final User U) throws IOException {

       final File localdir = new File(Init.getAppContext().getFilesDir(), U.getId());

        if(!localdir.exists())
            localdir.mkdir();

        final File localFile = new File(Init.getAppContext().getFilesDir(), U.getId() + "/userprofile.jpg");
        if(localFile.exists()) {
            U.setProfile_image(BitmapFactory.decodeStream(new FileInputStream(localFile)));
        }
        //final File localFile = File.createTempFile("images", "jpg");

        StorageReference islandRef = storageUserRef.child(U.getId()).child("userprofile.jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    U.setProfile_image(BitmapFactory.decodeStream(new FileInputStream(localFile)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
