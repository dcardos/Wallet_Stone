import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.*;

public class CreditCardTest {
    private CreditCard mCreditCard;
    @Before
    public void setUp() throws Exception {
        mCreditCard = new CreditCard("123", 23, new GregorianCalendar(2021, Calendar.JANUARY,31),
                "Danilo", "223", new BigDecimal("2333.99"));
    }

    @Test
    public void getMethods() {
        assertTrue(mCreditCard.getNumber().equals("123"));
        assertTrue(mCreditCard.getPayDay() == 23);
        assertTrue(mCreditCard.getMonthOfExpiration() == Calendar.JANUARY);
        assertTrue(mCreditCard.getYearOfExpiration() == 2021);
        assertTrue(mCreditCard.getName().equals("Danilo"));
        assertTrue(mCreditCard.getCvv().equals("223"));
        assertTrue(mCreditCard.getLimit().compareTo(new BigDecimal("2333.99")) == 0);
    }

    @Test
    public void equals() {
        CreditCard sameCreditCard = new CreditCard("123", 23, new GregorianCalendar(2021,0,31),
                "Danilo", "223", new BigDecimal("2333.99"));
        CreditCard alsoTheSameCC = new CreditCard("123", 23, new GregorianCalendar(2021,0,31),
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
                new GregorianCalendar(2021,0,1));
        assertTrue(mCreditCard.equals(ccWithAnotherExpirationDay));
        assertEquals(mCreditCard.hashCode(), ccWithAnotherExpirationDay.hashCode());

        // not equal for any other change
        CreditCard ccDifferent = mCreditCard.changeNumber("321");
        assertFalse(mCreditCard.equals(ccDifferent));
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changePayDay(1);
        assertFalse(mCreditCard.equals(ccDifferent));
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changeExpirationDay(new GregorianCalendar(2022,0,31));
        assertFalse(mCreditCard.equals(ccDifferent));   // changed year of expiration date
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changeExpirationDay(new GregorianCalendar(2021,1,31));
        assertFalse(mCreditCard.equals(ccDifferent));   // changed month of expiration date
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent= mCreditCard.changeName("Livia das Lives");
        assertFalse(mCreditCard.equals(ccDifferent));
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());

        ccDifferent = mCreditCard.changeCvv("221");
        assertFalse(mCreditCard.equals(ccDifferent));
        assertFalse(mCreditCard.hashCode() == ccDifferent.hashCode());
    }

    @Test
    public void immutable() {
        // No test required here
        // All fields are immutable except for Calendar (mExpirationDate), so DO NOT return it in a get method!
    }
}