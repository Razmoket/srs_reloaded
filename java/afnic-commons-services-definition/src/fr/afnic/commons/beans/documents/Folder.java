/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/Folder.java#22 $ $Revision: #22 $Author: ginguene $
 */

package fr.afnic.commons.beans.documents;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * Principaux dossier de stockage des document dans docushare et leurs id. Ces Id ne sont pas amenés à évoluer. On les stocke donc en dur ici. Si toi,
 * qui lit ce commentaire trouve comment faire plus propre, n'hesites pas :)
 * 
 * @author ginguene
 * 
 */
public class Folder extends Element {

    private static final long serialVersionUID = -639346880393117924L;
    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(Folder.class);

    private Path path;

    public Folder(Path path, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.path = path;
    }

    public Folder(String handle, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.handle = handle;
    }

    public Folder(String handle, String title, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.handle = handle;
        this.title = title;
    }

    /**
     * retourne tous les element (Folder et Document) contenus dans le folder
     * 
     * @return
     * @throws IllegalAccessException
     */
    public List<Element> getElements() throws ServiceException {
        return AppServiceFacade.getDocumentService().getFolderElements(this.handle, this.userIdCaller, this.tldCaller);
    }

    /**
     * retourne tous les Document GDD contenus dans le folder.
     * 
     * @return
     * @throws RequestFailedException
     */
    public List<GddDocument> getGddDocuments() throws ServiceException {
        return this.getGddDocuments(false);
    }

    /**
     * retourne tous les Document GDD contenus dans le folder. La recherche s'effectue egalement dans les sous repertoire si isRecursive vaut true
     * 
     * @param recursive
     *            indique si l'on doit chercher aussi dans les sous-répertoires
     * 
     * @return
     * @throws IllegalAccessException
     */
    public List<GddDocument> getGddDocuments(boolean isRecursive) throws ServiceException {
        List<Element> elements = AppServiceFacade.getOldDocumentService().getElementsFromFolderHandle(this.handle, isRecursive, this.userIdCaller, this.tldCaller);
        List<GddDocument> result = new ArrayList<GddDocument>();
        for (Element element : elements) {
            if (element instanceof GddDocument) {
                result.add((GddDocument) element);
            }
        }
        return result;
    }

    /**
     * retourne tous les Document contenus dans le folder. La recherche s'effectue egalement dans les sous repertoire si isRecursive vaut true
     * 
     * @param recursive
     *            indique si l'on doit chercher aussi dans les sous-répertoires
     * 
     * @return
     * @throws IllegalAccessException
     */
    public List<Document> getDocuments() throws ServiceException {
        List<Element> elements = AppServiceFacade.getDocumentService().getFolderElements(this.handle, this.userIdCaller, this.tldCaller);

        List<Document> result = new ArrayList<Document>();
        for (Element element : elements) {
            if (element instanceof Document) {
                result.add((Document) element);
            }
        }
        return result;
    }

    /**
     * retourne tous les Document contenus dans le folder qui correspondent au model. Le modele est un document dont on set un ou plusieurs attributs.
     * Si un document possède les même attribut que le document alors il est dans la liste retournée. Je teste ce type de pattern pour voir si il est
     * pratique ou non. Ne pas hésiter à faire autrement si ça devient trop limité.
     * 
     * Le model est un EdmDocument mais peut aussi etre un objet heritant de EdmDocument tel que EdmGddDocument ou EdmMail. Dans le cas d'un objet
     * specialise ou recherche egalement sur les attributs specifiques à ces objets.
     * 
     * 
     * 
     * @return
     * @throws IllegalAccessException
     */
    public List<Document> getDocuments(Document model, boolean isRecursive) throws ServiceException {

        // trie sur les attributs standard
        Folder.LOGGER.debug("call getAllElements for handle " + this.handle);
        List<Element> elements = AppServiceFacade.getOldDocumentService().getElementsFromFolderHandle(this.handle, isRecursive, this.userIdCaller, this.tldCaller);

        if (elements == null) Folder.LOGGER.debug("return is null");

        List<Document> allDocuments = new ArrayList<Document>();
        if (elements != null) {
            for (Element element : elements) {
                if (element instanceof Document) {
                    allDocuments.add((Document) element);
                }
            }
        }

        // on retire les elements qui ne correspondent pas aux critères
        int max = allDocuments.size();

        for (int i = max - 1; i >= 0; i--) {
            Document document = allDocuments.get(i);
            if (model.getHandle() != null && !model.getHandle().equals(document.getHandle())) allDocuments.remove(i);
            if (model.getSender() != null && !model.getSender().equals(document.getSender())) allDocuments.remove(i);
            if (model.getTitle() != null && !model.getTitle().equals(document.getTitle())) allDocuments.remove(i);
        }

        if (model instanceof GddDocument) {
            this.filterDocuments((GddDocument) model, allDocuments);
            return allDocuments;
        } else if (model instanceof EmailDocument) {
            Folder.LOGGER.debug("search EdmMail");
            this.filterDocuments((EmailDocument) model, allDocuments);
            return allDocuments;
        } else {
            return allDocuments;
        }
    }

    public Document getDocumentWithHandle(String handle, boolean isRecursive) throws ServiceException {

        // trie sur les attributs standard
        Folder.LOGGER.debug("call getAllElements for handle " + this.handle);
        List<Element> elements = AppServiceFacade.getOldDocumentService().getElementsFromFolderHandle(this.handle, isRecursive, this.userIdCaller, this.tldCaller);

        if (elements == null) Folder.LOGGER.debug("return is null");

        if (elements == null) return null;

        for (Element element : elements) {
            if (element instanceof Document && element.getHandle().equals(handle)) {
                return (Document) element;
            }
        }

        return null;

    }

    /**
     * Enleve de la liste les elements qui ne correspondent pas au model.
     * 
     * @return
     * @throws IllegalAccessException
     */
    private void filterDocuments(EmailDocument model, List<Document> documentsToFilter) throws RequestFailedException {

        // on retire les elements qui ne correspondent pas aux critères
        for (int i = documentsToFilter.size() - 1; i >= 0; i--) {
            if (documentsToFilter.get(i) instanceof EmailDocument) {
                EmailDocument mailDocument = (EmailDocument) documentsToFilter.get(i);
                SentEmail sentMail = mailDocument.getSentEmail();
                SentEmail mailModel = model.getSentEmail();

                if (mailModel != null && mailModel.getMessageId() != null && !mailModel.getMessageId().equals(sentMail.getMessageId())) {
                    documentsToFilter.remove(i);
                }
            } else {
                documentsToFilter.remove(i);
            }

        }
    }

    /**
     * Enleve de la liste les elements qui ne correspondent pas au model.
     * 
     * 
     * @return
     * @throws IllegalAccessException
     */
    private void filterDocuments(GddDocument model, List<Document> documentsToFilter) throws RequestFailedException {

        // on retire les elements qui ne correspondent pas aux critères
        for (int i = documentsToFilter.size() - 1; i >= 0; i--) {
            if (documentsToFilter.get(i) instanceof GddDocument) {
                GddDocument document = (GddDocument) documentsToFilter.get(i);
                if (model.getDomain() != null && !model.getDomain().equals(document.getDomain())) documentsToFilter.remove(i);
                if (model.getHandle() != null && !model.getHandle().equals(document.getHandle())) documentsToFilter.remove(i);
                if (model.getRequestOperation() != null && !model.getRequestOperation().equals(document.getRequestOperation())) documentsToFilter.remove(i);
                if (model.getFileName() != null && !model.getFileName().equals(document.getFileName())) documentsToFilter.remove(i);
                if (model.getContactHandle() != null && !model.getContactHandle().equals(document.getContactHandle())) documentsToFilter.remove(i);
                if (model.getTitle() != null && !model.getTitle().equals(document.getTitle())) documentsToFilter.remove(i);
                if (model.getReceptionDate() != null && !model.getReceptionDate().equals(document.getReceptionDate())) documentsToFilter.remove(i);
                if (model.getSender() != null && !model.getSender().equals(document.getSender())) documentsToFilter.remove(i);
                if (model.getRegistrarCode() != null && !model.getRegistrarCode().equals(document.getRegistrarCode())) documentsToFilter.remove(i);
            } else {
                documentsToFilter.remove(i);
            }
        }
    }

    public Path getPath() {
        return this.path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = new Path(path);
    }

}
