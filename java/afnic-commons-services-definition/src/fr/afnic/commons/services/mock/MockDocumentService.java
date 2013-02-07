package fr.afnic.commons.services.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.DocumentSource;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.Path;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IDocumentService;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockDocumentService implements IDocumentService {

    public static final HashMap<String, Document> HANDLE_DOCUMENT_MAP = new HashMap<String, Document>();
    private static int index = 0;

    @Override
    public Tree getTree(UserId userId, TldServiceFacade tld) {
        Tree tree = new Tree();
        tree.setUnitTest(new Folder(new Path("/"), userId, tld));
        return tree;
    }

    @Override
    public String download(String handle, String downloadFolder, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Document getDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return HANDLE_DOCUMENT_MAP.get(handle);
    }

    @Override
    public <ELEMENT extends Element> ELEMENT getElementWithHandle(String handle, Class<ELEMENT> elementClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<Element> getFolderElements(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void deleteElement(Element element, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void updateTitle(String documentHandle, String newTitle, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDocumentWithHandle(documentHandle, userId, tld).setTitle(newTitle);
    }

    @Override
    public void updateReceptionDate(String documentHandle, Date date, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void updateComment(String documentHandle, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDocumentWithHandle(documentHandle, userId, tld).setComment(comment);
    }

    @Override
    public void moveDocument(String documentHandle, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    public static String getNextHandle(Document document) throws ServiceException {
        String id = System.currentTimeMillis() + "_" + Integer.toString(index++);
        if (document instanceof SimpleDocument) {
            return "Document-" + id;
        }

        if (document instanceof GddDocument) {
            return "AF_Document_GDD-" + id;
        }

        if (document instanceof EmailDocument) {
            return "MailMessage-" + id;
        }

        throw new ServiceException("getHandle() not implemented for " + document.getClass().getSimpleName());
    }

    @Override
    public String createDocument(Document document, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        String handle = getNextHandle(document);
        HANDLE_DOCUMENT_MAP.put(handle, document);
        document.setHandle(handle);
        return handle;
    }

    @Override
    public String createDocument(Document document, Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public String createDocument(Fax fax, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public String createDocument(SentEmail email, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {

        EmailDocument doc = new EmailDocument(userId, tld);
        doc.setSource(DocumentSource.Mail);
        doc.setSentEmail(email);

        return this.createDocument(doc, folder, userId, tld);
    }

    @Override
    public void deleteDocumentsFromFolder(Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void archive(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void sort(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Folder getOrCreateFolder(Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Folder getOrCreateFolder(String path, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Folder getElementFolder(String elementHandle, UserId userId, TldServiceFacade tld) throws RequestFailedException, ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public int getFolderElementsCount(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getFolderElements(folderHandle, userId, tld).size();
    }

    @Override
    public void updateMetadata(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

}
