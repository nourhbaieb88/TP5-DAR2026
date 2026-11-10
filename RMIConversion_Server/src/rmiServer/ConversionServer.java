package rmiServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import rmiService.ConversionImpl;

public class ConversionServer {
    public static void main(String[] args) {
        try {
            // i. Activer l'annuaire RMIRegistry
            LocateRegistry.createRegistry(1099);

            // ii. Créer une instance de ConversionImpl
            ConversionImpl service = new ConversionImpl();

            // iii. Publier la référence dans l'annuaire
            Naming.rebind("rmi://localhost:1099/ConversionService", service);

            System.out.println("Serveur RMI démarré : ConversionService est publié.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}