package rmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConversionImpl extends UnicastRemoteObject implements IConversion {

    private static final double TAUX_EUR_TND = 3.40; // taux fixe

    public ConversionImpl() throws RemoteException {
        super();
    }

    @Override
    public double convertirMontant(double mt) throws RemoteException {
        return mt * TAUX_EUR_TND;
    }
}