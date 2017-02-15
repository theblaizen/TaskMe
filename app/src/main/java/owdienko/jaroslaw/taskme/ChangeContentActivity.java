package owdienko.jaroslaw.taskme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import owdienko.jaroslaw.taskme.Data.ArrayDatabase;
import owdienko.jaroslaw.taskme.Data.DBHandler;
import owdienko.jaroslaw.taskme.Data.ImagesEnum;
import owdienko.jaroslaw.taskme.Data.TaskCollection;

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
        toolbar = (Toolbar) findViewById(R.id.toolbarChangeContentActivity);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            try {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            } catch (NullPointerException e) {
                Log.d(TAG, e.getMessage());
            }
        }

        intentToolbarTitle = getIntent();
        positionOfElement = intentToolbarTitle.getIntExtra("positionOfElement", 0);
        requestCodeActivity = intentToolbarTitle.getIntExtra("requestCodeActivity", 0);

        Log.e(TAG, String.valueOf(positionOfElement));
        content = (EditText) findViewById(R.id.contentChangeContentActivity);
        title = (EditText) findViewById(R.id.edit_title_change);
        title.getBackground().setColorFilter(getResources().getColor(R.color.true_white), PorterDuff.Mode.SRC_IN);
        title.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (requestCodeActivity == 4268) {
            newCollection = ArrayDatabase.getDataArray().getItemByPosition(positionOfElement);
            idOfElement = newCollection.get_id();
            title.setText(newCollection.get_title());
            title.setSelection(title.getText().length());
            if (!newCollection.get_content().matches("^\\s*$"))
                content.setText(newCollection.get_content());
        }

        if (requestCodeActivity == 8624) {
            //idOfElement = newCollection.get_id();
        }

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

        if (requestCodeActivity == 8624) {
            Intent data_collection = new Intent();
            if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()
                    && !content.getText().toString().matches("^\\s*$")
                    && !title.getText().toString().matches("^\\s*$")) {
                data_collection.putExtra("newTaskTitle", title.getText().toString());
                data_collection.putExtra("newTaskContent", content.getText().toString());
                data_collection.putExtra("newTaskImage", ImagesEnum.DEFAULT_RES); //todo selection of image
            }
            //todo title auto generation
            this.setResult(Activity.RESULT_OK, data_collection);
        }

        if (requestCodeActivity == 4268) {
            if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                newCollection.set_content(content.getText().toString());
                newCollection.set_title(title.getText().toString());
                ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
            } else {
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
