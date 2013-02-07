package fr.afnic.utils;

/**
 * Petite classe utilitaire permettant d'ajouter facilement des traces de debugs.
 * Il suffit de retracer les appels pour ensuite retirer les traces.
 * Dans un code de production, aucun appel à Debugger ne doit etre effectuer, c'est une classe réservée au dev.
 * 
 * @author ginguene
 *
 */
public class Debugger {

    private Debugger() {

    }

    public static void trace(String str) {
        System.err.println("====>" + str);
    }

    public static void stack() {
        try {
            throw new Exception("Debugger");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
