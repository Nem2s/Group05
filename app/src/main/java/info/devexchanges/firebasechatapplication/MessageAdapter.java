package info.devexchanges.firebasechatapplication;

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

import static android.support.design.R.id.info;

public class MessageAdapter extends FirebaseListAdapter<info.devexchanges.firebasechatapplication.ChatDatabase> {

    private ChatFragment chat;
    private Activity activity;

    public MessageAdapter(ChatFragment chatfrag, Class<info.devexchanges.firebasechatapplication.ChatDatabase> modelClass, int modelLayout, DatabaseReference ref) {

        super(chatfrag.getActivity(), modelClass, modelLayout, ref);
        activity=chatfrag.getActivity();
        this.chat= chat;
    }

    @Override
    protected void populateView(View v, info.devexchanges.firebasechatapplication.ChatDatabase model, int position) {
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);
        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());

        // Format the date before showing it
     //   messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
        messageTime.setText(DateFormat.format("HH:mm", model.getMessageTime()));
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        info.devexchanges.firebasechatapplication.ChatDatabase ChatDatabase = getItem(position);
     //   if (ChatDatabase.getMessageUserId().equals(Singleton.getInstance().getCurrentUser().getId()))
       //     view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        //else
        view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

        //generating view
        populateView(view, ChatDatabase, position);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}
