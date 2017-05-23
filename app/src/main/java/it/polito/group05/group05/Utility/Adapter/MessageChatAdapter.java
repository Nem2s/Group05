package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.ChatDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Holder.ChatHolder;

/**
 * Created by user on 22/05/2017.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<ChatHolder>{
    private List<ChatDatabase> listMessages;
    private Context c;



    public MessageChatAdapter(List<ChatDatabase> list, Context c){
        listMessages = list;
        this.c= c;
    }

    @Override
    public int getItemViewType(int position) {
        return (listMessages.get(position).getMessageUserId()== Singleton.getInstance().getCurrentUser().getId())?0:1;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chatView;
        switch (viewType){
            case 0://currentid
                 chatView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mess_cur_us, parent, false);
                break;
            default:
                chatView= LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
                break;
        }


        return new ChatHolder(chatView);
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        final ChatDatabase cdb = listMessages.get(position);
        holder.setData(cdb, c);
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }
}
