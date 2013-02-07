package fr.afnic.utils.junit;

/**
 * Type de snapshot. Définit le comportement lors de la comparaison
 * 
 * 
 * @author ginguene
 * 
 */
public enum SnapshotType {
    /** Fait uen comparaison des champs via la méthode equals() */
    NORMAL,
    /** ignore les accents lors de la comparaison */
    IGNORE_ACCENT;

}
