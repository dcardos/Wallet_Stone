import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wallet {
    /**
     * Uma wallet pode possuir vários cartões (motivo disso tudo)
     * Uma wallet só pode pertencer a um user
     * O limite máximo de uma wallet deve ser a soma de todos os cartões dela
     * O usuário pode setar o limite real da wallet, desde que não ultrapasse o limite máximo
     * Ter a capacidade de adicionar ou remover um cartão a qualquer momento
     * O user deve ser capaz de acessar as informações de sua wallet a qualquer momento (limite setado pelo o user, limite máximo e crédito disponível)
     * Executar a ação de uma compra de determinado valor de acordo com as prioridades citadas anteriormente
     */
    private static final Logger LOGGER = Logger.getLogger(CreditCard.class.getName());
    private final User mUser;                       // only one user associated with it (cannot change)
    private final List<CreditCard> mCreditCards;    // never return this list! NEVER!!! (mutable)
    private final CurrencyUnit mCurrencyUnit = Monetary.getCurrency("BRL");
    private MonetaryAmount mMaxLimit;
    private MonetaryAmount mUserLimit;
    private MonetaryAmount mTotalAvailableCredit;
    private MonetaryAmount mUserAvailableCredit;    // used for buying

    public Wallet(User user, CreditCard creditCard, MonetaryAmount userLimit) {
        mCreditCards = new ArrayList<>();
        mUser = user;
        // setting next values based on the credit card - initializing monetary members
        mMaxLimit = creditCard.getLimit();
        mTotalAvailableCredit = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(0).create();
        mUserAvailableCredit = Monetary.getDefaultAmountFactory().setCurrency(mCurrencyUnit).setNumber(0).create();
        if (!creditCard.getCurrencyUnit().equals(mCurrencyUnit)) {
            throw new IllegalArgumentException("The credid card currency unit has to be: " + mCurrencyUnit);
        }
        mCreditCards.add(creditCard);
        mTotalAvailableCredit = mTotalAvailableCredit.add(creditCard.getCredit());
        mUserAvailableCredit = mTotalAvailableCredit;   // so far is the same as the total credit
        if (userLimit.isGreaterThan(mMaxLimit)) {
            throw new IllegalArgumentException("Tried to set a limit more than permitted");
        }
        mUserLimit = userLimit;
        // making sure that no buying will be made more than the user limit
        mUserAvailableCredit = mUserAvailableCredit.isGreaterThan(mUserLimit) ? mUserLimit : mUserAvailableCredit;
    }

    public boolean addCreditCard(CreditCard creditCard) {
        if (mCreditCards.contains(creditCard)) {    // credit card already added
            LOGGER.log(Level.INFO, "Credit card already in wallet");
            return false;
        } else if (!creditCard.getCurrencyUnit().equals(mCurrencyUnit)) {
            LOGGER.log(Level.INFO, "The credid card currency unit has to be: " + mCurrencyUnit);
            return false;
        } else {
            mCreditCards.add(creditCard);
            mMaxLimit = mMaxLimit.add(creditCard.getLimit());
            mTotalAvailableCredit = mTotalAvailableCredit.add(creditCard.getCredit());
            mUserAvailableCredit = mUserAvailableCredit.add(creditCard.getCredit());
            // making sure that no buying will be made more than the user limit
            mUserAvailableCredit = mUserAvailableCredit.isGreaterThan(mUserLimit) ? mUserLimit : mUserAvailableCredit;
            return true;
        }
    }

    // TODO: everyday check is there is a need to remove a card that expired (and its implications)
    public boolean removeCreditCard(CreditCard creditCard) {
        if (!mCreditCards.contains(creditCard)) {
            LOGGER.log(Level.INFO, "Tried to remove a card that was not in the wallet");
            return false;
        } else if (mCreditCards.remove(creditCard)) {  // credit card removed
            mMaxLimit = mMaxLimit.subtract(creditCard.getLimit());
            if (mUserLimit.isGreaterThan(mMaxLimit)) {    // if user limit became more than maximum
                setUserLimit(mMaxLimit); // then change it to the new maximum value
            }
            mTotalAvailableCredit = mTotalAvailableCredit.subtract(creditCard.getCredit());
            mUserAvailableCredit = mTotalAvailableCredit;    // re-setting user credit
            mUserAvailableCredit = mUserAvailableCredit.isGreaterThan(mUserLimit) ? mUserLimit : mUserAvailableCredit;
            return true;
        } else {
            LOGGER.log(Level.WARNING, "Could not remove the credit card!");
            return false;
        }
    }

    public boolean setUserLimit(MonetaryAmount userLimit) {
        if (userLimit.isNegative() || userLimit.isGreaterThan(mMaxLimit)) {
            LOGGER.log(Level.INFO, "Tried to set a limit more than permitted or to a negative value");
            return false;
        } else {
            mUserLimit = userLimit;
            // making sure that no buying will be made more than the user limit
            mUserAvailableCredit = mTotalAvailableCredit;   // re-setting user available credit
            mUserAvailableCredit = mUserAvailableCredit.isGreaterThan(mUserLimit) ? mUserLimit : mUserAvailableCredit;
            return true;
        }
    }

    public String getUserName() {
        return mUser.getName();
    }

    public MonetaryAmount getMaxLimit() {
        return mMaxLimit;
    }

    public MonetaryAmount getUserLimit() {
        return mUserLimit;
    }

    public MonetaryAmount getTotalAvailableCredit() {
        return mTotalAvailableCredit;
    }

    public MonetaryAmount getUserAvailableCredit() {
        return mUserAvailableCredit;
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append("Wallet of ").append(getUserName())
                .append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        output.append("Maximum limit: ").append(getMaxLimit()).append(System.getProperty("line.separator"));
        output.append("Limit set by you: ").append(getUserLimit()).append(System.getProperty("line.separator"));
        output.append("Total available credit: ").append(getTotalAvailableCredit())
                .append(System.getProperty("line.separator"));
        output.append("Available credit according to your predefined limit: ")
                .append(getUserAvailableCredit()).append(System.getProperty("line.separator"));
        output.append(System.getProperty("line.separator"));
        output.append("Cards: ").append(System.getProperty("line.separator"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");    // date print pattern
        for (CreditCard cc : mCreditCards) {
            output.append("Last 4 numbers: ").append(cc.getNumber());
            output.append(", expiration date: ").append(fmt.print(cc.getExpiration()));
            output.append(", limit: ").append(cc.getLimit());
            output.append(", credit available: ").append(cc.getCredit());
            output.append(System.getProperty("line.separator"));
        }
        return output.toString();
    }
}
