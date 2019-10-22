package owdienko.jaroslaw.taskme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.ImagesEnum;
import owdienko.jaroslaw.taskme.Data.TaskCollection;
import owdienko.jaroslaw.taskme.Utils.Const;

public class ChangeContentActivity extends AppCompatActivity {
    private final String TAG = "DebugIssues";

    private Toolbar toolbar;
    private Intent intentToolbarTitle;
    private int positionOfElement;
    private EditText content;
    private EditText title;
    private TaskCollection newCollection;
    private int idOfElement;
    private int requestCodeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_content);
        initViews();
        setupToolbar();

        setToolbarTitle();
        setClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarChangeContentActivity);
        content = findViewById(R.id.contentChangeContentActivity);
        title = findViewById(R.id.edit_title_change);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setToolbarTitle(){
        intentToolbarTitle = getIntent();
        positionOfElement = intentToolbarTitle.getIntExtra(Const.TASK_POSITION, 0);
        requestCodeActivity = intentToolbarTitle.getIntExtra(Const.MODE, 0);

        Log.e(TAG, String.valueOf(positionOfElement));

        title.getBackground().setColorFilter(getResources().getColor(R.color.true_white), PorterDuff.Mode.SRC_IN);
        title.requestFocus();

        if (requestCodeActivity == Const.EDIT_TASK) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            newCollection = ArrayDatabase.getDataArray().getItemByPosition(positionOfElement);
            idOfElement = newCollection.get_id();
            title.setText(newCollection.get_title());
            title.setSelection(title.getText().length());
            if (!newCollection.get_content().matches("^\\s*$"))
                content.setText(newCollection.get_content());
        }

        if (requestCodeActivity == Const.NEW_TASK) {
            //idOfElement = newCollection.get_id();
        }
    }

    private void setClickListeners(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change_image_res:
                        //todo image selection activity!
                        break;
                    case R.id.change_clear_content:
                        if (!content.getText().toString().isEmpty()) {
                            content.setText("");
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (requestCodeActivity == Const.NEW_TASK) {
            Intent data_collection = new Intent();
            if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()
                    && !content.getText().toString().matches("^\\s*$")
                    && !title.getText().toString().matches("^\\s*$")) {
                data_collection.putExtra("newTaskTitle", title.getText().toString().trim());
                data_collection.putExtra("newTaskContent", content.getText().toString().trim());
                data_collection.putExtra("newTaskImage", ImagesEnum.DEFAULT_RES); //todo selection of image
            }
            //todo title auto generation
            this.setResult(Activity.RESULT_OK, data_collection);
        } else if (requestCodeActivity == Const.EDIT_TASK) {
            if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                newCollection.set_content(content.getText().toString().trim());
                newCollection.set_title(title.getText().toString().trim());
                ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
            } else {
                //TODO auto title generation
                newCollection.set_content(" ");
                newCollection.set_title("Title is empty");
                ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
            }
            this.setResult(Activity.RESULT_OK);
        }

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_change, menu);
        return true;
    }

}
