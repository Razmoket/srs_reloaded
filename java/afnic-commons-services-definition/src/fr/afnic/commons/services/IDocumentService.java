package fr.afnic.commons.services;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.Path;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Interface permettant de piloter une Gestion Electronique de Documents.
 * 
 * @author ginguene
 *
 */
public interface IDocumentService extends Serializable {

    /**
     * Retourne l'arbre des répertoires prédéfinies
     * @throws RemoteException 
     * @throws ServiceException 
     */
    public Tree getTree(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Télécharge un document dont on précise l'identifiant    
     */
    public String download(String handle, String downloadFolder, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère un document dont on précise l'identifiant
     */
    public Document getDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne un element à partir de son handle.
     * On précise le type de sous élément désiré.
     */
    public <ELEMENT extends Element> ELEMENT getElementWithHandle(String handle, Class<ELEMENT> elementClass, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère tous les éléments contenues d'un répertoire.
     */
    public List<Element> getFolderElements(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Supprime un élément.
     */
    public void deleteElement(Element element, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met à jour le titre d'un document.
     */
    public void updateTitle(String documentHandle, String newTitle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met à jour la date de reception d'un document.
     */
    public void updateReceptionDate(String documentHandle, Date date, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met à jour le commentaire d'un document.
     */
    public void updateComment(String documentHandle, String comment, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateMetadata(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * déplace un document dans un répertoire
     */
    public void moveDocument(String documentHandle, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un document dans la Ged et retourne le handle du document créé.
     */
    public String createDocument(Document document, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un document dans la Ged et retourne le handle du document créé.
     */
    public String createDocument(Document document, Path path, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un fax en tant que document Gdd dans la Ged et retourne le handle du document créé.
     */
    public String createDocument(Fax fax, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un mail dans un répertoire et retouren le handle du document créé.
     */
    public String createDocument(SentEmail email, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Supprime tous les documents d'un répertoire.
     */
    public void deleteDocumentsFromFolder(Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void archive(Document document, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void sort(Document document, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Folder getOrCreateFolder(Path path, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Folder getOrCreateFolder(String path, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retroune le répertoire contenant un element.
     */
    public Folder getElementFolder(String elementHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getFolderElementsCount(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException;;

}
