package redditreader.com.redditreader_android.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Math.floor;

public class TimestampHelper {
    public static String readTimestamp(int timestamp){
        String time;
        Date posted = new Date(timestamp*1000);
        Date now = new Date(System.currentTimeMillis());
        Long gap = ChronoUnit.MILLIS.between(posted.toInstant(), now.toInstant());
        Date diff = new Date(gap);

        if ((diff.getSeconds() <= 0 || diff.getSeconds() > 0) && (diff.getMinutes() == 0 || diff.getMinutes() > 0) && diff.getHours() == 0) {
            if(diff.getMinutes() > 0 && diff.getHours() == 0){
                time = diff.getMinutes()+"m";
            }else{
                time = "Now";
            }
        } else if(diff.getHours() > 0 && diff.getDay() == 0){
            time = diff.getHours()+"h";
        }else if (diff.getDay() > 0 && diff.getDay() < 7) {
            if (diff.getDay() == 1) {
                time = diff.getDay() + "d";
            } else {
                time = diff.getDay() + "d";
            }
        } else if (diff.getDay() >=7 && diff.getDay() < 365) {
            if (diff.getDay() == 1) {
                time = floor(diff.getDay() / 7) +"w";
            } else {
                time = floor(diff.getDay() / 7) + "w";
            }
        }else {
            if (diff.getDay() == 365) {
                time = floor(diff.getDay() / 365) + "y";
            } else {
                time = floor(diff.getDay() / 365) + "y";
            }
        }

        return time;
    }
}
