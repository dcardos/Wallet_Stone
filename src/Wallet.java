import java.math.BigDecimal;
import java.util.ArrayList;

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
    private final User mUser; // only one user associated with it (cannot change)
    private final ArrayList<CreditCard> mCreditCards;
    private BigDecimal mMaxLimit;
    private BigDecimal mUserLimit;
    private BigDecimal mAvailableCredit;

    public Wallet(User user, CreditCard creditCard, BigDecimal userLimit) {
        mUser = user;
        addCreditCard(creditCard);
        mCreditCards = new ArrayList<>();
        if (!setUserLimit(userLimit)) {
            throw new IllegalArgumentException("User Limit is bigger than the maximum limit of " + mMaxLimit);
        }
    }

    public void addCreditCard(CreditCard creditCard) {
        mMaxLimit = mMaxLimit.add(creditCard.getLimit());
        mAvailableCredit = mAvailableCredit.add(creditCard.getCredit());
    }

    public boolean setUserLimit(BigDecimal userLimit) {
        if (userLimit.compareTo(mMaxLimit) > 0)
            return false;
        else {
            mUserLimit = userLimit;
            return true;
        }
    }
}
