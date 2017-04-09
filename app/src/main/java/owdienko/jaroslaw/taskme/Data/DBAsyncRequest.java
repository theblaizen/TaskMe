package owdienko.jaroslaw.taskme.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Jaroslaw Owdienko on 4/9/2017. All rights reserved TaskMe!
 */

public class DBAsyncRequest extends AsyncTask<Object, Void, Void> {
    private String titleToUpdate, contentToUpdate;
    private int imageToUpdate;
    private long dateToUpdate;
    private Context context;

    public DBAsyncRequest() {
        //todo update init variables
    }

    public DBAsyncRequest(Context context) {
        this.context = context;
    }

    public DBAsyncRequest(Context context, String title, String content, int image, long date) {// for update all data!
        this.context = context;
        titleToUpdate = title;
        contentToUpdate = content;
        imageToUpdate = image;
        dateToUpdate = date;
    }

    public DBAsyncRequest(Context context, int image, long date) {//for update image only!
        this.context = context;
        imageToUpdate = image;
        dateToUpdate = date;
    }

    public DBAsyncRequest(Context context, String title, String content, long date) { //for update title and content only!
        this.context = context;
        titleToUpdate = title;
        contentToUpdate = content;
        dateToUpdate = date;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Object... params) {
        Log.d(Constants.LOG_TAG, "doInBackground started");
        //todo implementation DB and all commands that need to work with
        /*
        * RUN AsyncTask only:
        * in onPause to save all data,
        * in onCreate to load data,
        * in onActivityResult(not stable[not recommended to use there]) for working with update/insert data
        * */

        Log.d(Constants.LOG_TAG, "doInBackground finished");
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //todo handle cancel of AsyncTask
        Log.d(Constants.LOG_TAG, "process was interrupted");
    }
}
