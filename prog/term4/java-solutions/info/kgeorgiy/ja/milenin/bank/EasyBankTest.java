package info.kgeorgiy.ja.milenin.bank;

import info.kgeorgiy.ja.milenin.bank.interfaces.Bank;
import info.kgeorgiy.ja.milenin.bank.remote.RemoteBank;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class EasyBankTest {
    private static final int MAXIMUM_VALUE_PERSONS_ACCOUNTS = 500;
    private static final int MAIN_PORT = 8888;
    private static final int REGISTRY_PORT = 1099;
    private static Bank bank;

    @BeforeClass
    public static void before() throws RemoteException, MalformedURLException {
        // :NOTE: вынести порты
        bank = new RemoteBank(MAIN_PORT);
        LocateRegistry.createRegistry(REGISTRY_PORT);
        Naming.rebind("//localhost/bank", bank);
    }

    @org.junit.Test
    public void test_creatingPerson() throws RemoteException {
        final String PASSPORT_NUMBER = "test_creatingPerson";
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNotNull(bank.createRemotePerson("Vasya", "Pupkin", PASSPORT_NUMBER));
        assertNotNull(bank.getRemotePerson(PASSPORT_NUMBER));
    }

    @org.junit.Test
    public void test_creatingPersonHard() throws RemoteException {
        for (int passInt = 0; passInt < MAXIMUM_VALUE_PERSONS_ACCOUNTS; passInt++) {
            String PASSPORT_NUMBER = "test_creatingPersonHard_" + passInt;
            assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
            assertNotNull(bank.createRemotePerson("Vasya", "Pupkin", PASSPORT_NUMBER));
            assertNotNull(bank.getRemotePerson(PASSPORT_NUMBER));
        }

    }

    @org.junit.Test
    public void test_creatingSamePerson() throws RemoteException {
        final String PASSPORT_NUMBER = "test_creatingSamePerson";
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNotNull(bank.createRemotePerson("Vasya_1", "Pupkin_1", PASSPORT_NUMBER));
        assertNotNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNull(bank.createRemotePerson("Vasya_2", "Pupkin_2", PASSPORT_NUMBER));
    }

    @org.junit.Test
    public void test_creatingAccount() throws RemoteException {
        final String PASSPORT_NUMBER = "test_creatingAccount", ACCOUNT_ID = "1";
        createPersonCheck(PASSPORT_NUMBER, ACCOUNT_ID);
    }

    private void createPersonCheck(String PASSPORT_NUMBER, String ACCOUNT_ID) throws RemoteException {
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNotNull(bank.createRemotePerson("Vasya", "Pupkin", PASSPORT_NUMBER));
        assertNull(bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
        assertNotNull(bank.createAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
        assertNotNull(bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
    }

    @org.junit.Test
    public void test_setAccount() throws RemoteException {
        final String PASSPORT_NUMBER = "test_setAccount", ACCOUNT_ID = "2";
        createPersonCheck(PASSPORT_NUMBER, ACCOUNT_ID);
        assertEquals(0, bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).getAmount());
    }

    @org.junit.Test
    public void test_multiAccounts() throws RemoteException {
        final String PASSPORT_NUMBER = "test_multiAccounts";
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNotNull(bank.createRemotePerson("Vasya", "Pupkin", PASSPORT_NUMBER));
        createMultiAccounts(PASSPORT_NUMBER);
    }

    @org.junit.Test
    public void test_clientActions() throws RemoteException {
        final String PASSPORT_NUMBER = "test_clientActions", ACCOUNT_ID = "1";
        final int valueSet = 129;
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNotNull(bank.createRemotePerson("Vasya", "Pupkin", PASSPORT_NUMBER));
        assertNull(bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
        assertNotNull(bank.createAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
        assertEquals(0, bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).getAmount());
        bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).setAmount(valueSet);
        assertEquals(valueSet, bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).getAmount());
    }

    @org.junit.Test
    public void test_multiAccountsSet() throws RemoteException {
        final String PASSPORT_NUMBER = "test_multiAccountsSet";
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        assertNotNull(bank.createRemotePerson("Vasya", "Pupkin", PASSPORT_NUMBER));
        createMultiAccounts(PASSPORT_NUMBER);
        for (int i = 0; i < MAXIMUM_VALUE_PERSONS_ACCOUNTS; i++) {
            String ACCOUNT_ID = Integer.toString(i);
            assertEquals(0, bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).getAmount());
            bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).setAmount(i);
            assertEquals(i, bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).getAmount());
        }
    }

    @Test
    public void test_clientNullArgs() throws RemoteException {
        final String PASSPORT_NUMBER = "test_clientNullArgs";
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        Client.main(null);
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
    }

    @Test
    public void test_clientCorrectArgs1() throws RemoteException {
        final String PASSPORT_NUMBER = "test_clientCorrectArgs1_67676";
        int delta = 200;
        String accountNumber = "5469";
        String[] args = {"Vasya", "Pupkin", PASSPORT_NUMBER, accountNumber, Integer.toString(delta)};
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        Client.main(args);
        accountCheck(PASSPORT_NUMBER, accountNumber, delta);
    }

    @Test
    public void test_clientCorrectArgs2() throws RemoteException {
        final String PASSPORT_NUMBER = "test_clientCorrectArgs2_67676";
        int delta = 200;
        String accountNumber = "5469";
        String[] args = {"Vasya", "Pupkin", PASSPORT_NUMBER, accountNumber, Integer.toString(delta)};
        assertNull(bank.getRemotePerson(PASSPORT_NUMBER));
        Client.main(args);
        accountCheck(PASSPORT_NUMBER, accountNumber, delta);

        delta = 300;
        args[4] = Integer.toString(delta);
        Client.main(args);
        accountCheck(PASSPORT_NUMBER, accountNumber, delta);
    }

    // :NOTE: многопоточные тесты
    @Test
    public void test_multiThreadAnotherPerson2() {
        ExecutorService bankService = Executors.newFixedThreadPool(2);
        bankService.submit(() -> createPerson("Ivan1",
                "Milenin1",
                "test_multiThreadAnotherPerson2_1",
                "acc_1"));
        bankService.submit(() -> createPerson("Ivan2",
                "Milenin2",
                "test_multiThreadAnotherPerson2_2",
                "acc_2"));
        bankService.shutdown();
    }

    @Test
    public void test_multiThreadAnotherPersonMax() throws InterruptedException {
        ExecutorService bankService = Executors.newFixedThreadPool(2);
        List<Callable<Void>> calls = new ArrayList<>();
        IntStream.range(0, MAXIMUM_VALUE_PERSONS_ACCOUNTS).forEach(i ->
                calls.add(() -> {
                    createPerson("Ivan",
                            "Milenin",
                            "test_multiThreadAnotherPersonMax_" + i,
                            "acc_1");
                    return null;
                }));
        bankService.invokeAll(calls);
    }

    @Test
    public void test_multiThreadOnePersonGettingAccount() throws InterruptedException, RemoteException {
        ExecutorService bankService = Executors.newFixedThreadPool(2);
        String passportNumber = "test_multiThreadOnePersonGettingAccount_1";
        String accId = "acc_1";
        createPerson("Ivan",
                "Milenin",
                passportNumber,
                accId);
        List<Callable<Void>> calls = new ArrayList<>();
        IntStream.range(0, MAXIMUM_VALUE_PERSONS_ACCOUNTS).forEach(i ->
                calls.add(() -> {
                    assertNotNull(bank.getRemotePerson(passportNumber));
                    assertNotNull(bank.getAccount(accId, bank.getRemotePerson(passportNumber)));
                    bank.getAccount(accId, bank.getRemotePerson(passportNumber)).setAmount(i);
                    return null;
                }));
        bankService.invokeAll(calls);
        assertNotEquals(0, bank.getAccount(accId, bank.getRemotePerson(passportNumber)).getAmount());
    }


    private void createPerson(String firstName, String secondName, String passportNumber, String accountNumber) {
        try {
            assertNull(bank.getRemotePerson(passportNumber));
            assertNotNull(bank.createRemotePerson(firstName, secondName, passportNumber));
            assertNotNull(bank.getRemotePerson(passportNumber));
            assertNull(bank.getAccount(accountNumber, bank.getRemotePerson(passportNumber)));
            assertNotNull(bank.createAccount(accountNumber, passportNumber));
            assertNotNull(bank.getAccount(accountNumber, bank.getRemotePerson(passportNumber)));
        } catch (RemoteException ignored) {
        }
    }

    private void accountCheck(String passportNumber, String accountNumber, int delta) throws RemoteException {
        assertNotNull(bank.getRemotePerson(passportNumber));
        assertNotNull(bank.getAccount(accountNumber, bank.getRemotePerson(passportNumber)));
        assertEquals(delta, bank.getAccount(accountNumber, bank.getRemotePerson(passportNumber)).getAmount());
    }

    private void createMultiAccounts(String PASSPORT_NUMBER) throws RemoteException {
        for (int accountId = 0; accountId < MAXIMUM_VALUE_PERSONS_ACCOUNTS; accountId++) {
            String ACCOUNT_ID = Integer.toString(accountId);
            assertNull(bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
            assertNotNull(bank.createAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
            assertNotNull(bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)));
            assertEquals(0, bank.getAccount(ACCOUNT_ID, bank.getRemotePerson(PASSPORT_NUMBER)).getAmount());
        }
    }

}
