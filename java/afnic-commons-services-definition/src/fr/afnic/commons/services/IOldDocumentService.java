/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IOldDocumentService.java#8 $
 * $Revision: #8 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.FolderType;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * On passe par une interface pour avoir la même structure que pour acceder aux données de la DB. En passant via le singleton on a une abstraction qui
 * masque la prevenance des données. Cependant le modèle reste assez dépendant de l'edm choisie, je n'ai pas eu le temps de faire système tip top
 * permettant de passer d'une ged à l'autre sans se poser de question.
 * 
 * @author ginguene
 * 
 * 
 * Ancienne version remplacée par IDocumentService
 */
@Deprecated
public interface IOldDocumentService {

    /**
     * Retourne toutes les doc correspondants à un domaine dans le dossier en cours
     */
    public List<GddDocument> getAttachedDocumentsToDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Methode permettant de normaliser le format des titres des documents GDD avec le format de date adéquate.
     */
    public String getNormalizedTitle(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException;

    /***********************************************
    * Toutes les autres méthodes sont déprecated, utilisées encore par les anciennes couches attachées au requetes 
    * Elle pourront etre supprimé en même temps que le système de requete    
    * 
    */

    /**
     * Recupère le contenu du répertoire que l'on précise
     * 
     * @param folder
     * @return
     */
    @Deprecated
    public Folder getFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * insere un mail dans la ged dans un repertoire que l'on precise. Retourne l'handle de l'objet insere ou une daoException en cas de retour
     * 
     * @param mail
     *            objet representant le mail
     * @param folderHandle
     *            handle du repertoire ou l'on souhaite mettre le mail dans la ged
     * @return
     * 
     * @param mail
     * @param folderType
     * @return
     * 
     * @throws ServiceException
     */
    @Deprecated
    public String addMailInFolder(SentEmail mail, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Change un document de repertoire
     * 
     * @param documentHandle
     * @param fromFolderHandle
     * @param toFolderHandle
     * @return
     */
    @Deprecated
    public void moveDocumentToFolder(String documentHandle, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un document Gdd dans la GED. Le document est uploade et on initialise ses meta donnees.
     * 
     * @param document
     * @param fileName
     * @param folderType
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public String addGddDocumentInFolder(GddDocument document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un document dans la GED. Le document est uploade et on initialise ses meta donnees.
     * 
     * @param document
     * @param fileName
     * @param folderType
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public String addSimpleDocumentInFolder(SimpleDocument document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des documents qui correspondent au model dans le repertoire précisés en parametre. Pour correspondre au model un document
     * doit etre de la meme classe que le modele et avoir tous ses attributs egaux aux attribut non nul du model.
     * 
     * @param model
     * @param folderHandle
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<Element> getElementsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des documents qui correspondent au model dans le repertoire précisés en parametre. Pour correspondre au model un document
     * doit etre de la meme classe que le modele et avoir tous ses attributs egaux aux attribut non nul du model.
     * 
     * @param folderHandle
     * @param isRecursive
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<Element> getElementsFromFolderHandle(String folderHandle, boolean isRecursive, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des documents d'un répertoire correspondant à un model. Pour correspondre au model un document doit etre de la meme classe
     * que le modele et avoir tous ses attributs egaux aux attribut non nul du model.
     * 
     * @param model
     * @param folderHandle
     * @param isRecursive
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<Document> getDocumentsFromFolder(Document model, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des documents d'un répertoire correspondant à un model. Pour correspondre au model un document doit etre de la meme classe
     * que le modele et avoir tous ses attributs egaux aux attribut non nul du model. La recherche s'effectue dans le repertoire et ses sous
     * repertoires
     * 
     * @param model
     * @param folderHandle
     * @param isRecursive
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<Document> getDocumentsFromFolder(Document model, FolderType folderType, boolean isRecursive, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne tous les documents gdd contenus dans un répertoire.
     * 
     * @param model
     * @param folderType
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<GddDocument> getGddDocumentsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne tous les documents contenus dans un répertoire.
     * 
     * @param model
     * @param folderType
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<Document> getDocumentsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * 
     * 
     * 
     * @param documentHandle
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public FolderType getFolderTypeForDocument(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un Fax en tant que DocumentGdd dans le répertoire précisé.<br/>
     * Les autres méta données sont vides.<br
     * 
     * 
     * @param fax
     *            Fax à ajouter dans la Ged
     * @param folderType
     *            Repertoire dans lequel on veut mettre le nouveau document
     * @return l'identifiant de la doc ajoutée
     * @throws ServiceException
     *             En cas d'erreur retourne une DaoException
     */
    public String addFaxAsGddDocumentInFolder(Fax fax, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException;

}
