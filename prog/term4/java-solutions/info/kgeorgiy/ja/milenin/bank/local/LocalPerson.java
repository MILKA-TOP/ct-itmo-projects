package info.kgeorgiy.ja.milenin.bank.local;

import info.kgeorgiy.ja.milenin.bank.interfaces.PersonAbstract;

import java.io.Serializable;
import java.util.HashMap;

public class LocalPerson extends PersonAbstract implements Serializable {


    /**
     * Constructor of this class;
     *
     * @param firstName      first name
     * @param secondName     second name
     * @param passportNumber passport number
     * @param allAccounts    accounts of person
     */
    public LocalPerson(String firstName,
                       String secondName,
                       String passportNumber, HashMap<String, LocalAccount> allAccounts) {
        super(firstName, secondName, passportNumber, allAccounts);
    }

}
