package owdienko.jaroslaw.taskme.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jaroslaw Owdienko on 4/4/2017. All rights reserved TaskMe!
 */

public class DBBackend implements DBContract {
    private final DBOpenHelper tasksDbOpenHelper;

    DBBackend(Context context) {
        tasksDbOpenHelper = new DBOpenHelper(context);
    }

    DBBackend(DBOpenHelper dbOpenHelper) {
        tasksDbOpenHelper = dbOpenHelper;
    }


    public Cursor getAllRowsFromDB() {
        SQLiteDatabase db = tasksDbOpenHelper.getWritableDatabase();
        String tables = TASKS + " LEFT JOIN " + IMAGES + " ON " +
                TASKS + "." + Tasks.TASKS_IMAGE + "=" + IMAGES + "." + Images.IMAGES_ID;
        String[] columns = new String[]{TASKS + "." + Tasks.TASKS_ID,
                TASKS + "." + Tasks.TASKS_TITLE,
                TASKS + "." + Tasks.TASKS_CONTENT,
                Images.IMAGES_RESOURCE};
//        String where = inputUserText == null
//                ? null : Pages.PAGE_URL + " LIKE ?";
//        String[] whereArgs = inputUserText == null
//                ? null : new String[]{"%" + inputUserText + "%"};
        String orderBy = Tasks.TASKS_DATE + " DESC";

        Cursor c = db.query(tables, columns,
                null, null, null, null, orderBy);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void removeAllRowsFromDB() {
        SQLiteDatabase db = tasksDbOpenHelper.getWritableDatabase();
        String query = "DELETE FROM " + TASKS;
        db.execSQL(query);
    }

    public Cursor getOneRowByTitle(String title) {
        SQLiteDatabase db = tasksDbOpenHelper.getWritableDatabase();
        String tables = TASKS + " LEFT JOIN " + IMAGES + " ON " +
                TASKS + "." + Tasks.TASKS_IMAGE + "=" + IMAGES + "." + Images.IMAGES_ID;
        String[] columns = new String[]{
                TASKS + "." + Tasks.TASKS_ID,
                TASKS + "." + Tasks.TASKS_TITLE,
                TASKS + "." + Tasks.TASKS_CONTENT,
                TASKS + "." + Tasks.TASKS_DATE,
                IMAGES + "." + Images.IMAGES_RESOURCE
        };
        String where = title == null
                ? null : Tasks.TASKS_TITLE + " LIKE ?";
        String[] whereArgs = title == null
                ? null : new String[]{"%" + title + "%"};

        Cursor c = db.query(tables, columns,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void removeOneRowBy() {

    }

    private long insertRowInDB(String title, String content, int image, Date date) {
        SQLiteDatabase db = tasksDbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tasks.TASKS_TITLE, title);
        values.put(Tasks.TASKS_CONTENT, content);
        values.put(Tasks.TASKS_DATE, persistDate(date));
        values.put(Tasks.TASKS_IMAGE, image);
        return db.insert(TASKS, null, values);
    }

    public long updateImageInRow() {
        //TODO update image in db
        return 2;
    }

    public long updateDateInRow() {
        //TODO date update
        return 2;
    }

    public void insertItems(String x) {//todo transaction method
        SQLiteDatabase db = tasksDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
//            final Date timeMs = new Date();
//            Calendar calendar = new GregorianCalendar();
//            calendar.setTime(timeMs);
//            calendar.getTime().getTime();

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }


    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    public static Date loadDate(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return new Date(cursor.getLong(index));
    }//entity.setDate(loadDate(cursor, INDEX));
}
