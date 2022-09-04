package info.kgeorgiy.ja.milenin.bank.remote;

import info.kgeorgiy.ja.milenin.bank.interfaces.PersonAbstract;
import info.kgeorgiy.ja.milenin.bank.interfaces.Person;
import info.kgeorgiy.ja.milenin.bank.local.LocalAccount;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RemotePerson extends PersonAbstract implements Person {

    /**
     * Constructor of {@link RemotePerson}
     *
     * @param firstName      first name;
     * @param secondName     second name;
     * @param passportNumber passport number;
     * @param allAccounts    map of all accounts;
     * @param port           port for remote connection;
     */
    public RemotePerson(String firstName,
                        String secondName,
                        String passportNumber, HashMap<String, LocalAccount> allAccounts, int port) throws RemoteException {
        super(firstName, secondName, passportNumber, allAccounts);
        UnicastRemoteObject.exportObject(this, port);

    }

}
