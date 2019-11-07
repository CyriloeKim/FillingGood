package com.example.fillinggoodwithdb.Control;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.fillinggoodwithdb.Database.ScheduleContract;
import com.example.fillinggoodwithdb.Entity.Schedule;

public class ScheduleController {

    public int addSchedule(String title, String place, String memo, String priority,
                                   String startDate, String startTime, String endDate, String endTime,
                                   long mScheduleID) {
        Schedule schedule = new Schedule(title, place, memo, priority, startDate, startTime, endDate,endTime);
        return schedule.save(mScheduleID);
    }

    public int modifySchedule(String title, String place, String memo, String priority,
                              String startDate, String startTime, String endDate, String endTime,
                              long mScheduleID) {
        Schedule schedule = new Schedule(title, place, memo, priority, startDate, startTime, endDate,endTime);
        return schedule.modify(mScheduleID);
    }

    public Intent readSchedule(Intent intent, Cursor cursor, long id) {
        Schedule schedule = new Schedule();
        return schedule.read(intent, cursor, id);
    }

    public int deleteSchedule(long deleteId, SQLiteDatabase db) {
        Schedule schedule = new Schedule();
        return schedule.delete(deleteId, db);
    }

}
