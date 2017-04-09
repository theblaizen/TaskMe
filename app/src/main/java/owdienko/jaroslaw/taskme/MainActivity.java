package owdienko.jaroslaw.taskme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.Constants;
import owdienko.jaroslaw.taskme.Data.DBAsyncRequest;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.ImagesEnum;
import owdienko.jaroslaw.taskme.Data.TaskCollection;


public class MainActivity extends AppCompatActivity {

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
                    AlertDialog diaBox = AskOption("Delete all items", "Do you want to delete all data?",
                            R.drawable.warning_res, "Delete", "Cancel");
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
                Log.d(Constants.LOG_TAG, e.getMessage());
            }
        }
    }

    private void initRecyclerView() {
        recyclerViewList = (RecyclerView) findViewById(R.id.list_main_res);

        recyclerViewList.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.divider_recycler));
        recyclerViewAdapter = new CustomRecyclerViewAdapter(this);
        recyclerViewList.setAdapter(recyclerViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == 4268) {
            boolean dataChanged = CustomRecyclerViewAdapter.activityResult(requestCode, resultCode, data);
            if (dataChanged)
                recyclerViewAdapter.notifyItemChanged(data.getIntExtra("positionOfItemInRV", recyclerViewAdapter.getItemCount()));
            //new DBAsyncRequest("4268", "dagwgwag").execute();
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 8624) {
            TaskCollection collection = new TaskCollection(
                    data.getStringExtra("newTaskTitle"),
                    data.getStringExtra("newTaskContent"),
                    data.getIntExtra("newTaskImage", ImagesEnum.RESOURCES[0]));
            ArrayDatabase.getDataArray().addItemToArray(collection);
            recyclerViewAdapter.notifyItemInserted(ArrayDatabase.getDataArray().getArraySize() - 1);
            DBHandler.getInstance(MainActivity.this).addRowToDatabase(collection);
//            DBHandler.getInstance(MainActivity.this).updateIdOfAllData();
            //recyclerViewAdapter.notifyItemRangeChanged(recyclerViewAdapter.getItemCount() - 1, recyclerViewAdapter.getItemCount());
            //new DBAsyncRequest("8624", "asdfasdf").execute();
        }

    }


    private AlertDialog AskOption(String title, String message, int icon, String positiveButton, String negativeButton) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                //set message, title, and icon
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)

                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        String query = "DELETE FROM " + DBHandler.getInstance(MainActivity.this).getTableName();
                        DBHandler.getInstance(MainActivity.this).execQueryFromActivity(query);
                        recyclerViewList.removeAllViews();
                        recyclerViewAdapter.notifyDataSetChanged();
                        ArrayDatabase.getDataArray().clearAllData();

                        Toast.makeText(MainActivity.this, "You have just deleted all data!", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            toolbar.showOverflowMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (int i = 0; i < ArrayDatabase.getDataArray().getArraySize(); i++)
            DBHandler.getInstance(this).updateRowsInDatabase(ArrayDatabase.getDataArray().getItemByPosition(i));
//        new DBAsyncRequest(this,"TEST", "onDestroy test").execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        for (int i = 0; i < ArrayDatabase.getDataArray().getArraySize(); i++)
//            DBHandler.getInstance(this).updateRowsInDatabase(ArrayDatabase.getDataArray().getItemByPosition(i));
    }
}
