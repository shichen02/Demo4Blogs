import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {


    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());
        System.out.println(date.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        System.out.println(format);


    }
}
