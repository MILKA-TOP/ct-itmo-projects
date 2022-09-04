package info.kgeorgiy.ja.milenin.bank;

import info.kgeorgiy.ja.milenin.bank.interfaces.Account;
import info.kgeorgiy.ja.milenin.bank.interfaces.Bank;
import info.kgeorgiy.ja.milenin.bank.interfaces.Person;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


/**
 * Client class, which try to create Person in the {@link Bank} and set amount to his account;
 *
 * @author Milenin_Ivan;
 */
public final class Client {


    /**
     * Run client, which who try to create Person in the {@link Bank} and set amount to his account;
     *
     * @param args - [First name] [Second name] [Passport number] [Account ID] [Value]
     */
    public static void main(final String[] args) throws RemoteException {
        final Bank bank;

        if (args == null || args.length < 5) {
            System.err.println("Input args: [First name] [Second name] [Passport number] [Account ID] [Value]");
            return;
        }
        for (int i = 0; i < 5; i++) {
            if (args[i] == null) {
                System.err.println("Input non-null arguments");
                return;
            }
        }
        String firstName = args[0], secondName = args[1], passportNumber = args[2], accountId = args[3];
        int value;
        try {
            value = Integer.parseInt(args[4]);
        } catch (final NumberFormatException e) {
            return;
        }
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            System.err.println("Bank is not bound: " + e.getMessage());
            return;
        } catch (final MalformedURLException e) {
            // :NOTE: e.getMessage()
            System.err.println("Bank URL is invalid: " + e.getMessage());
            return;
        }


        Person person = bank.getRemotePerson(passportNumber);
        if (person == null) {
            person = bank.createRemotePerson(firstName, secondName, passportNumber);
        }


        Account personAccount = bank.getAccount(accountId, person);
        if (personAccount == null) {
            bank.createAccount(accountId, person);
        }

        System.out.println("Value before setting: " + bank.getAccount(accountId, person).getAmount());
        bank.getAccount(accountId, person).setAmount(value);
        System.out.println("Value after setting: " + bank.getAccount(accountId, person).getAmount());
    }
}
