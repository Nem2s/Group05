package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import info.devexchanges.firebasechatapplication.ChatDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

import static android.app.Activity.RESULT_OK;
import static it.polito.group05.group05.Group_Activity.fab;
import static it.polito.group05.group05.Group_Activity.toolbar;
import static it.polito.group05.group05.R.drawable.info;


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
    FirebaseListAdapter<ChatDatabase> adapter;
    ListView listView;
    String textInput;
    DatabaseReference fdb;
    EditText input;
    TextView tv;
    private OnFragmentInteractionListener mListener;


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

    private static void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private static void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.chat_main, container, false);
            tv = (TextView) rootView.findViewById(R.id.tv_prova);
            input = (EditText) rootView.findViewById(R.id.input);
            listView = (ListView) rootView.findViewById(R.id.list);
        //  fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("chats").child(Singleton.getInstance().getIdCurrentGroup())
                        .push().setValue(new ChatDatabase("CLA cla", "ANNA", "ORA"));
            }
        });
            showAllOldMessages();

      /*      input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()> 0){
                    textInput = s.toString();
                    }
                }
            });



            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (input.getText().toString().trim().equals("")) {
                   //     Toast.makeText(ChatFragment.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                    } else {
                        UserDatabase u = Singleton.getInstance().getCurrentUser();
                        fdb =   FirebaseDatabase.getInstance().getReference("chats")
                                                .child(Singleton.getInstance().getIdCurrentGroup())
                                                .push();
                        ChatDatabase cdb = new ChatDatabase(textInput, u.getName().toString(), u.getId().toString());
                        fdb.setValue(cdb);
                        input.setText("");
                    }
                }
            });
        */
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void showAllOldMessages() {

        fdb = FirebaseDatabase.getInstance().getReference("chats").child(Singleton.getInstance().getIdCurrentGroup());
        adapter = new info.devexchanges.firebasechatapplication.MessageAdapter(this,
                info.devexchanges.firebasechatapplication.ChatDatabase.class,
                R.layout.item_in_message,
                fdb);
        listView.setAdapter(adapter);
    }

   //public String getLoggedInUserName() {
      //  return loggedInUserName;
   // }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
