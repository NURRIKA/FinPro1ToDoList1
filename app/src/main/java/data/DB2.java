package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB2 extends SQLiteOpenHelper {

    public DB2(Context context) {
        super(context, DB1.DB_NAME, null, DB1.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DB1.TaskEntry.TABLE + " ( " +
                DB1.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB1.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB1.TaskEntry.TABLE);
        onCreate(db);
    }
}
