package fr.afnic.commons.services.multitld;

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
import fr.afnic.commons.services.IDocumentService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldDocumentService implements IDocumentService {

    protected MultiTldDocumentService() {
        super();
    }

    @Override
    public Tree getTree(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getTree(userId, tld);
    }

    @Override
    public String download(String handle, String downloadFolder, UserId userId, TldServiceFacade tld) throws ServiceException {
        return null;
    }

    @Override
    public Document getDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getDocumentWithHandle(handle, userId, tld);
    }

    @Override
    public <ELEMENT extends Element> ELEMENT getElementWithHandle(String handle, Class<ELEMENT> elementClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getElementWithHandle(handle, elementClass, userId, tld);
    }

    @Override
    public List<Element> getFolderElements(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getFolderElements(folderHandle, userId, tld);
    }

    @Override
    public void deleteElement(Element element, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().deleteElement(element, userId, tld);
    }

    @Override
    public void updateTitle(String documentHandle, String newTitle, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().updateTitle(documentHandle, newTitle, userId, tld);
    }

    @Override
    public void updateReceptionDate(String documentHandle, Date date, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().updateReceptionDate(documentHandle, date, userId, tld);
    }

    @Override
    public void updateComment(String documentHandle, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().updateComment(documentHandle, comment, userId, tld);
    }

    @Override
    public void updateMetadata(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().updateMetadata(document, userId, tld);
    }

    @Override
    public void moveDocument(String documentHandle, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().moveDocument(documentHandle, folder, userId, tld);
    }

    @Override
    public String createDocument(Document document, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().createDocument(document, folder, userId, tld);
    }

    @Override
    public String createDocument(Document document, Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().createDocument(document, path, userId, tld);
    }

    @Override
    public String createDocument(Fax fax, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().createDocument(fax, folder, userId, tld);
    }

    @Override
    public String createDocument(SentEmail email, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().createDocument(email, folder, userId, tld);
    }

    @Override
    public void deleteDocumentsFromFolder(Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().deleteDocumentsFromFolder(folder, userId, tld);
    }

    @Override
    public void archive(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().archive(document, userId, tld);
    }

    @Override
    public void sort(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDocumentService().sort(document, userId, tld);
    }

    @Override
    public Folder getOrCreateFolder(Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getOrCreateFolder(path, userId, tld);
    }

    @Override
    public Folder getOrCreateFolder(String path, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getOrCreateFolder(path, userId, tld);
    }

    @Override
    public Folder getElementFolder(String elementHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getElementFolder(elementHandle, userId, tld);
    }

    @Override
    public int getFolderElementsCount(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDocumentService().getFolderElementsCount(folderHandle, userId, tld);
    }

}
