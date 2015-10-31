package com.wafflestudio.siksha.util;

import android.content.Context;

import java.util.Calendar;

public class Date {
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        String dayOfWeek = "";
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour >= 20)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "(일)";
                break;
            case 2:
                dayOfWeek = "(월)";
                break;
            case 3:
                dayOfWeek = "(화)";
                break;
            case 4:
                dayOfWeek = "(수)";
                break;
            case 5:
                dayOfWeek = "(목)";
                break;
            case 6:
                dayOfWeek = "(금)";
                break;
            case 7:
                dayOfWeek = "(토)";
                break;
        }

        return (calendar.get(Calendar.MONTH) + 1) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일 " + dayOfWeek;
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static String getTimeSlot(int position) {
        String timeSlot = "";

        switch (position) {
            case 0:
                timeSlot = "아침";
                break;
            case 1:
                timeSlot = "점심";
                break;
            case 2:
                timeSlot = "저녁";
                break;
        }

        return timeSlot;
    }

    public static int getTimeSlotIndex() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour <= 9 || hour >= 21)
            return 0;
        else if (hour >= 10 && hour <= 14)
            return 1;
        else
            return 2;
    }

    public static int getTimeSlotIndexForWidget(Context context, int appWidgetID) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour <= 9 || hour >= 21) {
            if (Preference.loadBooleanValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_BREAKFAST_PREFIX + appWidgetID))
                return 0;
            else
                return 1;
        }
        else if (hour >= 10 && hour <= 14)
            return 1;
        else
            return 2;
    }

    public static String getRefreshTimestamp() {
        return getDate() + " " + getHour() + ":" + getMinute();
    }
}
