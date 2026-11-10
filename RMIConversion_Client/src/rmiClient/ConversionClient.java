package rmiClient;

import java.rmi.Naming;

import rmiService.IConversion;

public class ConversionClient {
    public static void main(String[] args) {
        try {
            // ii. Chercher la référence de l'objet distant dans le RMI registry
            IConversion service =
                    (IConversion) Naming.lookup("rmi://localhost:1099/ConversionService");

            // iii. Invoquer la méthode distante
            double resultat = service.convertirMontant(500);

            System.out.println("500 EUR = " + resultat + " TND");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}