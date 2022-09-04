package info.kgeorgiy.ja.milenin.bank.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    /**
     * Creates a new account with specified identifier if it is not already exists.
     *
     * @param id     account id;
     * @param person {@link Person} information;
     * @return created account or {@code null}.
     */
    Account createAccount(String id, Person person) throws RemoteException;

    /**
     * Creates a new account with specified identifier if it is not already exists.
     *
     * @param id             account id;
     * @param passportNumber passportNumber of {@link Person};
     * @return created account or {@code null}.
     */
    Account createAccount(String id, String passportNumber) throws RemoteException;

    /**
     * Returns account by identifier.
     *
     * @param id     account id;
     * @param person {@link Person} information;
     * @return account with specified identifier or {@code null} if such account does not exists.
     */
    Account getAccount(String id, Person person) throws RemoteException;

    /**
     * Create RemotePerson without accounts;
     * @param firstName first name;
     * @param secondName second name;
     * @param passportNumber passport number;
     * @return created {@link Person} or {@code null} if such person is in the base or person's information incorrect.
     */
    Person createRemotePerson(String firstName,
                              String secondName,
                              String passportNumber) throws RemoteException;

    /**
     * Returns LocalPerson by passport number.
     *
     * @param passportNumber passportNumber of {@link Person};
     * @return LocalPerson or {@code null} if such person does not exists or {@link String passportNumber} incorrect.
     */
    @SuppressWarnings("unused")
    Person getLocalPerson(String passportNumber) throws RemoteException;

    /**
     * Returns RemotePerson by passport number.
     *
     * @param passportNumber passportNumber of {@link Person};
     * @return RemotePerson or {@code null} if such person does not exists or {@link String passportNumber} incorrect.
     */
    Person getRemotePerson(String passportNumber) throws RemoteException;

}
