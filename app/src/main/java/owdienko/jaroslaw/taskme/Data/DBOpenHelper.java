package owdienko.jaroslaw.taskme.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static owdienko.jaroslaw.taskme.Data.ImagesEnum.RESOURCES;

/**
 * Created by Jaroslaw Owdienko on 4/3/2017. All rights reserved TaskMe!
 */

public class DBOpenHelper extends SQLiteOpenHelper implements DBContract {

    private static final int DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TASKS + "(" +
                Tasks.TASKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Tasks.TASKS_TITLE + " TEXT UNIQUE NOT NULL, " +
                Tasks.TASKS_CONTENT + " TEXT, " +
                Tasks.TASKS_DATE + " INTEGER, " +
                Tasks.TASKS_IMAGE + " INTEGER " +
                ")");
        db.execSQL(
                "CREATE TABLE " + IMAGES + "(" +
                        Images.IMAGES_ID + " INTEGER PRIMARY KEY, " +
                        Images.IMAGES_RESOURCE + " TEXT " +
                        ")");

        for (int i = 1; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put(Images.IMAGES_ID, i);
            values.put(Images.IMAGES_RESOURCE, RESOURCES[i - 1]);

            db.insert(IMAGES, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TASKS);
        db.execSQL("DROP TABLE " + IMAGES);
        onCreate(db);
    }
}
