package owdienko.jaroslaw.taskme;

/**
 * Created by Jaroslaw Owdienko on 2/6/2017. All rights reserved TaskMe!
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.Constants;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.TaskCollection;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private Context ResContext;

    public CustomRecyclerViewAdapter(Context context) {
        this.ResContext = context;
    }

    private Context getContext() {
        return ResContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, content, date;
        public ImageView img;

        public ViewHolder(Context context, View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.row_title);
            content = (TextView) itemView.findViewById(R.id.row_context);
            date = (TextView) itemView.findViewById(R.id.row_date);
            img = (ImageView) itemView.findViewById(R.id.row_image);
        }
    }


    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        final View custom_view = inflater.inflate(R.layout.task_row, parent, false);
        final CustomRecyclerViewAdapter.ViewHolder holder = new CustomRecyclerViewAdapter.ViewHolder(getContext(), custom_view);
        custom_view.setLongClickable(true);

        custom_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TaskCollection collection = ArrayDatabase.getDataArray().getItemByPosition(holder.getAdapterPosition());
                final int position = holder.getAdapterPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(custom_view.getContext(), ChangeContentActivity.class);
                    intent.putExtra("idOfElement", collection.get_id());
                    intent.putExtra("requestCodeActivity", 4268);
                    intent.putExtra("positionOfElement", position);
                    ((Activity) custom_view.getContext()).startActivityForResult(intent, 4268);
                }
            }
        });

        custom_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final TaskCollection collection = ArrayDatabase.getDataArray().getItemByPosition(holder.getAdapterPosition());
                final int position = holder.getAdapterPosition();  // gets item position
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    AlertDialog diaBox = AskOption("Delete item", "Do you want to delete an item?", R.drawable.warning_res,
                            "Delete", "Cancel", context, collection.get_title(),
                            position, collection.get_id());
                    diaBox.show();
                }
                return true;
            }
        });

        return holder;
    }

    //parsing string and cutting 4 words from content.
    public String getFirstTwentyFourLetters(String arg) {
        if (arg.isEmpty())
            return arg;
        else if (arg.length() < 23)
            return arg.substring(0, arg.length());
        else if (isSymbolInRow(arg.substring(22, 23)))
            return arg.substring(0, 23) + "...";
        else return arg.substring(0, 22) + "...";
    }


    @Override
    public void onBindViewHolder(final CustomRecyclerViewAdapter.ViewHolder holder, int position) {
        final TextView titleView = holder.title;
        final TextView contentView = holder.content;
        final ImageView imageView = holder.img;
        final TextView dateView = holder.date;
        final TaskCollection collection = ArrayDatabase.getDataArray().getItemByPosition(position);
        Calendar calendar = new GregorianCalendar();


        titleView.setText(collection.get_title());// .toUpperCase()
        contentView.setText(getFirstTwentyFourLetters(collection.get_content()));
        imageView.setImageResource(collection.get_image());
        dateView.setText(String.valueOf(format(calendar.getTime()))); //TODO date implementation
    }

    @Override
    public int getItemCount() {
        return ArrayDatabase.getDataArray().getArraySize();
    }

    public static boolean activityResult(int requestCode, int resultCode, Intent data) {
        return (resultCode == Activity.RESULT_OK && requestCode == 4268);
    }

    private static String format(Date date) {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(date);
    }

    private boolean isSymbolInRow(String smbl) {
        boolean flag = true;
        String[] symbols = {" ", ".", ",", "!", "?", "@", ":", ";", "\'", "\"",
                "{", "[", "/", "<", "#", "$", "(", "=", "-", "_"};
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equals(smbl)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private AlertDialog AskOption(String title, String message, int icon, String positiveButton,
                                  String negativeButton, final Context cxt, final String toastTitle,
                                  final int position, final int id) {
        Log.e(Constants.LOG_TAG, "isInDialog?");
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(cxt, R.style.AlertDialogStyle)
                //set message, title, and icon
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DBHandler.getInstance(cxt).removeRowFromDatabase(id);
                        ArrayDatabase.getDataArray().removeItemFromArray(position);
                        Toast.makeText(cxt, toastTitle + " | REMOVED", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);
                        //notifyItemChanged(position);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}