package com.example.stepcounter.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String COLUMN_LOG_DISTANCE = "log_distance_in_km";
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
                + COLUMN_LOG_DISTANCE + " REAL, "
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

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());

        return dateFormat.format(calendar.getTime());
    }

    public static void addLog(int stepsCount, float distance, boolean achievedGoal) {
        SQLiteDatabase database = databaseHelperInstance.getWritableDatabase();

        ContentValues contentValues = DatabaseHelper.getInsertingContentValues(stepsCount, distance, achievedGoal);
        contentValues.put(COLUMN_LOG_DATE, DatabaseHelper.getTodayDate());

        long insert_log = database.insert(TABLE_NAME, null, contentValues);
        if(insert_log == 1){
            Log.d("inserted", "yup");
        }
    }

    public static void updateLog(int stepsCount, float distance, boolean achievedGoal) {
        SQLiteDatabase database = databaseHelperInstance.getWritableDatabase();

        ContentValues contentValues = DatabaseHelper.getInsertingContentValues(stepsCount, distance, achievedGoal);

        String updateOnColumn = COLUMN_LOG_DATE + " LIKE ?";
        String[] updateOnColumnArgs = {DatabaseHelper.getTodayDate()};
        long update_log = database.update(TABLE_NAME, contentValues, updateOnColumn, updateOnColumnArgs);

        if(update_log == 1){
            Log.d("updated", "yup");
        }
    }

    public static ContentValues getInsertingContentValues(int stepsCount, float distance, boolean achievedGoal) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOG_STEPS_COUNT, stepsCount);
        contentValues.put(COLUMN_LOG_DISTANCE, distance);
        contentValues.put(COLUMN_LOG_ACHIEVED_GOAL, (achievedGoal ? 1 : 0));
        return contentValues;
    }

    public static Cursor getLastFiveDaysLogs() {
        String readLogsQuery = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY " + COLUMN_LOG_DATE + " DESC "
                + "LIMIT 5;";
        SQLiteDatabase database = databaseHelperInstance.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(readLogsQuery, null);
        }

        return cursor;
    }

    public static Cursor getTodayLog() {
        String checkForLogs = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOG_DATE + " LIKE \"" + DatabaseHelper.getTodayDate() + "\";";
        SQLiteDatabase database = databaseHelperInstance.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(checkForLogs, null);
        }

        return cursor;
    }

    public static Cursor getTotalStepsCount() {
        return getTotalValuesOfColumn(COLUMN_LOG_STEPS_COUNT);
    }

    public static Cursor getTotalDistance() {
        return getTotalValuesOfColumn(COLUMN_LOG_DISTANCE);
    }

    public static Cursor getTotalValuesOfColumn(String column){
        String checkForLogs = "SELECT sum(" + column + " ) FROM " + TABLE_NAME + ";";
        SQLiteDatabase database = databaseHelperInstance.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(checkForLogs, null);
        }

        return cursor;
    }

    public static boolean didTheUserWorkoutToday() {
        Cursor cursor = DatabaseHelper.getTodayLog();
        return cursor.getCount() != 0;
    }
}
