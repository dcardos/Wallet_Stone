import java.math.BigDecimal;
import java.util.Calendar;

public final class CreditCard { // immutable class - thread safe and less error-prone
    /**  Possuir as propriedades necessárias para realizar uma compra
     *   (número, data de vencimento, data de validade, nome impresso, cvv e limite)
    */
    private final String mNumber;        // last digit is a verifier one - no count needed (String)
    private final int mPayDay;
    private final Calendar mExpiration;
    private final String mName;
    private final String mCvv;
    private final BigDecimal mLimit;      // do not use float or double for currency, why?
                                    // https://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency


    public CreditCard(String number, int payDay, Calendar expiration, String name, String cvv, BigDecimal limit) {
        mNumber = number;
        // validate that cents is 0 to 99
        if (payDay < 0 || payDay > 28) {
            throw new IllegalArgumentException("Invalid pay day value: " + payDay + ". Must be positive and less than 29");
        }
        mPayDay = payDay;
        mExpiration = expiration;
        mName = name;
        mCvv = cvv;
        mLimit = limit;
    }

    public String getNumber() {
        return mNumber;
    }

    public int getPayDay() {
        return mPayDay;
    }

    public int getMonthOfExpiration() {
        return mExpiration.get(Calendar.MONTH);
    }

    public int getYearOfExpiration() {
        return mExpiration.get(Calendar.YEAR);
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

    //Accessor functions for mutable objects (correcting wrong values)
    public CreditCard changeNumber(String newNumber) {
        return new CreditCard(newNumber, mPayDay, mExpiration, mName, mCvv, mLimit);
    }

    public CreditCard changePayDay(int newPayDay) {
        return new CreditCard(mNumber, newPayDay, mExpiration, mName, mCvv, mLimit);
    }

    public CreditCard changeExpirationDay(Calendar newExpiration) {
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

    @Override
    public boolean equals(Object obj) {
        //check for self-comparison
        if ( this == obj ) return true;

        //use instanceof instead of getClass here for two reasons
        //1. if need be, it can match any supertype, and not just one class;
        //2. it renders an explict check for "that == null" redundant, since
        //it does the check for null already - "null instanceof [type]" always
        //returns false. (See Effective Java by Joshua Bloch.)
        if ( !(obj instanceof CreditCard) ) return false;
        //Alternative to the above line :
        //if ( aThat == null || aThat.getClass() != this.getClass() ) return false;

        //cast to native object is now safe
        CreditCard that = (CreditCard) obj;

        //now a proper field-by-field evaluation can be made
        return  (mNumber.equals(that.mNumber)) &&
                (mPayDay == that.mPayDay) &&
                (mExpiration.get(Calendar.MONTH) == that.mExpiration.get(Calendar.MONTH)) &&
                (mExpiration.get(Calendar.YEAR) == that.mExpiration.get(Calendar.YEAR)) &&
                (mName.equals(that.mName)) &&
                (mCvv.equals(that.mCvv));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mNumber.hashCode();
        result = 31 * result + mPayDay;
        result = 31 * result + mExpiration.get(Calendar.MONTH);
        result = 31 * result + mExpiration.get(Calendar.YEAR);
        result = 31 * result + mName.hashCode();
        result = 31 * result + mCvv.hashCode();
        return result;
    }
}
