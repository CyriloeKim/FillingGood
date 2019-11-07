package com.example.fillinggoodwithdb.Entity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.fillinggoodwithdb.Boundary.ScheduleAdditionForm;
import com.example.fillinggoodwithdb.Database.ScheduleContract;
import com.example.fillinggoodwithdb.Database.ScheduleDbHelper;

public class Schedule {

    // 필드
    private String title;
    private String place;
    private String memo;
    private String priority;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    // 생성자
    public Schedule() {};
    public Schedule(String title, String place, String memo, String priority,
                    String startDate, String startTime, String endDate, String endTime) {
        this.title = title;
        this.place = place;
        this.memo = memo;
        this.priority = priority;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    // DB에 Schedule 저장
    public int save(long mScheduleID) {

        // SQLite에 저장하는 기본적인 방법 = ContentValues라는 객체를 만들어 거기에 담아서 DB에 저장
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_TITLE, this.getTitle());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PLACE, this.getPlace());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_MEMO, this.getMemo());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PRIORITY, this.getPriority());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_DATE, this.getStartDate());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_TIME, this.getStartTime());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_DATE, this.getEndDate());
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_TIME, this.getEndTime());
        // DB에 작성할 것이기 때문에 WritableDatabase
        SQLiteDatabase db = ScheduleDbHelper.getInstance(null).getWritableDatabase();
        // 수정이 아니라 최초 저장인 경우
        long newRowID = db.insert(ScheduleContract.ScheduleEntry.TABLE_NAME,
                null,
                contentValues);

        if (newRowID == -1) {
            return -1;
            // Toast.makeText(this, "저장에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
        } else {
            return 0;
            // Toast.makeText(this, "일정이 저장되었습니다", Toast.LENGTH_SHORT).show();
            // setResult(RESULT_OK);
        }
        // 잘 되었는지 안 되었는지는 return 값으로 확인 가능
        // 잘 되었다면 return row_ID(long type), 안 되었다면 return -1

    }

    public int modify(long mScheduleID) {
        // SQLite에 저장하는 기본적인 방법 = ContentValues라는 객체를 만들어 거기에 담아서 DB에 저장
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PLACE, place);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_MEMO, memo);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PRIORITY, priority);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_DATE, startDate);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_TIME, startTime);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_DATE, endDate);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_TIME, endTime);

        // DB에 작성할 것이기 때문에 WritableDatabase
        SQLiteDatabase db = ScheduleDbHelper.getInstance(null).getWritableDatabase();
        // 수정인 경우
        // count = 수정된 row의 개수
        int count = db.update(ScheduleContract.ScheduleEntry.TABLE_NAME, contentValues,
                ScheduleContract.ScheduleEntry._ID + " = " + mScheduleID, null);
        if (count == 0) {
            return -1;
            // Toast.makeText(this, "수정에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
        } else {
            return 0;
            // Toast.makeText(this, "일정이 수정되었습니다", Toast.LENGTH_SHORT).show();
            // setResult(RESULT_OK);
        }
    }

    public int delete(long deleteId, SQLiteDatabase db) {
        int deletedCount = db.delete(ScheduleContract.ScheduleEntry.TABLE_NAME,
                ScheduleContract.ScheduleEntry._ID + " = " + deleteId, null);
        if (deletedCount == 0) {
            return -1;
            // Toast.makeText(ScheduleManagementForm.this, "삭제에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
        } else {
            return 0;
            // 삭제된 것을 반영해 최신내용으로 갱신
            // mAdapter.swapCursor(getScheduleCursor());
            // Toast.makeText(ScheduleManagementForm.this, "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void read(Intent intent, Cursor cursor, long id) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_TITLE));
        String place = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_PLACE));
        String memo = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_MEMO));
        String priority = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_PRIORITY));
        String startDate = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_DATE));
        String startTime = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_TIME));
        String endDate = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_DATE));
        String endTime = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_TIME));

        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("place", place);
        intent.putExtra("memo", memo);
        intent.putExtra("priority", priority);
        intent.putExtra("startDate", startDate);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endDate", endDate);
        intent.putExtra("endTime", endTime);
    }

    // GETTERs & SETTERs
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

}
