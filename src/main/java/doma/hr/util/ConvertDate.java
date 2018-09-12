package doma.hr.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertDate {

    public static Date gmttoLocalDate(Date date) {

        String timeZone = TimeZone.getTimeZone("Europe/Zagreb").getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    public static Date serverToZagreb() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Zagreb"));
        Date gmt = new Date(sdf.format(date));
        return gmt;
    }
}
