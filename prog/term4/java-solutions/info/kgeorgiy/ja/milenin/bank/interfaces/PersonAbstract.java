package info.kgeorgiy.ja.milenin.bank.interfaces;

import info.kgeorgiy.ja.milenin.bank.local.LocalAccount;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;

public abstract class PersonAbstract implements Person {


    private final String firstName;
    private final String secondName;
    private final String passportNumber;
    private final HashMap<String, LocalAccount> allAccounts;

    /**
     * Constructor of this class;
     *
     * @param firstName      first name
     * @param secondName     second name
     * @param passportNumber passport number
     * @param allAccounts    accounts of person
     */
    public PersonAbstract(String firstName,
                          String secondName,
                          String passportNumber, HashMap<String, LocalAccount> allAccounts) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.passportNumber = passportNumber;
        this.allAccounts = allAccounts;
    }


    /**
     * @return first name;
     */
    @Override
    public String getFirstName() throws RemoteException {
        return firstName;
    }

    /**
     * @return second name;
     */
    @Override
    public String getSecondName() throws RemoteException {
        return secondName;
    }

    /**
     * @return passport number;
     */
    @Override
    public String getPassportNumber() throws RemoteException {
        return passportNumber;
    }

    /**
     * @return set of accounts;
     */
    @Override
    public Set<String> getAllAccounts() throws RemoteException {
        return allAccounts.keySet();
    }

    /**
     * @param accountNumber number of found account
     * @return {@link Account} by {@link String accountNumber} or {@code null} if account with that number doesn't exist;
     */
    @Override
    public Account getAccount(String accountNumber) throws RemoteException {
        return (accountNumber == null) ? null : allAccounts.get(accountNumber);
    }
}

