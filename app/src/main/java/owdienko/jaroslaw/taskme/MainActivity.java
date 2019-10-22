package owdienko.jaroslaw.taskme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.ImagesEnum;
import owdienko.jaroslaw.taskme.Data.TaskCollection;
import owdienko.jaroslaw.taskme.Utils.Const;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fabButton;
    private RecyclerView recyclerViewList;
    private CustomRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler.getInstance(this);

        initViews();
        setupToolbar();
        initRecyclerView();
        setClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarMainActivity);
        recyclerViewList = findViewById(R.id.list_main_res);
        fabButton = findViewById(R.id.floatingButtonMain);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initRecyclerView() {
        recyclerViewList.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.divider_recycler));
        recyclerViewAdapter = new CustomRecyclerViewAdapter();
        recyclerViewList.setAdapter(recyclerViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setClickListeners() {
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeContentActivity.class);
                intent.putExtra(Const.MODE, Const.NEW_TASK);
                (MainActivity.this).startActivityForResult(intent, Const.NEW_TASK);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Const.EDIT_TASK) {
            boolean dataChanged = CustomRecyclerViewAdapter.activityResult(requestCode, resultCode, data);
            if (dataChanged)
                recyclerViewAdapter.notifyItemRangeChanged(0, recyclerViewAdapter.getItemCount());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Const.NEW_TASK) {
            TaskCollection collection = new TaskCollection(
                    data.getStringExtra("newTaskTitle"),
                    data.getStringExtra("newTaskContent"),
                    data.getIntExtra("newTaskImage", ImagesEnum.DEFAULT_RES));
            ArrayDatabase.getDataArray().addItemToArray(collection);
            DBHandler.getInstance(MainActivity.this).addRowToDatabase(collection);
            recyclerViewAdapter.notifyItemInserted(ArrayDatabase.getDataArray().getArraySize() - 1);
            DBHandler.getInstance(MainActivity.this).updateIdOfAllData();
            recyclerViewAdapter.notifyItemRangeChanged(0, recyclerViewAdapter.getItemCount());
        }
    }

    private AlertDialog AskOption(String title, String message, int icon, String positiveButton, String negativeButton) {
        return new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                //set message, title, and icon
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDelete();
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

    private void onDelete() {
        String query = "DELETE FROM " + DBHandler.getInstance(MainActivity.this).getTableName();
        DBHandler.getInstance(MainActivity.this).execQueryFromActivity(query);
        recyclerViewList.removeAllViews();
        recyclerViewAdapter.notifyItemRangeChanged(0, recyclerViewAdapter.getItemCount());
        ArrayDatabase.getDataArray().clearAllData();
        Toast.makeText(MainActivity.this, "You have just deleted all data!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
