package info.kgeorgiy.ja.milenin.bank.remote;

import info.kgeorgiy.ja.milenin.bank.interfaces.Account;
import info.kgeorgiy.ja.milenin.bank.interfaces.Bank;
import info.kgeorgiy.ja.milenin.bank.interfaces.Person;
import info.kgeorgiy.ja.milenin.bank.local.LocalAccount;
import info.kgeorgiy.ja.milenin.bank.local.LocalPerson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank extends UnicastRemoteObject implements Bank {
    private final int port;
    // passport  - List<passport:subId>
    private final ConcurrentMap<String, HashSet<String>> accountsPassport = new ConcurrentHashMap<>();
    // passport:subId - Account
    private final ConcurrentMap<String, Account> accountsSubId = new ConcurrentHashMap<>();
    // passport - Person
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();

    /**
     * Constructor of {@link RemoteBank};
     *
     * @param port port for remote connection;
     */
    public RemoteBank(int port) throws RemoteException {
        super(port);
        this.port = port;
    }

    /**
     * Creates a new account with specified identifier if it is not already exists.
     *
     * @param id     account id;
     * @param person {@link Person} information;
     * @return created account or {@code null}.
     */
    @Override
    public Account createAccount(final String id, Person person) throws RemoteException {
        if (id == null || person == null) return null;
        String personPassport = person.getPassportNumber();
        return createAccount(id, personPassport);

    }

    /**
     * Creates a new account with specified identifier if it is not already exists.
     *
     * @param id             account id;
     * @param passportNumber passportNumber of {@link Person};
     * @return created account or {@code null}.
     */
    @Override
    public Account createAccount(final String id, final String passportNumber) throws RemoteException {
        if (id == null || passportNumber == null) {
            return null;
        }
        Person person = persons.get(passportNumber);
        if (person == null) {
            return null;
        }
        System.out.println("Creating account with id: " + id);
        if (!persons.containsKey(passportNumber)) {
            person = createRemotePerson(person.getFirstName(), person.getSecondName(), passportNumber);
            if (person == null) {
                return null;
            }
        }
        String accountId = passportNumber + ":" + id;
        if (accountsSubId.containsKey(accountId)) {
            return null;
        }
        Account account = new RemoteAccount(id, port);
        System.out.println("PUSHED ID: " + accountId);
        accountsPassport.get(passportNumber).add(accountId);
        accountsSubId.put(accountId, account);
        return account;
    }

    /**
     * Returns account by identifier.
     *
     * @param id     account id;
     * @param person {@link Person} information;
     * @return account with specified identifier or {@code null} if such account does not exists.
     */
    @Override
    public Account getAccount(final String id, final Person person) throws RemoteException {
        System.out.println("Getting account with id: " + id);
        if (id == null || person == null) {
            return null;
        }
        Set<String> personAccounts = accountsPassport.get(person.getPassportNumber());
        if (personAccounts == null) {
            return null;
        }
        String accountId = person.getPassportNumber() + ":" + id;
        Account account = accountsSubId.get(accountId);
        if (account == null) {
            return null;
        }
        return (personAccounts.contains(accountId)) ? account : null;
    }

    /**
     * Returns LocalPerson by passport number.
     *
     * @param passportNumber passportNumber of {@link Person};
     * @return LocalPerson or {@code null} if such person does not exists or {@link String passportNumber} incorrect.
     */
    @Override
    public Person getLocalPerson(String passportNumber) throws RemoteException {
        System.out.println("Getting person with passportNumber: " + passportNumber);
        if (passportNumber == null) {
            return null;
        }
        Person foundRemotePerson = getRemotePerson(passportNumber);
        if (foundRemotePerson == null) {
            return null;
        }
        HashMap<String, LocalAccount> personAccounts = new HashMap<>();
        for (String nowAccountId : accountsPassport.get(passportNumber)) {
            Account nowAccount = accountsSubId.get(nowAccountId);
            personAccounts.put(nowAccountId, new LocalAccount(nowAccount.getId(), nowAccount.getAmount()));
        }
        return new LocalPerson(foundRemotePerson.getFirstName(), foundRemotePerson.getSecondName(), passportNumber, personAccounts);
    }

    /**
     * Returns RemotePerson by passport number.
     *
     * @param passportNumber passportNumber of {@link Person};
     * @return RemotePerson or {@code null} if such person does not exists or {@link String passportNumber} incorrect.
     */
    @Override
    public Person getRemotePerson(String passportNumber) throws RemoteException {
        System.out.println("Getting person with passportNumber: " + passportNumber);
        if (passportNumber == null) {
            return null;
        }
        return persons.get(passportNumber);
    }

    /**
     * Create RemotePerson without accounts;
     *
     * @param firstName      first name;
     * @param secondName     second name;
     * @param passportNumber passport number;
     * @return created {@link Person} or {@code null} if such person is in the base or person's information incorrect.
     */
    @Override
    public Person createRemotePerson(String firstName,
                                     String secondName,
                                     String passportNumber) throws RemoteException {
        System.out.println("Create person with secondName: " + secondName);
        if (firstName == null || secondName == null || passportNumber == null || persons.containsKey(passportNumber)) {
            return null;
        }
        Person createdPerson = new RemotePerson(firstName, secondName, passportNumber, new HashMap<>(), port);

        accountsPassport.put(passportNumber, new HashSet<>());
        persons.put(passportNumber, createdPerson);
        return createdPerson;
    }


}
