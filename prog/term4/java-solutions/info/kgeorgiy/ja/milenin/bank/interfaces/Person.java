package info.kgeorgiy.ja.milenin.bank.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

@SuppressWarnings("unused")
public interface Person extends Remote {

    /**
     * Returns person's first name;
     */
    String getFirstName() throws RemoteException;

    /**
     * Returns person's second name;
     */
    String getSecondName() throws RemoteException;

    /**
     * Returns person's passport number;
     */
    String getPassportNumber() throws RemoteException;

    /**
     * Returns person's account with number {@link String accountNumber};
     *
     * @param accountNumber number of search account;
     */
    Account getAccount(String accountNumber) throws RemoteException;

    /**
     * Returns all person's accounts;
     */
    Set<String> getAllAccounts() throws RemoteException;

}
