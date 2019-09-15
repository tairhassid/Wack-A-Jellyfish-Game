package whack.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import whack.activities.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DB_NAME = "whack.db";
    public static final String TABLE_NAME = "records_data";
    public static final String COL_PLAYER_NAME = "name";
    public static final String COL_SCORE = "score";
    public static final String COL_LAT = "latitude";
    public static final String COL_LNG = "longitude";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PLAYER_NAME + " TEXT, " + COL_SCORE + " INTEGER, " +
            COL_LAT + " DOUBLE, " + COL_LNG + " DOUBLE " + ")";


    public DatabaseHelper(Context context) {
        super(context, context.getResources().getString(R.string.app_name), null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO
    }

    public long put(String playerName, int score, double lat, double lng) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PLAYER_NAME, playerName);
        contentValues.put(COL_SCORE, score);
        contentValues.put(COL_LAT, lat);
        contentValues.put(COL_LNG, lng);

        return this.put(contentValues);
    }

    public long put(ContentValues contentValues) {
        long rowId;
        SQLiteDatabase db = this.getWritableDatabase();

        rowId = db.insert(TABLE_NAME, null, contentValues);

        return rowId;
    }

    public Cursor getCursor(int limit) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME +
                " ORDER BY " + COL_SCORE + " DESC LIMIT " + limit;
        Cursor cursor = database.rawQuery(selectQuery, null);

        return cursor;
    }

//    public Cursor getCursor(String selectQuery) {
//        SQLiteDatabase database = this.getReadableDatabase();
////        String selectQuery = "SELECT * FROM " + TABLE_NAME; // Instead of "SELECT * FROM"
//        Cursor cursor = database.rawQuery(selectQuery, null);
//
//        return cursor;
//    }
}
