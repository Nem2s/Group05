package it.polito.group05.group05.Utility;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;

import static it.polito.group05.group05.Utility.ColorUtils.context;

/**
 * Created by antonino on 03/04/2017.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Expense> list;
    LayoutInflater li;
    Context c;
    //added view types
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;

    public ExpenseAdapter(Context c, List<Expense> data) {
        li = LayoutInflater.from(c);
        this.c = c;
        list = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View rootView = li.inflate(R.layout.item_expense, parent, false);
            return ExpenseAdapterHolder.newInstance(rootView);
        } else if (viewType == TYPE_HEADER) {
            View rootView = li.inflate(R.layout.fragment_header_group, parent, false);
            return new ExpenseAdapterHeaderHolder(rootView);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types    correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isPositionHeader(position)) {
            ((ExpenseAdapterHolder)holder).setData(list.get(position -1), c);
        }

    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }


    //added a method that returns viewType for a given position
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    //added a method to check if given position is a header
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}


class ExpenseAdapterHolder extends ViewHolder {
    ImageView expense_image;
    TextView name;
    TextView price;
    RecyclerView rv;
    TextView description;
    TextView file;
    CardView cv;
    Intent intent;


    public ExpenseAdapterHolder(View itemView, ImageView expense_image, TextView name, TextView price,
                                TextView description, CardView cv, RecyclerView rv, TextView file) {
        super(itemView);
        this.expense_image = expense_image;
        this.name = name;
        this.price = price;
        this.description = description;
        this.cv = cv;
        this.rv = rv;
        this.file = file;
    }


        public static ExpenseAdapterHolder newInstance(View itemView) {
            ImageView expense_image = (ImageView) itemView.findViewById(R.id.expense_image);
            TextView name= (TextView) itemView.findViewById(R.id.expense_name);
            TextView price= (TextView) itemView.findViewById(R.id.expense_price);
            TextView description=(TextView) itemView.findViewById(R.id.expense_owner);
            CardView cv = (CardView) itemView.findViewById(R.id.card_expense);
            RecyclerView rv = (RecyclerView) itemView.findViewById(R.id.expense_rv);
            TextView file= (TextView) itemView.findViewById(R.id.expense_file);
            ExpenseAdapterHolder e = new ExpenseAdapterHolder(itemView, expense_image, name, price, description, cv, rv, file);
            return e;
        }


    public void setData(final Expense expense, final Context context){

        if(expense.getImage()==null)
            expense.setImage(String.valueOf(R.drawable.idea));
        expense_image.setImageResource(Integer.parseInt(expense.getImage()));
        name.setText(expense.getName());
        if(expense.getFilePresence()){
            file.setText(expense.getNameFile());
            //ANDREA GIULIANO
            file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File url = expense.getFile();
                    Uri uri = Uri.fromFile(url);
                    intent = new Intent(Intent.ACTION_VIEW);
                    if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                        // Word document
                        intent.setDataAndType(uri, "application/msword");
                    } else if (url.toString().contains(".pdf")) {
                        // PDF file
                        intent.setDataAndType(uri, "application/pdf");
                    } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                        // Powerpoint file
                        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                    } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                        // Excel file
                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                    } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                        // WAV audio file
                        intent.setDataAndType(uri, "application/x-wav");
                    } else if (url.toString().contains(".rtf")) {
                        // RTF file
                        intent.setDataAndType(uri, "application/rtf");
                    } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                        // WAV audio file
                        intent.setDataAndType(uri, "audio/x-wav");
                    } else if (url.toString().contains(".gif")) {
                        // GIF file
                        intent.setDataAndType(uri, "image/gif");
                    } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                        // JPG file
                        intent.setDataAndType(uri, "image/jpeg");
                    } else if (url.toString().contains(".txt")) {
                        // Text file
                        intent.setDataAndType(uri, "text/plain");
                    } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                        // Video files
                        intent.setDataAndType(uri, "video/*");
                    } else {
                        intent.setDataAndType(uri, "*/*");
                    }
                    context.startActivity(intent);
                }
            });
        }

        description.setText("Posted by "+expense.getOwner().getUser_name()+" on "+expense.getTimestamp().toString());
        LinearLayoutManager l = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        rv.setLayoutManager(l);
        price.setText(String.format("%.2f €",expense.getPrice()));
        final ExpenseCardAdapter adapter = new ExpenseCardAdapter(context,expense.getPartecipants());
        rv.setAdapter(adapter);
        rv.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        file.setVisibility(View.GONE);

    if(expense.getType()== TYPE_EXPENSE.NOTMANDATORY){
        price.setTextColor(context.getResources().getColor(R.color.colorAccent));
        if(expense.getDebtUser(Singleton.getInstance().getId())==0.0) {
            AnimUtils.bounce((View) price, 15000, context, true);
        }
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                View s = LayoutInflater.from(context).inflate(R.layout.layout, null, false);
                dialog.setView(s);
                dialog.setTitle("Choose your amount");
                dialog.setCanceledOnTouchOutside(true);
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Double d = Double.valueOf(((EditText) dialog.findViewById(R.id.expense_amount_not_mandatory)).getText().toString());
                        d = d * (-1.00);
                        expense.setDebtUser(Singleton.getInstance().getId(), d);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }




        });
    }
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(description.getVisibility()==View.GONE) {
                    description.setVisibility(View.VISIBLE);
                    file.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);

                }
                else {
                    description.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    file.setVisibility(View.GONE);
                }
            }
        });

   /*     cv.setOnLongClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        */

    }



}

class ExpenseAdapterHeaderHolder extends ViewHolder {

    public ExpenseAdapterHeaderHolder(View itemView) {
        super(itemView);
    }
}
