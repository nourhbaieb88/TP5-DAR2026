package rmiServer;

import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;

import rmiService.BanqueImpl;

public class BanqueServer {
    public static void main(String[] args) {
        try {
            // Demarre seulement l'annuaire RMI sur le port 1099
            LocateRegistry.createRegistry(1099);

            BanqueImpl banque = new BanqueImpl();

            // JNDI : configuration lue dans jndi.properties
            Context ctx = new InitialContext();
            ctx.rebind("BanqueService", banque);

            System.out.println("Serveur Banque demarre : BanqueService publie via JNDI.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}