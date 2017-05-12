package it.polito.group05.group05.Utility.Adapter;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import it.polito.group05.group05.ChatFragment;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.ChatDatabase;

public class MessageAdapter extends FirebaseListAdapter<ChatDatabase> {

    private Activity activity;

    public MessageAdapter(ChatFragment chatfrag, Class<ChatDatabase> modelClass, int modelLayout, DatabaseReference ref) {

        super(chatfrag.getActivity(), modelClass, modelLayout, ref);
        activity=chatfrag.getActivity();
    }

    @Override
    protected void populateView(View v, ChatDatabase model, int position) {
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);
        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());
        messageTime.setText(DateFormat.format("hh:mm",model.getMessageTime()));
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChatDatabase cdb = getItem(position);
        if (cdb.getMessageUserId().equals(Singleton.getInstance().getCurrentUser().getId())){
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);}
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);

        populateView(view, cdb, position);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
