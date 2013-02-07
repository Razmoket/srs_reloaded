package fr.afnic.commons.services.filter;

/**
 * Retourne l'identifiant d'un objet
 * 
 * @author ginguene
 * 
 */
public interface IObjectIdentifier<T> {

    public String getId(T object);

}
