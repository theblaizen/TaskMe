package owdienko.jaroslaw.taskme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import owdienko.jaroslaw.taskme.Behavior.HidingScrollListener;
import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.ImagesEnum;
import owdienko.jaroslaw.taskme.Data.TaskCollection;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "DebugIssues";

    private Toolbar toolbar;
    private TextView title;
    android.support.design.widget.FloatingActionButton fabButton;
    private RecyclerView recyclerViewList;
    private CustomRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DBHandler.getInstance(this);

        initToolbar();
        initRecyclerView();

        fabButton = (FloatingActionButton) findViewById(R.id.floatingButtonMain);

        //button ADD OnClick method.
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeContentActivity.class);
                intent.putExtra("requestCodeActivity", 8624);
                (MainActivity.this).startActivityForResult(intent, 8624);
            }
        });

        //OnLongClickListener for ADD button.
        fabButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (recyclerViewAdapter.getItemCount() > 0) {
                    AlertDialog diaBox = AskOption();
                    diaBox.show();
                }
                return true;
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity);
        title = (TextView) findViewById(R.id.tv_title);
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            } catch (NullPointerException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    private void initRecyclerView() {
        recyclerViewList = (RecyclerView) findViewById(R.id.list_main_res);
        recyclerViewAdapter = new CustomRecyclerViewAdapter(this);
        recyclerViewList.setAdapter(recyclerViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewList.setOnScrollListener(new HidingScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }

            @Override
            public void onShow() {
                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                toolbar.animate().translationY(-getmToolbarHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == 4268) {
            boolean dataChanged = CustomRecyclerViewAdapter.activityResult(requestCode, resultCode, data);
            if (dataChanged)
                recyclerViewAdapter.notifyDataSetChanged();
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 8624) {
            TaskCollection collection = new TaskCollection(
                    data.getStringExtra("newTaskTitle"),
                    data.getStringExtra("newTaskContent"),
                    data.getIntExtra("newTaskImage", ImagesEnum.DEFAULT_RES));
            ArrayDatabase.getDataArray().addItemToArray(collection);
            DBHandler.getInstance(MainActivity.this).addRowToDatabase(collection);
            recyclerViewAdapter.notifyItemInserted(ArrayDatabase.getDataArray().getArraySize() - 1);
            DBHandler.getInstance(MainActivity.this).updateIdOfAllData();
            recyclerViewAdapter.notifyDataSetChanged();
        }

    }


    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete all data")
                .setIcon(R.drawable.warning_res)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        String query = "DELETE FROM " + DBHandler.getInstance(MainActivity.this).getTableName();
                        DBHandler.getInstance(MainActivity.this).execQueryFromActivity(query);
                        recyclerViewList.removeAllViews();
                        recyclerViewAdapter.notifyDataSetChanged();
                        ArrayDatabase.getDataArray().clearAllData();

                        Toast.makeText(MainActivity.this, "You have just deleted all the data!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                })


                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

}
