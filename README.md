# TP5 – Activité 5-1 : Service de conversion de devise (Java RMI)

**Étudiant :** Nour Hbaieb
**Classe :** LSI3 1TP1
**Matière :** Développement d'Applications Réparties
**Enseignante des TPs :** Zeineb DAOUD
**Environnement :** IntelliJ IDEA, JDK, Git/GitHub

## 1. Objectif

Concevoir une application répartie avec Java RMI. Un serveur expose un service
de conversion de devises. Un client distant l'invoque pour convertir un montant
en euros vers le dinar tunisien.

## 2. Structure du dépôt

```
TP5-DAR2026/
├── RMIConversion_Server/
├── RMIConversion_Client/
├── conversionClient.jar
├── images/
└── README.md
```

## 3. Développement du serveur (projet `RMIConversion_Server`)

Le projet serveur contient deux packages :
- `rmiService` : l'interface distante `IConversion` et son implémentation `ConversionImpl`
- `rmiServer` : la classe `ConversionServer`, qui active le RMIRegistry, crée
  l'objet distant et publie sa référence dans l'annuaire

![Arborescence du serveur](images/01_arborisation_server.png)

### Interface distante

```java
package rmiService;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IConversion extends Remote {
    double convertirMontant(double mt) throws RemoteException;
}
```

### Implémentation

```java
package rmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConversionImpl extends UnicastRemoteObject implements IConversion {
    private static final double TAUX_EUR_TND = 3.40;

    public ConversionImpl() throws RemoteException {
        super();
    }

    @Override
    public double convertirMontant(double mt) throws RemoteException {
        return mt * TAUX_EUR_TND;
    }
}
```

### Serveur

```java
package rmiServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import rmiService.ConversionImpl;

public class ConversionServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);                  // i. activer le RMIRegistry
            ConversionImpl service = new ConversionImpl();        // ii. créer l'objet distant
            Naming.rebind("rmi://localhost:1099/ConversionService", service); // iii. publier
            System.out.println("Serveur RMI démarré : ConversionService est publié.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 4. Développement du client (projet `RMIConversion_Client`)

### Création du projet client

![Création du projet client](images/02_creation_client.png)

### Export de l'interface distante en `.jar`

Seule l'interface `IConversion` est exportée dans `conversionClient.jar`.
`ConversionImpl` n'est pas incluse, car l'implémentation reste côté serveur.
Le JAR est créé en ligne de commande puis vérifié avec `jar tf` :

```powershell
javac -d tmp_jar src\rmiService\IConversion.java
jar cf ..\conversionClient.jar -C tmp_jar rmiService\IConversion.class
jar tf ..\conversionClient.jar
```

![Création et vérification du JAR](images/03_export_jar_selection.png)

Le fichier `conversionClient.jar` est enregistré dans l'espace de travail :

![Fichier conversionClient.jar](images/04_executable_jar_file.png)

### Ajout du JAR au classpath du client

*File → Project Structure → Modules → Dependencies → + → JARs or Directories*.

![Classpath du client](images/05_classpath_client.png)

Le JAR apparaît dans les bibliothèques externes du projet client, avec le
package `rmiService` et l'interface `IConversion` :

![Bibliothèques référencées](images/06_referenced_libraries.png)

### Classe client

```java
package rmiClient;

import java.rmi.Naming;
import rmiService.IConversion;

public class ConversionClient {
    public static void main(String[] args) {
        try {
            IConversion service =
                (IConversion) Naming.lookup("rmi://localhost:1099/ConversionService");
            double resultat = service.convertirMontant(500);
            System.out.println("500 EUR = " + resultat + " TND");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 5. Exécution de l'application

Le serveur est lancé en premier. Sa console reste active, car il attend les
appels des clients.

![Exécution du serveur](images/07_execution_ConversionServer.png)

Le client est ensuite exécuté. Il récupère la référence de l'objet distant dans
le RMIRegistry et invoque `convertirMontant(500)` :

![Exécution du client et résultat](images/08_execution_ConversionClient_resultat.png)

**Résultat obtenu :** `500 EUR = 1700.0 TND` (taux fixe de 3,40).

## 6. Synthèse

Ce TP m'a permis de mettre en œuvre une communication client/serveur avec Java
RMI et de comprendre le cycle de vie d'un service distant :

1. **Définition de l'interface** : `IConversion` étend `Remote`, et sa méthode
   déclare `RemoteException`.
2. **Implémentation** : `ConversionImpl` étend `UnicastRemoteObject`, ce qui
   exporte automatiquement l'objet pour qu'il reçoive des appels distants.
3. **Enregistrement** : le serveur démarre le RMIRegistry avec
   `LocateRegistry.createRegistry(1099)` et publie l'objet avec `Naming.rebind`.
4. **Invocation** : le client retrouve l'objet avec `Naming.lookup` et appelle
   `convertirMontant` comme une méthode locale.

Le client ne dispose que de l'interface, via `conversionClient.jar`. Le code
métier reste sur le serveur, ce qui sépare clairement les deux applications.

**Difficultés rencontrées :** la génération du JAR a échoué avec le chemin
`out\production`. Je l'ai résolue en compilant l'interface seule avec `javac`
puis en créant le JAR avec la commande `jar`, adaptée à PowerShell.