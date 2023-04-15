package com.example.stepcounter.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.SQLClientInfoException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "stepCounter.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "stepCounterLogs";
    private static final String COLUMN_LOG_DATE = "log_date";
    private static final String COLUMN_LOG_STEPS_COUNT = "log_steps_count";
    private static final String COLUMN_LOG_ACHIEVED_GOAL = "log_achieved_goal";
    private static DatabaseHelper databaseHelperInstance;
    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_LOG_DATE + " TEXT PRIMARY KEY ,"
                + COLUMN_LOG_STEPS_COUNT + " INTEGER, "
                + COLUMN_LOG_ACHIEVED_GOAL + " INTEGER);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static DatabaseHelper getDatabaseHelperInstance(Context context) {
        if(databaseHelperInstance == null){
            databaseHelperInstance = new DatabaseHelper(context);
        }
        return databaseHelperInstance;
    }

    public static DatabaseHelper getDatabaseHelperInstance() {
        return databaseHelperInstance;
    }

    public static void addLog(int stepsCount, boolean achievedGoal) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase database = databaseHelperInstance.getWritableDatabase();
        contentValues.put(COLUMN_LOG_DATE, currentDate);
        contentValues.put(COLUMN_LOG_STEPS_COUNT, stepsCount);
        contentValues.put(COLUMN_LOG_ACHIEVED_GOAL, (achievedGoal ? 1 : 0));
        long insert_log = database.insert(TABLE_NAME, null, contentValues);
        if(insert_log == 1){
            Log.d("inserted", "yup");
        }
    }
}
