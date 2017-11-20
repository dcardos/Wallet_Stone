import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        BigDecimal limit = new BigDecimal("123.22");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "31-08-1982";
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CreditCard creditCard = new CreditCard("123", date, date, "Danilo", "223", limit);
        BigDecimal newValue = limit.add(new BigDecimal("500.50"));
        System.out.println(creditCard.getLimit());
        System.out.println(newValue);
        System.out.println(sdf.format(creditCard.getPayDay()));
    }
}
