/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

/**
 * 
 * Beaucoup d'objet disposent de méthodes close() à appeler dans les finally mais aucune interface ne les regroupe.<br/>
 * les implémentations de ICloser sont des handler chargée d'appeleer les close() de ces objet.
 * 
 * @author ginguene
 * 
 */
public interface ICloser {

    public void close() throws Exception;

}
