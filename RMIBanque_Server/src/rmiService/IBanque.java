package rmiService;

import java.rmi.Remote;
import java.rmi.RemoteException;

import metier.Compte;

public interface IBanque extends Remote {
    String creerCompte(Compte c) throws RemoteException;
    String getInfoCompte(int code) throws RemoteException;
}