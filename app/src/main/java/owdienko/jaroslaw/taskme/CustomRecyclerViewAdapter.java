package owdienko.jaroslaw.taskme;

/**
 * Created by Jaroslaw Owdienko on 2/6/2017. All rights reserved TaskMe!
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.TaskCollection;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private final String TAG = "DebugIssues";
    private Context ResContext;

    public CustomRecyclerViewAdapter(Context context) {
        this.ResContext = context;
    }

    private Context getContext() {
        return ResContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, content;
        public ImageView img;

        public ViewHolder(Context context, View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.row_title);
            content = (TextView) itemView.findViewById(R.id.row_context);
            img = (ImageView) itemView.findViewById(R.id.row_image);
        }
    }


    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View custom_view = inflater.inflate(R.layout.task_row, parent, false);

        return new ViewHolder(getContext(), custom_view);
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
        final TaskCollection collection = ArrayDatabase.getDataArray().getItemByPosition(position);

        titleView.setText(collection.get_title());// .toUpperCase()
        contentView.setText(getFirstTwentyFourLetters(collection.get_content()));
        imageView.setImageResource(collection.get_image());
        holder.itemView.setLongClickable(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = holder.itemView.getContext();
                final int position = holder.getLayoutPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ChangeContentActivity.class);
                    intent.putExtra("toolbarTitle", collection.get_title());
                    intent.putExtra("idOfElement", collection.get_id());
                    intent.putExtra("requestCodeActivity", 4268);
                    Log.e(TAG, String.valueOf(collection.get_id()) + "ID CRVA");
                    intent.putExtra("positionOfElement", position);
                    ((Activity) context).startActivityForResult(intent, 4268);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = holder.itemView.getContext();
                int position = holder.getLayoutPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    Toast.makeText(context, titleView.getText() + " | REMOVED", Toast.LENGTH_SHORT).show();

                    DBHandler.getInstance(getContext()).removeRowFromDatabase(collection.get_id());
                    ArrayDatabase.getDataArray().removeItemFromArray(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return ArrayDatabase.getDataArray().getArraySize();
    }

    public static boolean activityResult(int requestCode, int resultCode, Intent data) {
        return (resultCode == Activity.RESULT_OK && requestCode == 4268);
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

}