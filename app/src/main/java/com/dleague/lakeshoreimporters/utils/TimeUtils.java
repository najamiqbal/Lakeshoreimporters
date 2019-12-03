package com.dleague.lakeshoreimporters.utils;

import org.joda.time.DateTime;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy - hh h mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
//    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEE MMM dd yyyy hh:mm aa");
    /**
     * @param timeInMilliSeconds1 epoch time format in milliseconds given parameter to compare with 1st parameter
     * @param timeInMilliSeconds2 epoch time format in milliseconds given parameter to compare with 1st parameter
     * @return 0, 1, 2 0 when both are equal 1 when calling object time is greater 2 when given parameter is greater
     */

    public int compareTimeStamps(String timeInMilliSeconds1, String timeInMilliSeconds2) {
        BigInteger bigInteger1 = new BigInteger(timeInMilliSeconds1);
        BigInteger bigInteger2 = new BigInteger(timeInMilliSeconds2);
        return bigInteger1.compareTo(bigInteger2);
    }

    public String getDate(long timeInMilliSeconds) {
        //Here you set to your timezone
        DateTime dateTime = new DateTime(timeInMilliSeconds*1000L);
        return dateTime.toString("MM/dd/yyyy");
    }

    public String getDateTime(long timeInMilliSeconds) {
        //Here you set to your timezone
        DateTime dateTime = new DateTime(timeInMilliSeconds*1000L);
        return dateTime.toString("MM/dd/yyyy - hh:mm");
    }

    public String getDateForNext7weeks(long timeInMilliSeconds) {
           //Here you set to your timezone
           DateTime dateTime = new DateTime(timeInMilliSeconds*1000L);
           dateTime = dateTime.plusWeeks(7);
           return dateTime.toString("MM/dd/yyyy");
       }

    public String getTime(String timeInMilliSeconds) {
        //Here you set to your timezone
        timeFormat.setTimeZone(TimeZone.getDefault());
        BigInteger bigInteger1 = new BigInteger(timeInMilliSeconds);
        Date timestamp = new Date(bigInteger1.longValue());
        return timeFormat.format(timestamp.getTime());
    }

    public String getDateTime(String timeInMilliSeconds){
        dateTimeFormat.setTimeZone(TimeZone.getDefault());
        BigInteger bigInteger1 = new BigInteger(timeInMilliSeconds);
        Date timestamp = new Date(bigInteger1.longValue());
        return dateTimeFormat.format(timestamp.getTime());
    }

    public DateTime getDateTimeObj(long timeInMilliSeconds){
        DateTime dateTime = new DateTime(timeInMilliSeconds * 1000L);
        return dateTime;
    }

    public int addShareTimeinCurrentTime(int index){
        Calendar now = Calendar.getInstance();
        if(index == 1) {
            now.add(Calendar.MINUTE, 15);
        }else if(index == 2){
            now.add(Calendar.MINUTE, 30);
        }else if(index == 3){
            now.add(Calendar.HOUR, 1);
        }else if(index == 4){
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        int dateInSeconds = (int)(now.getTimeInMillis()/1000);
        return dateInSeconds;
    }
}
