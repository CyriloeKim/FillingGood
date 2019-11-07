package com.example.fillinggoodwithdb.Boundary;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggoodwithdb.Control.ScheduleController;
import com.example.fillinggoodwithdb.Database.ScheduleContract;
import com.example.fillinggoodwithdb.Database.ScheduleDbHelper;
import com.example.fillinggoodwithdb.R;

import java.util.Calendar;

// Act as SubActivity, context -> schedule_add.xml
public class ScheduleModificationForm extends AppCompatActivity {

    private EditText mTitleEditText, mPlaceEditText, mMemoEditText;
    private TextView mStartDateTextView, mEndDateTextView, mStartTimeTextView, mEndTimeTextView;
    private CalendarView mCalendarView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button modifyButton;
    private RadioGroup radioGroup;
    private RadioButton fixed, hardly, easily;
    private long mScheduleID = -1;
    public String TITLE, PLACE, MEMO, PRIORITY, START_DATE, START_TIME, END_DATE, END_TIME,
            priority, startDate, startTime, endDate, endTime, amPm;
    public ScheduleController scheduleController = new ScheduleController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_add);

        // element 할당하기
        mCalendarView = findViewById(R.id.calenderView);

        mTitleEditText = findViewById(R.id.event_title);
        mPlaceEditText = findViewById(R.id.event_place);
        mMemoEditText = findViewById(R.id.event_memo);

        mStartDateTextView = findViewById(R.id.event_start_date);
        mStartTimeTextView = findViewById(R.id.event_start_time);
        mEndDateTextView = findViewById(R.id.event_end_date);
        mEndTimeTextView = findViewById(R.id.event_end_time);

        radioGroup = findViewById(R.id.radioGroup);
        fixed = findViewById(R.id.fixed);
        hardly = findViewById(R.id.hardly);
        easily = findViewById(R.id.easily);

        modifyButton = findViewById(R.id.modify_event);

        // 화면에 표시된 캘린더뷰에서 선택한 날짜를 "이벤트시작날짜"로 지정
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startDate = (year) + "." + (month+1) + "." + dayOfMonth;
                mStartDateTextView.setText(startDate);
            }
        });

        // "이벤트시작시간"은 현재시간이 default로 설정되며, 이후에 자유롭게 설정 가능
        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        ScheduleModificationForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        startTime = String.format("%02d:%02d ", hourOfDay, minute) + amPm;
                        mStartTimeTextView.setText(startTime);
                    }
                }, currentHour, currentMinute, false);
                dialog.show();
            }
        });

        // "이벤트종료날짜"를 클릭하여 mDateSetListener에 저장하고, 이를 종료 날짜를 직접 지정
        mEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        ScheduleModificationForm.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate = (year) + "." + (month+1) + "." + dayOfMonth;
                mEndDateTextView.setText(endDate);
            }
        };

        // "이벤트시작시간"은 (현재시간+1시간)이 default로 설정되며, 이후에 자유롭게 설정 가능
        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(
                        ScheduleModificationForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        endTime = String.format("%02d:%02d ", hourOfDay, minute) + amPm;
                        mEndTimeTextView.setText(endTime);
                    }
                }, currentHour+1, currentMinute, false);
                dialog.show();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            // 누군가 나를 호출했다면,
            mScheduleID = intent.getLongExtra("id", -1);
            TITLE = intent.getStringExtra("title");
            PLACE = intent.getStringExtra("place");
            MEMO = intent.getStringExtra("memo");
            PRIORITY = intent.getStringExtra("priority");
            START_DATE = intent.getStringExtra("startDate");
            START_TIME = intent.getStringExtra("startTime");
            END_DATE = intent.getStringExtra("endDate");
            END_TIME = intent.getStringExtra("endTime");

            radioGroup.clearCheck();
            if (PRIORITY.equals(fixed.getText().toString())) {
                radioGroup.check(R.id.fixed);
            } else if (PRIORITY.equals(hardly.getText().toString())) {
                radioGroup.check(R.id.hardly);
            } else if (PRIORITY.equals(easily.getText().toString())) {
                radioGroup.check(R.id.easily);
            }

            mTitleEditText.setText(TITLE);
            mPlaceEditText.setText(PLACE);
            mMemoEditText.setText(MEMO);
            mStartDateTextView.setText(START_DATE);
            mStartTimeTextView.setText(START_TIME);
            mEndDateTextView.setText(END_DATE);
            mEndTimeTextView.setText(END_TIME);

            // 없던 부분 시작
            TITLE = mTitleEditText.getText().toString();
            PLACE = mPlaceEditText.getText().toString();
            MEMO = mMemoEditText.getText().toString();
            if (fixed.isChecked()) {
                priority = fixed.getText().toString();
            } else if (hardly.isChecked()) {
                priority = hardly.getText().toString();
            } else if (easily.isChecked()) {
                priority = easily.getText().toString();
            }
            PRIORITY = priority;
            START_DATE = mStartDateTextView.getText().toString();
            START_TIME = mStartTimeTextView.getText().toString();
            END_DATE = mEndDateTextView.getText().toString();
            END_TIME = mEndTimeTextView.getText().toString();
            // 없던 부분 끝
        }
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int modified = scheduleController.modifySchedule(TITLE, PLACE, MEMO, PRIORITY, START_DATE, START_TIME, END_DATE, END_TIME, mScheduleID);
                if (modified == -1) {
                    Toast.makeText(ScheduleModificationForm.this, "수정에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
                } else if (modified == 0) {
                    Toast.makeText(ScheduleModificationForm.this, "일정이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                }
                // modifyButtonClicked();
                onBackPressed();
            }
        });
    }

    public void modifyButtonClicked() {
        String TITLE = mTitleEditText.getText().toString();
        String PLACE = mPlaceEditText.getText().toString();
        String MEMO = mMemoEditText.getText().toString();
        if (fixed.isChecked()) {
            priority = fixed.getText().toString();
        } else if (hardly.isChecked()) {
            priority = hardly.getText().toString();
        } else if (easily.isChecked()) {
            priority = easily.getText().toString();
        }
        String PRIORITY = priority;
        String START_DATE = mStartDateTextView.getText().toString();
        String START_TIME = mStartTimeTextView.getText().toString();
        String END_DATE = mEndDateTextView.getText().toString();
        String END_TIME = mEndTimeTextView.getText().toString();
        // SQLite에 저장하는 기본적인 방법 = ContentValues라는 객체를 만들어 거기에 담아서 DB에 저장
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_TITLE, TITLE);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PLACE, PLACE);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_MEMO, MEMO);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PRIORITY, PRIORITY);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_DATE, START_DATE);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_TIME, START_TIME);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_DATE, END_DATE);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_TIME, END_TIME);
        // DB에 작성할 것이기 때문에 WritableDatabase
        SQLiteDatabase db = ScheduleDbHelper.getInstance(this).getWritableDatabase();
        // 수정인 경우, count = 수정된 row의 개수
        int count = db.update(ScheduleContract.ScheduleEntry.TABLE_NAME, contentValues,
                ScheduleContract.ScheduleEntry._ID + " = " + mScheduleID, null);
        if (count == 0) {
            Toast.makeText(this, "수정에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "일정이 수정되었습니다", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
        // onBackPressed();
    }
}
