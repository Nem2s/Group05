package it.polito.group05.group05;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import it.polito.group05.group05.Utility.BaseClasses.ChatDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Holder.ChatHolder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private static final int SIGN_IN_REQUEST_CODE = 111;
    private RecyclerView rec;
    private EditText input;
    private String textInput;
    private FloatingActionButton fab;
    private DatabaseReference fdb;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager ll;


    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_main, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        input = (EditText) rootView.findViewById(R.id.input);
        rec= (RecyclerView) rootView.findViewById(R.id.rec);
        rec.setHasFixedSize(false);
        ll =  new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true);
        rec.setLayoutManager(ll);
        ll.setStackFromEnd(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats")
                                .child(Singleton.getInstance().getIdCurrentGroup());
        adapter = new FirebaseRecyclerAdapter<ChatDatabase, ChatHolder>(ChatDatabase.class, R.layout.message,
                ChatHolder.class, ref) {

            @Override
            protected void populateViewHolder(ChatHolder viewHolder, ChatDatabase model, int position) {
                if(model==null) return;
                viewHolder.setData(model,getContext());
            }
            @Override
            public int getItemViewType(int position) {
                if(getItem(position).getMessageUserId().equals(Singleton.getInstance().getCurrentUser().getId())) {
                    return R.layout.mess_cur_us;
                } else return R.layout.message;
            }
        };

        rec.setAdapter(adapter);



        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    textInput = s.toString();
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().equals("")) {
                    //  Toast.makeText(ChatFragment.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                } else {
                    UserDatabase u = Singleton.getInstance().getCurrentUser();
                    fdb = FirebaseDatabase.getInstance().getReference("chats")
                            .child(Singleton.getInstance().getIdCurrentGroup())
                            .push();
                    ChatDatabase cdb = new ChatDatabase(textInput, u.getName().toString(), u.getId().toString());
                    fdb.setValue(cdb);
                    input.setText("");
                }
            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chat");
        super.onCreateView(inflater, container, savedInstanceState);
        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();}

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
