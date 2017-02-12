package owdienko.jaroslaw.taskme;

/**
 * Created by Jaroslaw Owdienko on 2/6/2017. All rights reserved TaskMe!
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHandler extends SQLiteOpenHelper {
    private final String TAG = "DebugIssues";
    private static DBHandler dbHandler;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasks.db";
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK = "_task";
    private static final String COLUMN_CONTENT = "_content";
    private static final String COLUMN_IMAGE = "_image";

    public static synchronized DBHandler getInstance(Context context) {
        if (dbHandler == null) {
            dbHandler = new DBHandler(context.getApplicationContext());
        }
        return dbHandler;
    }

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        selectRowsFromDatabase();
    }


    public String getTableName() {
        return TABLE_NAME;
    }

    public String getColumnId() {
        return COLUMN_ID;
    }

    public String getColumnTask() {
        return COLUMN_TASK;
    }

    public String getColumnContent() {
        return COLUMN_CONTENT;
    }

    public String getColumnImage() {
        return COLUMN_IMAGE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK + " TEXT," +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_IMAGE + " INTEGER " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //placing row into Data Base(table).
    public void addRowToDatabase(TaskCollection collection) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, collection.get_title());
        values.put(COLUMN_CONTENT, collection.get_content());
        values.put(COLUMN_IMAGE, collection.get_image());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //removing row from table by its title.
    public void removeRowFromDatabase(int id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " == \"" + id + "\";";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    //selection all rows from the table and placing into List in other Activity.
    private void selectRowsFromDatabase() {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do {
            if (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_TASK)) != null &&
                    /*cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)) != null &&*/
                        cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)) != null) {

                    ArrayDatabase.getDataArray().addItemToArray(new TaskCollection(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TASK)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                            Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)))));
                }
            }
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
    }

    //ability to change some information from other Activity if it necessary.
    public void execQueryFromActivity(String query) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d(TAG, "SQLiteDatabase set to getWritableDatabase() -> success\n");
        db.execSQL(query);
        Log.d(TAG, "execSQL set to query -> success\n");
        db.close();
        Log.d(TAG, "query executed! -> success\n");
    }

    public void closeDatabase() {
        dbHandler.close();
    }

    public void updateRowInDatabase(int id, String str, int row) {
        String query = "";
        if (row == 0)
            query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_TASK + " = " + "\"" + str + "\"" + " WHERE " + COLUMN_ID + " = \"" + id + "\";";
        if (row == 1)
            query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_CONTENT + " = " + "\"" + str + "\"" + " WHERE " + COLUMN_ID + " = \"" + id + "\";";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void updateIdOfAllData() {
        ArrayDatabase.getDataArray().clearAllData();
        selectRowsFromDatabase();
    }
}