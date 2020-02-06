package comq.example.raymond.bloan.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BLoansUtils {
    public static String dateFromLong(long time){
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US);
        return format.format(new Date(time));
    }
}