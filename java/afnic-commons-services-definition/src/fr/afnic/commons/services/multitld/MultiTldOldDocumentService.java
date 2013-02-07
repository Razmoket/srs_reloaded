package fr.afnic.commons.services.multitld;

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
import fr.afnic.commons.services.IOldDocumentService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldOldDocumentService implements IOldDocumentService {

    protected MultiTldOldDocumentService() {
        super();
    }

    @Override
    public List<GddDocument> getAttachedDocumentsToDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getAttachedDocumentsToDomain(domain, userId, tld);
    }

    @Override
    public String getNormalizedTitle(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getNormalizedTitle(document, userId, tld);
    }

    @Override
    public Folder getFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getFolder(folderType, userId, tld);
    }

    @Override
    public String addMailInFolder(SentEmail mail, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().addMailInFolder(mail, folderType, userId, tld);
    }

    @Override
    public void moveDocumentToFolder(String documentHandle, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOldDocumentService().moveDocumentToFolder(documentHandle, folderType, userId, tld);
    }

    @Override
    public String addGddDocumentInFolder(GddDocument document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().addGddDocumentInFolder(document, folderType, userId, tld);
    }

    @Override
    public String addSimpleDocumentInFolder(SimpleDocument document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().addSimpleDocumentInFolder(document, folderType, userId, tld);
    }

    @Override
    public List<Element> getElementsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getElementsFromFolder(folderType, userId, tld);
    }

    @Override
    public List<Element> getElementsFromFolderHandle(String folderHandle, boolean isRecursive, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getElementsFromFolderHandle(folderHandle, isRecursive, userId, tld);
    }

    @Override
    public List<Document> getDocumentsFromFolder(Document model, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getDocumentsFromFolder(model, folderType, userId, tld);
    }

    @Override
    public List<Document> getDocumentsFromFolder(Document model, FolderType folderType, boolean isRecursive, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getDocumentsFromFolder(model, folderType, isRecursive, userId, tld);
    }

    @Override
    public List<GddDocument> getGddDocumentsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getGddDocumentsFromFolder(folderType, userId, tld);
    }

    @Override
    public List<Document> getDocumentsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getDocumentsFromFolder(folderType, userId, tld);
    }

    @Override
    public FolderType getFolderTypeForDocument(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().getFolderTypeForDocument(documentHandle, userId, tld);
    }

    @Override
    public String addFaxAsGddDocumentInFolder(Fax fax, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOldDocumentService().addFaxAsGddDocumentInFolder(fax, folderType, userId, tld);
    }
}
