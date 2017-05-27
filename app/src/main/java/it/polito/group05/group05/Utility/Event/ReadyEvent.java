package it.polito.group05.group05.Utility.Event;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

/**
 * Created by antonino on 22/05/2017.
 */

public class ReadyEvent {
    GroupDatabase groupDatabase;

    public GroupDatabase getGroupDatabase() {
        return groupDatabase;
    }

    public ReadyEvent(final GroupDatabase g) {
        Map<String, Object> tmp = new HashMap<>(g.getMembers());
        groupDatabase = g;
        for (String userID : tmp.keySet()) {
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userID)
                    .child("userInfo")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) return;
                            UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                            u.setBalance(new Balance(Double.valueOf(g.getMembers().get(u.getId()).toString()), Double.valueOf(g.getMembers().get(u.getId()).toString())));
                            g.getMembers().put(u.getId(), u);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
    }
}
