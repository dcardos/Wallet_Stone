import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.*;

public class CreditCardTest {
    private CurrencyUnit brl = Monetary.getCurrency("BRL");
    private MonetaryAmount limit = Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(2000).create();
    private CreditCard mCreditCard;
    @Before
    public void setUp() throws Exception {
        mCreditCard = new CreditCard("123", 23, new LocalDate(2021, 1,31),
                "Danilo", "223", limit);
    }

    @Test
    public void checkinConstructorAndGetMethods() {
        assertTrue(mCreditCard.getNumber().equals("123"));
        assertTrue(mCreditCard.getPayDay() == 23);
        assertTrue(mCreditCard.getMonthOfExpiration() == 1);
        assertTrue(mCreditCard.getYearOfExpiration() == 2021);
        assertTrue(mCreditCard.getName().equals("Danilo"));
        assertTrue(mCreditCard.getCvv().equals("223"));
        assertTrue(mCreditCard.getLimit().isEqualTo(Monetary.getDefaultAmountFactory().
                setCurrency(mCreditCard.getCurrencyUnit()).setNumber(2000).create()));
    }

    @Test
    public void equals() {
        CreditCard sameCreditCard = new CreditCard("123", 23, new LocalDate(2021,1,31),
                "Danilo", "223", limit);
        CreditCard alsoTheSameCC = new CreditCard("123", 23, new LocalDate(2021,1,31),
                "Danilo", "223", limit);
        // reflexive property
        assertTrue(mCreditCard.equals(mCreditCard));

        // symmetric property
        assertTrue(mCreditCard.equals(sameCreditCard) == sameCreditCard.equals(mCreditCard));
        assertEquals(mCreditCard.hashCode(), sameCreditCard.hashCode());

        // transitive property
        if (mCreditCard.equals(sameCreditCard) && sameCreditCard.equals(alsoTheSameCC)) {
            assertTrue(mCreditCard.equals(alsoTheSameCC));
            assertEquals(mCreditCard.hashCode(), alsoTheSameCC.hashCode());
        }

        // consistency property
        assertTrue(mCreditCard.equals(sameCreditCard) == mCreditCard.equals(sameCreditCard));

        // non-null property
        assertFalse(mCreditCard.equals(null));

        // should also be equal no matter the limit or day of the expiration date
        CreditCard ccWithAnotherExpirationDay = mCreditCard.changeExpirationDay(
                new LocalDate(2021,1,1));
        assertTrue(mCreditCard.equals(ccWithAnotherExpirationDay));
        assertEquals(mCreditCard.hashCode(), ccWithAnotherExpirationDay.hashCode());

        // different number should imply in different cards
        CreditCard ccDifferent = mCreditCard.changeNumber("321");
        assertFalse(mCreditCard.equals(ccDifferent));
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());

        // although this is an inconsistency, if a card has the same number is the same card
        // no matter the other fields
        ccDifferent = mCreditCard.changePayDay(1);
        assertTrue(mCreditCard.equals(ccDifferent));
        assertTrue(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changeExpirationDay(new LocalDate(2022,1,31));
        assertTrue(mCreditCard.equals(ccDifferent));   // changed year of expiration date
        assertTrue(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changeExpirationDay(new LocalDate(2021,2,28));
        assertTrue(mCreditCard.equals(ccDifferent));   // changed month of expiration date
        assertTrue(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent= mCreditCard.changeName("Livia das Lives");
        assertTrue(mCreditCard.equals(ccDifferent));
        assertTrue(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changeCvv("221");
        assertTrue(mCreditCard.equals(ccDifferent));
        assertTrue(mCreditCard.hashCode() == ccDifferent.hashCode());
    }

    @Test
    public void manipulatingCredit() {
        // buy something less than limit and checking credit after
        MonetaryAmount productValue = Monetary.getDefaultAmountFactory().
                setCurrency(mCreditCard.getCurrencyUnit()).setNumber(99.09).create();
        MonetaryAmount oldCredit = mCreditCard.getCredit();
        assertTrue(mCreditCard.buyProduct(productValue));
        assertTrue(mCreditCard.getCredit().isEqualTo(oldCredit.subtract(productValue)));
        assertTrue(mCreditCard.makePayment(productValue));  // paying for the product
        assertTrue(mCreditCard.getCredit().isEqualTo(oldCredit));  // credit has to be the same

        // buying something equal to the limit value
        assertTrue(mCreditCard.buyProduct(mCreditCard.getLimit()));
        assertTrue(mCreditCard.getCredit().isZero());
        assertTrue(mCreditCard.makePayment(mCreditCard.getLimit()));  // paying for the product

        // buying something more than the credit available, should not be possible
        MonetaryAmount valueMoreThanLimit = mCreditCard.getLimit().add(Monetary.getDefaultAmountFactory().
                setCurrency(mCreditCard.getCurrencyUnit()).setNumber(0.01).create());
        assertFalse(mCreditCard.buyProduct(valueMoreThanLimit));
        // trying again, now with split value
        assertTrue(mCreditCard.buyProduct(mCreditCard.getLimit())); // all credit used here
        assertFalse(mCreditCard.buyProduct(Monetary.getDefaultAmountFactory().
                setCurrency(mCreditCard.getCurrencyUnit()).setNumber(0.01).create()));    // no credit left
        assertTrue(mCreditCard.makePayment(mCreditCard.getLimit()));  // restoring the old credit value

        // corner cases
        assertFalse(mCreditCard.buyProduct(Monetary.getDefaultAmountFactory().
                setCurrency(mCreditCard.getCurrencyUnit()).setNumber(-0.01).create()));
        assertFalse(mCreditCard.makePayment(Monetary.getDefaultAmountFactory().
                setCurrency(mCreditCard.getCurrencyUnit()).setNumber(-1.01).create()));
    }

    @Test
    public void immutable() {
        // No test required here
        // Most fields are immutable except for Calendar (mExpirationDate), so DO NOT return it in a get method!
        // Also take careful when manipulating the credit card credit field (it is a mutable field)
    }

    // The test below will work only if you are after between the 21st and 28th of the month!
    // That is why I am commenting it
    @Test
    public void priorities() {
        CreditCard cc1 = new CreditCard("22256", 10,
                new LocalDate("2020-11-21"), "Danilo", "223",
                Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(1500).create());
        CreditCard cc2 = new CreditCard("22257", 20,
                new LocalDate("2020-11-21"), "Murilo", "223",
                Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(1500).create());
        CreditCard cc3 = new CreditCard("444444", 10,
                new LocalDate("2020-11-21"), "Marcela", "223",
                Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(500).create());
        CreditCard cc4 = new CreditCard("444499", 20,
                new LocalDate("2020-11-21"), "Rafaela", "223",
                Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(2500).create());
        CreditCard cc5 = new CreditCard("555555", 28,
                new LocalDate("2020-11-21"), "Nivaldo", "223",
                Monetary.getDefaultAmountFactory().setCurrency(brl).setNumber(100).create());

        List<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(cc1);
        creditCards.add(cc2);
        creditCards.add(cc3);
        creditCards.add(cc4);
        creditCards.add(cc5);
        Collections.sort(creditCards);

        // following the priorities, those should be true:
        assertEquals(creditCards.get(0), cc2);
        assertEquals(creditCards.get(1), cc4);
        assertEquals(creditCards.get(2), cc3);
        assertEquals(creditCards.get(3), cc1);
        assertEquals(creditCards.get(4), cc5);
    }

}