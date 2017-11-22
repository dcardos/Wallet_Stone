import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreditCard implements Comparable<CreditCard>{ // immutable class - thread safe and less error-prone
    /**  Possuir as propriedades necessárias para realizar uma compra
     *   (número, data de vencimento, data de validade, nome impresso, cvv e limite)
    */
    private static final Logger LOGGER = Logger.getLogger(CreditCard.class.getName());
    private final String mNumber;        // last digit is a verifier one - no count needed (String)
    private final int mPayDay;
    private final LocalDate mExpiration;
    private final String mName;
    private final String mCvv;
    private final BigDecimal mLimit;      // do not use float or double for currency, why?
    // https://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency
    private BigDecimal mCredit;         // Attention: this field is mutable (using buy and payment method)!


    public CreditCard(String number, int payDay, LocalDate expiration, String name, String cvv, BigDecimal limit) {
        // TODO: check lenght of the credit card number and use the verification digit
        mNumber = number;
        if (payDay < 0 || payDay > 28) {
            throw new IllegalArgumentException("Invalid pay day value: " + payDay + ". Must be positive and less than 29");
        }
        mPayDay = payDay;
        mExpiration = expiration;
        if (mExpiration.isBefore(LocalDate.now())) {
            LOGGER.log(Level.WARNING, "Credit card already expired");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name cannot be 0 in length");
        }
        mName = name;
        if (cvv.length() < 3 || cvv.length() > 4) {
            throw new IllegalArgumentException("cvv has to be 3 or 4 digits");
        }
        mCvv = cvv;
        // TODO: validate that cents is 0 to 99
        if (limit.compareTo(new BigDecimal("0")) <= 0) { // negative or 0 value
            throw new IllegalArgumentException("limit has to be greater than 0");
        }
        mLimit = limit;
        mCredit = limit;
    }

    public String getNumber() {
        return mNumber;
    }

    public int getPayDay() {
        // TODO: change according to holidays and weekends
        return mPayDay;
    }

    public int getMonthOfExpiration() {
        return mExpiration.getMonthOfYear();
    }

    public int getYearOfExpiration() {
        return mExpiration.getYear();
    }

    public String getName() {
        return mName;
    }

    public String getCvv() {
        return mCvv;
    }

    public BigDecimal getLimit() {
        return mLimit;
    }

    public BigDecimal getCredit() {
        return mCredit;
    }

    //Accessor functions for mutable objects (correcting wrong values)
    public CreditCard changeNumber(String newNumber) {
        return new CreditCard(newNumber, mPayDay, mExpiration, mName, mCvv, mLimit);
    }

    public CreditCard changePayDay(int newPayDay) {
        return new CreditCard(mNumber, newPayDay, mExpiration, mName, mCvv, mLimit);
    }

    public CreditCard changeExpirationDay(LocalDate newExpiration) {
        return new CreditCard(mNumber, mPayDay, newExpiration, mName, mCvv, mLimit);
    }

    public CreditCard changeName(String newName) {
        return new CreditCard(mNumber, mPayDay, mExpiration, newName, mCvv, mLimit);
    }

    public CreditCard changeCvv(String newCvv) {
        return new CreditCard(mNumber, mPayDay, mExpiration, mName, newCvv, mLimit);
    }

    public CreditCard changeLimit(BigDecimal newLimit) {
        return new CreditCard(mNumber, mPayDay, mExpiration, mName, mCvv, newLimit);
    }

    public boolean buyProduct(BigDecimal value) {
        if (value.signum() == -1 || value.compareTo(mCredit) > 0)
            return false;   // value is negative or there is no enough credit to make the payment
        else {
            mCredit = mCredit.subtract(value);
            return true;
        }
    }

    public boolean makePayment(BigDecimal value) {
        if (value.signum() == -1) {
            return false;   // value is negative
        }
        mCredit = mCredit.add(value);   // accepting payment such as credit can be greater than limit
        return true;
    }

    /**
     * Você prefere usar o cartão que está mais longe de vencer porque terá mais tempo para pagar a conta.
     * Caso os dois cartões vençam no mesmo dia, você prefere usar aquele que tem menor limite para continuar tendo um cartão com o limite mais alto.
     * Lembre-se que cada compra é feita em apenas um cartão, então manter um cartão com limite mais alto te dá liberdade de fazer compras grandes.
     */
    @Override
    public int compareTo(CreditCard other) {
        // calculating next payday date
        LocalDate thisPayDayDate = nextPayDate(this);
        LocalDate otherPayDayDate = nextPayDate(other);

        if (thisPayDayDate.equals(otherPayDayDate)) {           // if they are the same
            return mCredit.compareTo(other.mCredit);           // credit card with lesser limit gets priority
        } else {
            return -thisPayDayDate.compareTo(otherPayDayDate);   // no tie here
        }
    }

    @Override
    public boolean equals(Object obj) {
        //check for self-comparison
        if ( this == obj ) return true;

        // use instanceof instead of getClass here for two reasons
        // 1. if need be, it can match any supertype, and not just one class;
        // 2. it renders an explict check for "that == null" redundant, since
        // it does the check for null already - "null instanceof [type]" always
        // returns false. (See Effective Java by Joshua Bloch.)
        if ( !(obj instanceof CreditCard) ) return false;
        // Alternative to the above line :
        // if ( aThat == null || aThat.getClass() != this.getClass() ) return false;

        // cast to native object is now safe
        CreditCard that = (CreditCard) obj;

        // now a proper field-by-field evaluation can be made
        // two credit cards are equals if they have the same number
        return  (mNumber.equals(that.mNumber));
    }

    // always override the hashcode when overriding equals
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mNumber.hashCode();
        return result;
    }

    public static LocalDate nextPayDate(CreditCard creditCard) {
        LocalDate today = LocalDate.now();
        LocalDate payDayThisMonth = new LocalDate(today.getYear(), today.getMonthOfYear(), creditCard.getPayDay());
        if (today.compareTo(payDayThisMonth) > 0) {             // today is a day past the pay day, so...
            payDayThisMonth = payDayThisMonth.plusMonths(1);    // ... pay day has to be set for the next month
        }
        return payDayThisMonth; // returning the new date for the next pay day (this month or the next)
    }
}
