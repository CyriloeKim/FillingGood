package com.example.fillinggoodwithdb.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ScheduleDbHelper extends SQLiteOpenHelper {

    private static ScheduleDbHelper sInstance;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Schedules.db"; // DB 파일명 이름
    private static final String SQL_CREATE_ENTREIS =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, " +
                            "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    ScheduleContract.ScheduleEntry.TABLE_NAME,
                    ScheduleContract.ScheduleEntry._ID,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_TITLE,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_PLACE,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_MEMO,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_PRIORITY,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_START_DATE,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_START_TIME,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_END_DATE,
                    ScheduleContract.ScheduleEntry.COLUMN_NAME_END_TIME);
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScheduleContract.ScheduleEntry.TABLE_NAME;

    public static ScheduleDbHelper getInstance(Context context) {
        if (sInstance == null)  sInstance = new ScheduleDbHelper(context);
        return sInstance;
    }
    
    public ScheduleDbHelper(@Nullable Context context) { super(context, DB_NAME, null, DB_VERSION); }

    public ScheduleDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // 최초에 DB를 생성하는 경우
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTREIS);
    }

    @Override
    // 이미 생성된 DB를 삭제하고 다시 생성해주는 방식으로 구현
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTREIS);
        // 바로 윗줄은 onCreate(db)와 동일
    }
}
