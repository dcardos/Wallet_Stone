import java.math.BigDecimal;
import java.util.Date;

public class CreditCard {
    /*  Possuir as propriedades necessárias para realizar uma compra
        (número, data de vencimento, data de validade, nome impresso, cvv e limite)
    */
    private String mNumber;        // last digit is a verifier one - no count needed (String)
    private Date mPayDay;
    private Date mExpiration;
    private String mName;
    private String mCvv;
    private BigDecimal mLimit;      // do not use float or double for currency, why?
                                    // https://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency


    public CreditCard(String number, Date payDay, Date expiration, String name, String cvv, BigDecimal limit) {
        mNumber = number;
        mPayDay = payDay;
        mExpiration = expiration;
        mName = name;
        mCvv = cvv;
        mLimit = limit;
    }

    public String getNumber() {
        return mNumber;
    }

    public Date getPayDay() {
        return mPayDay;
    }

    public Date getExpiration() {
        return mExpiration;
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
}
