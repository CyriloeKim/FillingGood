package com.example.fillinggoodwithdb.Database;

import android.provider.BaseColumns;

// Schedule Table 정보를 담을 계약 클래스
public class ScheduleContract {

    // private 생성자
    private ScheduleContract() {};

    // 상수(String)들을 정의해서 가져다 쓰는 용도
    public static class ScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "schedules";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_MEMO = "memo";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_DATE = "endDate";
        public static final String COLUMN_NAME_END_TIME = "endTime";
    }
}
