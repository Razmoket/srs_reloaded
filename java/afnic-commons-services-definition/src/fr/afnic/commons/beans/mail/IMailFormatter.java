package fr.afnic.commons.beans.mail;

/**
 * Interface définissant le formattage d'un mail.<br/>
 * Cette opération consiste à mettre le mail au format Text ou HTML
 * 
 * 
 * @author ginguene
 * 
 */
public interface IMailFormatter {

    public Email format(UnformatMail mail);

}
