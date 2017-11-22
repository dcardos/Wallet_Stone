import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class WalletTest {
    private CurrencyUnit mCurrencyUnit = Monetary.getCurrency("BRL");
    private MonetaryAmount amount2k = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(2000).create();
    private MonetaryAmount amount1k = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(1000).create();
    private MonetaryAmount amount5c = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(500).create();
    private CreditCard mCreditCard = new CreditCard("2332", 10, new LocalDate("2021-10-01"),
            "Danilo", "221", amount2k);
    private Wallet mWallet;
    @Before
    public void setUp() throws Exception {
        mWallet = new Wallet(new User("Danilo", "220.220.290.2"), mCreditCard, amount1k);
    }

    @Test
    public void checkinConstructorAndGetMethods() {
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k));
        assertTrue(mWallet.getUserLimit().isEqualTo(amount1k));
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(amount2k));
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(amount1k));
        assertTrue(mWallet.getUserName().equals("Danilo"));
    }

    @Test
    public void addCreditCard() throws Exception {
        CreditCard anotherCard = new CreditCard("4558", 15,
                new LocalDate("2020-09-21"), "Murilo", "123", amount1k);
        mWallet.addCreditCard(anotherCard);
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k.add(amount1k)));
        assertTrue(mWallet.getUserLimit().isEqualTo(amount1k));
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(amount2k.add(amount1k)));
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(amount1k));
        // increasing user limit by 500
        assertTrue(mWallet.setUserLimit(amount1k.add(amount5c)));
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k.add(amount1k)));  // nothing changed
        assertTrue(mWallet.getUserLimit().isEqualTo(amount1k.add(amount5c)));
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(amount2k.add(amount1k))); // nothing changed
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(amount1k.add(amount5c)));
        // trying to add a limit more than permitted
        assertFalse(mWallet.setUserLimit(amount2k.add(amount2k)));
        // decreasing user limit by 1500
        assertTrue(mWallet.setUserLimit(amount5c));
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k.add(amount1k)));  // nothing changed
        assertTrue(mWallet.getUserLimit().isEqualTo(amount5c));
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(amount2k.add(amount1k))); // nothing changed
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(amount5c));
        // adding the same credit card twice
        assertFalse(mWallet.addCreditCard(anotherCard));
        // changing a bit, but is the same credit card trying to be added (same number)
        anotherCard = anotherCard.changeCvv("999");
        anotherCard = anotherCard.changeName("Murilo");
        anotherCard = anotherCard.changePayDay(1);
        assertFalse(mWallet.addCreditCard(anotherCard));
    }

    @Test
    public void removeCreditCard() {
        CreditCard anotherCard1k = new CreditCard("9988", 15,
                new LocalDate("2020-09-21"), "Danilo", "123", amount1k);
        CreditCard anotherCard2k5c = new CreditCard("3316", 25,
                new LocalDate("2029-01-21"), "Danilo", "963", amount2k.add(amount5c));
        // adding cards
        assertTrue(mWallet.addCreditCard(anotherCard1k));
        assertTrue(mWallet.addCreditCard(anotherCard2k5c));
        // removing one card
        assertTrue(mWallet.removeCreditCard(anotherCard1k));
        // trying to remove a card that is not there
        assertFalse(mWallet.removeCreditCard(anotherCard1k));
        // checking new limits and credits
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k.add(amount2k).add(amount5c)));
        assertTrue(mWallet.getUserLimit().isEqualTo(amount1k)); // did not changed
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(amount2k.add(amount2k).add(amount5c)));
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(amount1k)); // did not changed
        // setting user limit to be the maximum value
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k.add(amount2k.add(amount5c))));
        assertTrue(mWallet.setUserLimit(mWallet.getMaxLimit()));
        // removing another cc
        assertTrue(mWallet.removeCreditCard(anotherCard2k5c));
        // checking new limits and credits (all have to be the new maximum)
        assertTrue(mWallet.getMaxLimit().isEqualTo(amount2k));
        assertTrue(mWallet.getUserLimit().isEqualTo(amount2k));
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(amount2k));
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(amount2k));
        // removing last credit card
        assertTrue(mWallet.removeCreditCard(mCreditCard));
        // checking new limits and credits (all have to be 0)
        MonetaryAmount zero = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(0).create();
        assertTrue(mWallet.getMaxLimit().isEqualTo(zero));
        assertTrue(mWallet.getUserLimit().isEqualTo(zero));
        assertTrue(mWallet.getTotalAvailableCredit().isEqualTo(zero));
        assertTrue(mWallet.getUserAvailableCredit().isEqualTo(zero));
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorLimit() throws Exception {
        // trying to create a wallet with forbidden user limit
        Wallet wallet = new Wallet(new User("Livia", "223.233.233.90"),
                new CreditCard("2233", 10, new LocalDate("2021-10-22"), "Livia", "233", amount1k),
                amount2k);
    }

}