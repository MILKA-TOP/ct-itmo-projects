package info.kgeorgiy.ja.milenin.bank.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Account extends Remote {
    /**
     * Returns account identifier.
     */
    String getId() throws RemoteException;

    /**
     * Returns amount of money at the account.
     */
    int getAmount() throws RemoteException;

    /**
     * Sets amount of money at the account.
     */
    void setAmount(int amount) throws RemoteException;
}