package comp2026.OctopusCard.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

public class DateTimeUtil {
    private static final String DateFormat = "dd-MM-yyyy@HH:mm:ss";

    public static String dateTime2Str(Date date) {
        // fixme: How to use SimpleDateFormat to convert a Date object to a String?
        // fixme: Date format: dd-mm-yyyy@hh:mm:ss
        DateFormat dateFormatted = new SimpleDateFormat(DateFormat);
        return dateFormatted.format(date);
    }

    public static Date str2DateTime(String dateTimeStr) throws ParseException {
        // fixme: How to use SimpleDateFormat to convert a String to a Date object?
        // fixme: Date format: dd-mm-yyyy@hh:mm:ss
        Date date = new SimpleDateFormat(DateFormat).parse(dateTimeStr);
        return date;
    }
}
