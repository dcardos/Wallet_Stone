import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.*;

public class CreditCardTest {
    private CreditCard mCreditCard;
    @Before
    public void setUp() throws Exception {
        mCreditCard = new CreditCard("123", 23, new LocalDate(2021, 1,31),
                "Danilo", "223", new BigDecimal("2333.99"));
    }

    @Test
    public void getMethods() {
        assertTrue(mCreditCard.getNumber().equals("123"));
        assertTrue(mCreditCard.getPayDay() == 23);
        assertTrue(mCreditCard.getMonthOfExpiration() == 1);
        assertTrue(mCreditCard.getYearOfExpiration() == 2021);
        assertTrue(mCreditCard.getName().equals("Danilo"));
        assertTrue(mCreditCard.getCvv().equals("223"));
        assertTrue(mCreditCard.getLimit().compareTo(new BigDecimal("2333.99")) == 0);
    }

    @Test
    public void equals() {
        CreditCard sameCreditCard = new CreditCard("123", 23, new LocalDate(2021,1,31),
                "Danilo", "223", new BigDecimal("2333.99"));
        CreditCard alsoTheSameCC = new CreditCard("123", 23, new LocalDate(2021,1,31),
                "Danilo", "223", new BigDecimal("2333.99"));
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
        CreditCard ccWithAnotherLimit = mCreditCard.changeLimit(new BigDecimal("1500.66"));
        assertTrue(mCreditCard.equals(ccWithAnotherLimit));
        assertEquals(mCreditCard.hashCode(), ccWithAnotherLimit.hashCode());

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
        BigDecimal productValue = new BigDecimal("99.09");
        BigDecimal oldCredit = mCreditCard.getCredit();
        assertTrue(mCreditCard.buyProduct(productValue));
        assertTrue(mCreditCard.getCredit().compareTo(oldCredit.subtract(productValue)) == 0);
        assertTrue(mCreditCard.makePayment(productValue));  // paying for the product
        assertTrue(mCreditCard.getCredit().compareTo(oldCredit) == 0);  // credit has to be the same

        // buying something equal to the limit value
        assertTrue(mCreditCard.buyProduct(mCreditCard.getLimit()));
        assertTrue(mCreditCard.getCredit().compareTo(new BigDecimal("0")) == 0);
        assertTrue(mCreditCard.makePayment(mCreditCard.getLimit()));  // paying for the product

        // buying something more than the credit available, should not be possible
        BigDecimal valueMoreThanLimit = mCreditCard.getLimit().add(new BigDecimal("0.01"));
        assertFalse(mCreditCard.buyProduct(valueMoreThanLimit));
        // trying again, now with split value
        assertTrue(mCreditCard.buyProduct(mCreditCard.getLimit())); // all credit used here
        assertFalse(mCreditCard.buyProduct(new BigDecimal("0.01")));    // no credit left
        assertTrue(mCreditCard.makePayment(mCreditCard.getLimit()));  // restoring the old credit value

        // corner cases
        assertFalse(mCreditCard.buyProduct(new BigDecimal("-1.00")));
        assertFalse(mCreditCard.makePayment(new BigDecimal("-1.00")));
    }

    @Test
    public void immutable() {
        // No test required here
        // Most fields are immutable except for Calendar (mExpirationDate), so DO NOT return it in a get method!
        // Also take careful when manipulating the credit card credit field (it is a mutable field)
    }

    // The test below will work only if you are after between the 21st and 28th of the month!
    // That is why I am commenting it
//    @Test
//    public void priorities() {
//        CreditCard cc1 = new CreditCard("22256", 10,
//                new LocalDate("2020-11-21"), "Danilo", "223", new BigDecimal("1500"));
//        CreditCard cc2 = new CreditCard("22257", 20,
//                new LocalDate("2020-11-21"), "Murilo", "223", new BigDecimal("1500"));
//        CreditCard cc3 = new CreditCard("444444", 10,
//                new LocalDate("2020-11-21"), "Marcela", "223", new BigDecimal("500"));
//        CreditCard cc4 = new CreditCard("444499", 20,
//                new LocalDate("2020-11-21"), "Rafaela", "223", new BigDecimal("2500"));
//        CreditCard cc5 = new CreditCard("555555", 28,
//                new LocalDate("2020-11-21"), "Nivaldo", "223", new BigDecimal("100"));
//
//        List<CreditCard> creditCards = new ArrayList<>();
//        creditCards.add(cc1);
//        creditCards.add(cc2);
//        creditCards.add(cc3);
//        creditCards.add(cc4);
//        creditCards.add(cc5);
//        Collections.sort(creditCards);
//
//        // following the priorities, those should be true:
//        assertEquals(creditCards.get(0), cc2);
//        assertEquals(creditCards.get(1), cc4);
//        assertEquals(creditCards.get(2), cc3);
//        assertEquals(creditCards.get(3), cc1);
//        assertEquals(creditCards.get(4), cc5);
//    }

}