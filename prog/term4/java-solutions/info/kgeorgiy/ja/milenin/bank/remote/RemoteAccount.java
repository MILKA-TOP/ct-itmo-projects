package info.kgeorgiy.ja.milenin.bank.remote;

import info.kgeorgiy.ja.milenin.bank.interfaces.AccountAbstract;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// :NOTE: copy paste
public class RemoteAccount extends AccountAbstract {

    /**
     * Constructor of {@link RemoteAccount}
     *
     * @param id   Account id
     * @param port port for remote connection;
     */
    public RemoteAccount(final String id, final int port) throws RemoteException {
        super(id, 0);
        UnicastRemoteObject.exportObject(this, port);
    }


}
