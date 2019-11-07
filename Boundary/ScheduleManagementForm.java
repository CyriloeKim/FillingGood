package com.example.fillinggoodwithdb.Boundary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggoodwithdb.Control.ScheduleController;
import com.example.fillinggoodwithdb.Database.ScheduleContract;
import com.example.fillinggoodwithdb.Database.ScheduleDbHelper;
import com.example.fillinggoodwithdb.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Act as MainActivity, context -> schedule_main.xml
public class ScheduleManagementForm extends AppCompatActivity {

    public static final int REQUEST_CODE_INSERT = 1000;
    private ScheduleAdapter mAdapter;
    public ScheduleController scheduleController = new ScheduleController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_main);

        // schedule_main에서 + 플로팅버튼을 눌렀을 때 schedule_add로 이동하는 코드
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB에 저장되는 데이터들을 실시간으로 보여주도록 코드 변경
                startActivityForResult(new Intent(ScheduleManagementForm.this, ScheduleAdditionForm.class),
                        REQUEST_CODE_INSERT);
            }
        });

        ListView listView = findViewById(R.id.schedule_list);

        // DB에 있는 schedule data를 가져오는 코드
        Cursor cursor = getScheduleCursor();

        // Cursor로 받은 것을 ListView에 뿌려주려면 CursorAdapter가 필요
        mAdapter = new ScheduleAdapter(this, cursor);
        listView.setAdapter(mAdapter);

        // 리스트에서 아이템을 짧게 누르면 그 아이템(일정)을 불러오도록 하는 코드
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScheduleManagementForm.this, ScheduleModificationForm.class);
                Cursor cursor = (Cursor) mAdapter.getItem(position);

                scheduleController.readSchedule(intent, cursor, id);
                // 수정이 일어날 수 있기 때문에, 수정된 마지막 데이터를 보여주기 위해서 아래와 같은 코드 작성
                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        // 리스트에서 아이템을 길게 누르면 그 아이템을 삭제하도록 하는 코드
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long deleteId = id;
                // 삭제를 위해서는 id 필요
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleManagementForm.this);
                builder.setTitle("일정 삭제");
                builder.setMessage("일정을 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = ScheduleDbHelper.getInstance(ScheduleManagementForm.this).getWritableDatabase();
                        int deletedCount = db.delete(ScheduleContract.ScheduleEntry.TABLE_NAME,
                                ScheduleContract.ScheduleEntry._ID + " = " + deleteId, null);
                        if (deletedCount == 0) {
                            Toast.makeText(ScheduleManagementForm.this, "삭제에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            // 삭제된 것을 반영해 최신내용으로 갱신
                            mAdapter.swapCursor(getScheduleCursor());
                            Toast.makeText(ScheduleManagementForm.this, "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
                // 아이템 이벤트를 소비
                return true;
            }
        });

    }

    private Cursor getScheduleCursor() {
        ScheduleDbHelper dbHelper = ScheduleDbHelper.getInstance(this);
        return dbHelper.getReadableDatabase()
                .query(ScheduleContract.ScheduleEntry.TABLE_NAME,
                        null, null, null, null, null, null);
        // 최근 저장된 순서로 보려면 마지막 인수를 orderBy: ScheduleContract.ScheduleEntry._ID + " DESC"
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK) {
            // 성공한 경우이며, 이때는 데이터를 갱신
            mAdapter.swapCursor(getScheduleCursor());
        }
    }

    private static class ScheduleAdapter extends CursorAdapter {

        public ScheduleAdapter(Context context, Cursor c) { super(context, c, false); }

        // Layout 부분
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // 데이터를 실제로 뿌려주는 부분
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleText = view.findViewById(android.R.id.text1);
            titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_TITLE)));
        }
    }
}
