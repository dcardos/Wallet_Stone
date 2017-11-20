import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        BigDecimal limit = new BigDecimal("123.22");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar calendar = new GregorianCalendar(2013,0,31);
        CreditCard creditCard = new CreditCard("123", 23, calendar, "Danilo", "223", limit);

        HashSet<CreditCard> creditCards = new HashSet<>();
        creditCards.add(creditCard);
        CreditCard anotherCreditCard = new CreditCard("123", 23, calendar, "Danilo", "223", new BigDecimal("2333.99"));
        System.out.println(creditCard.equals(anotherCreditCard));
        System.out.println(creditCards.contains(anotherCreditCard));
        System.out.println(creditCards.size());
        System.out.println(creditCards.add(anotherCreditCard));
        System.out.println(creditCards.size());
    }
}
