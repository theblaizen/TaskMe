package owdienko.jaroslaw.taskme;

/**
 * Created by Jaroslaw Owdienko on 2/6/2017. All rights reserved TaskMe!
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.TaskCollection;
import owdienko.jaroslaw.taskme.Utils.Const;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    CustomRecyclerViewAdapter() {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content;
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.row_title);
            content = itemView.findViewById(R.id.row_context);
            img = itemView.findViewById(R.id.row_image);
        }
    }


    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        final View taskRow = inflater.inflate(R.layout.task_row, parent, false);
        final CustomRecyclerViewAdapter.ViewHolder holder = new CustomRecyclerViewAdapter.ViewHolder(taskRow);
        taskRow.setLongClickable(true);

        taskRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TaskCollection collection = ArrayDatabase.getDataArray().getItemByPosition(holder.getAdapterPosition());
                final int position = holder.getAdapterPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(taskRow.getContext(), ChangeContentActivity.class);
                    intent.putExtra(Const.TASK_ID, collection.get_id());
                    intent.putExtra(Const.MODE, Const.EDIT_TASK);
                    intent.putExtra(Const.TASK_POSITION, position);
                    ((Activity) taskRow.getContext()).startActivityForResult(intent, Const.EDIT_TASK);
                }
            }
        });

        taskRow.setOnLongClickListener(new View.OnLongClickListener() {
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
            return arg;
        else if (isSymbolInRow(arg.substring(22, 23)))
            return arg.substring(0, 23) + "...";
        else return arg.substring(0, 22) + "...";
    }


    @Override
    public void onBindViewHolder(final CustomRecyclerViewAdapter.ViewHolder holder, int position) {
        final TextView titleView = holder.title;
        final TextView contentView = holder.content;
        final ImageView imageView = holder.img;
        final TaskCollection collection = ArrayDatabase.getDataArray().getItemByPosition(position);

        titleView.setText(collection.get_title());// .toUpperCase()
        contentView.setText(getFirstTwentyFourLetters(collection.get_content()));
        imageView.setImageResource(collection.get_image());
    }

    @Override
    public int getItemCount() {
        return ArrayDatabase.getDataArray().getArraySize();
    }

    static boolean activityResult(int requestCode, int resultCode, Intent data) {
        return (resultCode == Activity.RESULT_OK && requestCode == Const.EDIT_TASK);
    }

    private boolean isSymbolInRow(String smbl) {
        boolean flag = true;
        String[] symbols = {" ", ".", ",", "!", "?", "@", ":", ";", "\'", "\"",
                "{", "[", "/", "<", "#", "$", "(", "=", "-", "_"};
        for (String symbol : symbols) {
            if (symbol.equals(smbl)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private AlertDialog AskOption(String title, String message, int icon, String positiveButton,
                                  String negativeButton, final Context cxt, final String toastTitle,
                                  final int position, final int id) {
        return new AlertDialog.Builder(cxt, R.style.AlertDialogStyle)
                //set message, title, and icon
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DBHandler.getInstance(cxt).removeRowFromDatabase(id);
                        ArrayDatabase.getDataArray().removeItemFromArray(position);
                        Toast.makeText(cxt, toastTitle + " | REMOVED", Toast.LENGTH_LONG).show();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

    }
}