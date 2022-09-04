package info.kgeorgiy.ja.milenin.bank;

import info.kgeorgiy.ja.milenin.bank.interfaces.Bank;
import info.kgeorgiy.ja.milenin.bank.remote.RemoteBank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


/**
 * Server class of {@link Bank};
 *
 * @author Georgiy Korneev (and Milenin Ivan);
 */
public final class Server {
    private final static int DEFAULT_PORT = 8888;

    /**
     * Start server with {@link Bank};
     */
    public static void main(final String... args) throws RemoteException {

        final Bank bank = new RemoteBank(DEFAULT_PORT);
        try {
            //UnicastRemoteObject.exportObject(bank, 1099);
            LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/bank", bank);
            System.out.println("Server started");
        } catch (final RemoteException e) {
            System.out.println("Cannot export object: " + e.getMessage());
            e.printStackTrace();
            //System.exit(1);
        } catch (final MalformedURLException e) {
            System.out.println("Malformed URL");
        }
    }
}
