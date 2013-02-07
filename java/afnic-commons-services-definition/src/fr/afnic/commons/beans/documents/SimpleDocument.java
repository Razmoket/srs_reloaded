/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/SimpleDocument.java#10 $
 * $Revision: #10 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.documents;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * Représente un document de base de la GED rattaché avec un fichier
 * 
 * @author ginguene
 * 
 */
public class SimpleDocument extends Document implements Cloneable {

    private static final long serialVersionUID = 1L;

    /* infos issus de la ged */
    protected String fileName;

    /* permet de surcharger le nom du fichier par défaut lors de l'upload*/
    protected String preferedFileName;

    public SimpleDocument(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    /**
     * telecharge le fichier dans un répertoire que l'on précise. Retourne le chemin local du fichier telechargé ou null en cas d'erreur.
     * 
     * @param downloadFolder
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public String download(String destinationPath) throws ServiceException {
        return AppServiceFacade.getDocumentService().download(this.handle, destinationPath, this.userIdCaller, this.tldCaller);
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public SimpleDocument copy() throws CloneNotSupportedException {
        return (SimpleDocument) this.clone();
    }

    public String getPreferedFileName() {
        return this.preferedFileName;
    }

    public void setPreferedFileName(String preferedFileName) {
        this.preferedFileName = preferedFileName;
    }

}
