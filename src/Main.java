import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        BigDecimal limit = new BigDecimal("123.22");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

        LocalDate calendar = new LocalDate(2013,1,31);
        CreditCard creditCard = new CreditCard("123", 22, calendar, "Danilo", "223", limit);

        HashSet<CreditCard> creditCards = new HashSet<>();
        creditCards.add(creditCard);
        CreditCard anotherCreditCard = new CreditCard("123", 10, calendar, "Danilo", "223", new BigDecimal("2333.99"));
        System.out.println(creditCard.equals(anotherCreditCard));
        System.out.println(creditCards.contains(anotherCreditCard));
        System.out.println(creditCards.size());
        System.out.println(creditCards.add(anotherCreditCard));
        System.out.println(creditCards.size());

        LocalDate nextPayDay = CreditCard.nextPayDate(creditCard);
        System.out.println(fmt.print(nextPayDay));
    }
}
