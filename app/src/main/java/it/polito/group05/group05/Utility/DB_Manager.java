package it.polito.group05.group05.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.MainActivity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.CurrentUser;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

import static android.R.attr.bitmap;

/**
 * Created by andre on 05-May-17.
 */

public class DB_Manager {

    private static DB_Manager mInstance = null;

    private static FirebaseAuth mAuth;

    public static String currentUserID;
    private static List<String> groupUser = new ArrayList<>();

    private static FirebaseDatabase database;

    private static DatabaseReference    userRef,
                                        groupRef,
                                        expenseRef,
                                        usernumberRef,
                                        inviteRef;

    private static FirebaseStorage      storage;
    private static StorageReference     storageGroupRef,
                                        storageUserRef,
                                        storageExpenseRef;

    private static String userInfo = new String("userInfo");
    private static String userGroups = new String("userGroups");


    public DB_Manager() {
    }

   /* public static void signOut(){
        currentUserID = null;
        Singleton.getInstance().clearGroups();
        mAuth.signOut();
    }*/

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
            usernumberRef = database.getReference("usersNumber");
            usernumberRef.keepSynced(true);
            groupRef = database.getReference("groups");
            groupRef.keepSynced(true);
            expenseRef = database.getReference("expense");
            expenseRef.keepSynced(true);
            inviteRef = database.getReference("invites");
            inviteRef.keepSynced(true);

            //Storege Init
            storage = FirebaseStorage.getInstance();
            storageGroupRef = storage.getReference("groups");
            storageUserRef = storage.getReference("users");
            storageExpenseRef = storage.getReference("expenses");

            //Authentication Init
            mAuth = FirebaseAuth.getInstance();

        }
        return database;
    }


    public static void pushNewUser(CurrentUser currentUser) {
        UserDatabase userDatabase = currentUser;

        DatabaseReference ref = userRef.push();
        //currentUserID = ref.getKey();
        userDatabase.setId(ref.getKey());

        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("00", true);

        ref.child(userInfo).setValue(userDatabase);
        ref.child(userGroups).setValue(tmp);

        immageProfileUpload(1, userDatabase.getId(), currentUser.getImg_profile());
    }

    public static String pushNewGroup(GroupDatabase groupDatabase, Bitmap bitmap){
        DatabaseReference ref = groupRef.push();
        for(String s : groupDatabase.getMembers().keySet()){
            if(s==null) continue;
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put(groupDatabase.getId(), true);
            userRef.child(s).child(userGroups).updateChildren(temp);
        }
        immageProfileUpload(2, groupDatabase.getId(), bitmap);
        groupDatabase.setId(ref.getKey());
        ref.setValue(groupDatabase);
        return groupDatabase.getId();
    }

    public static void pushNewExpense(ExpenseDatabase expenseDatabase){
        DatabaseReference ref = expenseRef.push();
        expenseDatabase.setId(ref.getKey());
        ref.setValue(expenseDatabase);
    }

    public static void getCurrentUser() {
        userRef.orderByChild("authKey")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CurrentUser currentUser = new CurrentUser();
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals(userInfo)) {
                                UserDatabase u = child.getValue(UserDatabase.class);
                                currentUser = new CurrentUser(u);
                            }
                            else{
                                Map<String, Object> tmp = child.getValue(Map.class);
                                currentUser.setGroups(new ArrayList<String>(tmp.keySet()));
                            }
                        }
                        Singleton.getInstance().setCurrentUser(currentUser);
                        /*DOWNLOAD DELL'IMMAGINE????*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static void immageProfileUpload(int type, String Id, Bitmap bitmap){

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
            default:
                return;
        }

        final File localdir = new File(MainActivity.context.getFilesDir(), Id);

        if(!localdir.exists())
            localdir.mkdir();

        final File localFile = new File(MainActivity.context.getFilesDir(), Id + "/" + name);
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

    public static void fileUpload(String expenseId, String name, File file) {

        StorageReference ref;

        final File localdir = new File(MainActivity.context.getFilesDir(), expenseId);

        if(!localdir.exists())
            localdir.mkdir();

        final File localFile = new File(MainActivity.context.getFilesDir(), expenseId + "/" + name);

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(localFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buff = new BufferedInputStream(new FileInputStream(file));
            buff.read(bytes, 0, bytes.length);
            buff.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UploadTask uploadTask = storageExpenseRef.child(expenseId).child(name).putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

}
