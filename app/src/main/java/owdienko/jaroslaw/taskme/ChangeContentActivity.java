package owdienko.jaroslaw.taskme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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

        if (requestCodeActivity == 4268) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            newCollection = ArrayDatabase.getDataArray().getItemByPosition(positionOfElement);
            idOfElement = newCollection.get_id();
            title.setText(newCollection.get_title());
            title.setSelection(title.getText().length());
            if (!newCollection.get_content().matches("^\\s*$"))
                content.setText(newCollection.get_content());
        }

//        if (requestCodeActivity == 8624) {
//            newCollection = ArrayDatabase.getDataArray().getItemByPosition(positionOfElement);
//        }

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
                    && !title.getText().toString().matches("^\\s*$")
                    && !title.getText().toString().equals("")
                    && !content.getText().toString().equals("")) {
                data_collection.putExtra("newTaskTitle", title.getText().toString().trim());
                data_collection.putExtra("newTaskContent", content.getText().toString().trim());
                data_collection.putExtra("newTaskImage", ImagesEnum.RESOURCES[0]); //todo selection of image
                this.setResult(Activity.RESULT_OK, data_collection);
            } else {
                this.setResult(Activity.RESULT_CANCELED);
            }
            //todo title auto generation
        }

        if (requestCodeActivity == 4268) {
            if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                Intent data_collection = new Intent();
                data_collection.putExtra("positionOfItemInRV", positionOfElement);
                newCollection.set_content(content.getText().toString().trim());
                newCollection.set_title(title.getText().toString().trim());
                ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
                this.setResult(Activity.RESULT_OK, data_collection);
            } else {
                //TODO auto title generation
//                newCollection.set_content(" ");
//                newCollection.set_title(" ");
//                ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
//                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
//                DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
                this.setResult(Activity.RESULT_CANCELED);
            }
        }

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_change, menu);
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

        if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()
                && !content.getText().toString().matches("^\\s*$")
                && !title.getText().toString().matches("^\\s*$")
                && !title.getText().toString().equals("")
                && !content.getText().toString().equals("")
                && requestCodeActivity == 4268) {
            newCollection.set_content(content.getText().toString().trim());
            newCollection.set_title(title.getText().toString().trim());
            ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
            DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
            DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (!content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()
//                && !content.getText().toString().matches("^\\s*$")
//                && !title.getText().toString().matches("^\\s*$")
//                && !title.getText().toString().equals("")
//                && !content.getText().toString().equals("")
//                && requestCodeActivity == 4268) {
//            newCollection.set_content(content.getText().toString().trim());
//            newCollection.set_title(title.getText().toString().trim());
//            ArrayDatabase.getDataArray().updateItemInArray(positionOfElement, newCollection);
//            DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_content(), 1);
//            DBHandler.getInstance(this).updateRowInDatabase(idOfElement, newCollection.get_title(), 0);
//        }

        toolbar = null;
        intentToolbarTitle = null;
        content = null;
        title = null;
        newCollection = null;

    }
}
