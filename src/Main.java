import org.joda.time.LocalDate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public class Main {

    public static void main(String[] args) {
        // creating credit cards, note that for currency there is the JSR354: Currency and Money API
        // for dates there is the joda-time API
        // those API deal with a lof of precision errors when used built in Java APIs!
        // Choosing currency
        CurrencyUnit mCurrencyUnit = Monetary.getCurrency("BRL");
        // Setting amounts of money (remember the money is a immutable object)
        MonetaryAmount amount5k = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(5000).create();
        MonetaryAmount amount2k = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(2000).create();
        MonetaryAmount amount1k = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(1000).create();
        MonetaryAmount amount5c = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(500).create();
        MonetaryAmount amount1c = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(100).create();
        MonetaryAmount zero = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(0).create();
        // creating credit cards
        CreditCard cc0 = new CreditCard("2332", 10,
                new LocalDate("2021-10-01"),"Danilo", "221", amount2k); // limit = $2000
        CreditCard cc1 = new CreditCard("22256", 10,
                new LocalDate("2020-11-21"), "Livia das Lives", "223", amount1k.add(amount5c)); // limit = $1500
        CreditCard cc2 = new CreditCard("22257", 20,
                new LocalDate("2021-11-21"), "Murilo", "654", amount1k.add(amount5c)); // limit = $1500
        CreditCard cc3 = new CreditCard("444444", 10,
                new LocalDate("2022-11-21"), "Marcela", "943", amount5c); // limit = $500
        CreditCard cc4 = new CreditCard("444499", 20,
                new LocalDate("2023-11-21"), "Carol", "789", amount2k.add(amount5c)); // limit = $2500
        CreditCard cc5 = new CreditCard("555555", 28,
                new LocalDate("2024-11-21"), "Nivaldo", "852", amount1c); // limit = $100
        // creating a new wallet
        // for that I had to create a user (with a name and CPF), add one credit card and the my user limit
        // (less than the credit card I just use as the parameter)
        Wallet myWallet = new Wallet(new User("Danilo", "220.220.290.2"), cc0, amount1k);
        // adding the other credit cards
        myWallet.addCreditCard(cc1);
        myWallet.addCreditCard(cc2);
        myWallet.addCreditCard(cc3);
        myWallet.addCreditCard(cc4);
        myWallet.addCreditCard(cc5);
        // setting a new user limit
        myWallet.setUserLimit(amount5k);    // now it is $5000 (out of $8100)
        // the user can always see the waller info just printing it
        System.out.println(myWallet);
        // now let's buy something!
        myWallet.buy(amount1k); // buying a product of $1000
        // it will automatic use the priority as specified, to check, you can print the wallet again
        // not that the order of the credit cards now match the priority!
        System.out.println(myWallet);
        // to remove a card
        myWallet.removeCreditCard(cc3);
        // feel free to explore and test. =)
    }
}
