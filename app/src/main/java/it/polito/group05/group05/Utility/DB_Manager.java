package it.polito.group05.group05.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
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

import junit.framework.TestResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Group1;
import it.polito.group05.group05.Utility.BaseClasses.GroupColor;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

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
    private static FirebaseStorage storage;
    private static StorageReference storageGroupRef;

    public DB_Manager() {
    }

    public static void signOut(){
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


    public static FirebaseDatabase getDatabase() {
        if (database == null) {

            //Realtime Database Init
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            userRef = database.getReference("users");
            userRef.keepSynced(true);
            groupRef = database.getReference("groups");
            groupRef.keepSynced(true);
            //expenseRef = database.getReference("expense");
            //expenseRef.keepSynced(true);

            //Storege Init
            storage = FirebaseStorage.getInstance();
            storageGroupRef = storage.getReference("groups");

            //Authentication Init
            mAuth = FirebaseAuth.getInstance();
            getCurrentUserID();
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
        photoMemoryUpload(grouptopush.getId(), G.getGroupProfile());
        grouptopush.setPictureUrl(storageGroupRef + grouptopush.getId() +"/gruoprofile");
        ref.setValue(grouptopush);
        G.setGroupID(ref.getKey());
        return;
    }

    public static Group PullGroupFromDB(Group G){
        final Group g1 = G;
        DatabaseReference ref = database.getReference("groups");
         ref.orderByKey()
                 .equalTo(G.getGroupID())
                 .limitToFirst(1)
                 .addChildEventListener(new ChildEventListener() {
                     @Override
                     public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                     }

                     @Override
                     public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                         GroupDatabase g = dataSnapshot.getValue(GroupDatabase.class);
                         g1.setName(g.getName());
                         g1.setBalance(g.getBalance());
                         //fare aggiunta di un membroal gruppo
                         HomeScreen.groupAdapter.notifyDataSetChanged();
                     }

                     @Override
                     public void onChildRemoved(DataSnapshot dataSnapshot) {

                     }

                     @Override
                     public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
        return g1;
    }

    public static void PullGroupFromDBWithUserId(){

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
                                PullGroupFromDB(g);
                                HomeScreen.groupAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            HomeScreen.groupAdapter.notifyDataSetChanged();
                        }
                    });
        }

    }


    public static void pushExpensetoDB(Expense e){

    }

    public static void getCurrentUserID(){
        userRef.orderByChild("authKey")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    UserDatabase u = child.getValue(UserDatabase.class);
                    currentUserID = u.getId();
                    groupUser = u.getUserGruopsList();
                    Singleton.getInstance().clearGroups();
                    DB_Manager.getInstance().PullGroupFromDBWithUserId();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void pushNewUser(String email, String cID)
    {
        DatabaseReference ref = userRef.push();
        currentUserID = ref.getKey();
        UserDatabase newuser = new UserDatabase(ref.getKey(), cID, "380xxxxxx2", email);
        ref.setValue(newuser);
    }

    public static void photoMemoryUpload(String Gid, Bitmap bitmap)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageGroupRef.child(Gid).child("gruoprofile").putBytes(data);
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

    public static void photoMemoryDownload(String Gid, final Group G) throws IOException {


        final File localFile = new File(HomeScreen.HomeScreenContext.getFilesDir(), Gid + "images.jpg");

        if(localFile.exists()) {
            G.setGroupProfile(BitmapFactory.decodeStream(new FileInputStream(localFile)));
            HomeScreen.groupAdapter.notifyDataSetChanged();
        }

            StorageReference islandRef = storageGroupRef.child(Gid).child("gruoprofile");

            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        G.setGroupProfile(BitmapFactory.decodeStream(new FileInputStream(localFile)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    HomeScreen.groupAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

}
