package info.kgeorgiy.ja.milenin.bank.interfaces;

import info.kgeorgiy.ja.milenin.bank.local.LocalAccount;

public abstract class AccountAbstract implements Account {
    private final String id;
    private int amount;

    /**
     * Constructor of {@link LocalAccount} with id;
     *
     * @param id     - account's id
     * @param amount - amount, which will be changed on this value;
     */
    @SuppressWarnings("unused")
    public AccountAbstract(final String id, final int amount) {
        this.id = id;
        this.amount = amount;
    }


    /**
     * @return account's id;
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return account's amount;
     */
    @Override
    public int getAmount() {
        System.out.println("Getting amount of money for account " + id);
        return amount;
    }

    /**
     * Set for this account {@link Integer amount}
     *
     * @param amount set this value;
     */
    @Override
    public void setAmount(final int amount) {
        System.out.println("Setting amount of money for account " + id);
        this.amount = amount;
    }
}
