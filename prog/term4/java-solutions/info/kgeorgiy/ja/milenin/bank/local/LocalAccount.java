package info.kgeorgiy.ja.milenin.bank.local;

import info.kgeorgiy.ja.milenin.bank.interfaces.AccountAbstract;

import java.io.Serializable;

public class LocalAccount extends AccountAbstract implements Serializable {

    /**
     * Constructor of {@link LocalAccount} with id;
     *
     * @param id - account's id
     */
    @SuppressWarnings("unused")
    public LocalAccount(final String id) {
        super(id, 0);
    }

    /**
     * Constructor of {@link LocalAccount} with id and amount;
     *
     * @param id     - account's id;
     * @param amount - amount, which will be changed on this value;
     */
    public LocalAccount(final String id, final int amount) {
        super(id, amount);
    }


}
