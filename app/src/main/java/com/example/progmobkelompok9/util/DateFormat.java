package com.example.progmobkelompok9.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateFormat {

    public String IDN(Calendar myCalendar){

        String myFormat = "yyyy-MM-dd"; //In which you need put here

        SimpleDateFormat formatOutgoing = new SimpleDateFormat(myFormat);
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        formatOutgoing.setTimeZone(tz);
        String s = formatOutgoing.format(myCalendar.getTime());
        return s;
    }



}
