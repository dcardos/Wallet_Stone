import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public class Main {

    public static void main(String[] args) {
        final CurrencyUnit brl = Monetary.getCurrency("BRL");
        MonetaryAmount limit = Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(123.22).create();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

        LocalDate calendar = new LocalDate(2013,1,31);
        CreditCard creditCard = new CreditCard("123", 22, calendar, "Danilo", "223", limit);

        System.out.println(creditCard.getCredit());
    }
}
