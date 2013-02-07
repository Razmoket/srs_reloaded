/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.DocumentSource;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.FolderType;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IOldDocumentService;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;

/**
 * Mock du documentService.<br/>
 * Fonctionne via un système de map et conserve toutes les données en mémoire.
 * 
 * @author ginguene
 * 
 */
public class MockOldDocumentService implements IOldDocumentService {

    private final HashMap<FolderType, Folder> folderMap = new HashMap<FolderType, Folder>();

    private final HashMap<Folder, ArrayList<Document>> documentFolderMap = new HashMap<Folder, ArrayList<Document>>();

    /** Clé handle du document */
    private final HashMap<String, FolderType> documentFolderTypeMap = new HashMap<String, FolderType>();

    /** {@inheritDoc} */
    @Override
    public Folder getFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getOrCreate(folderType, userId, tld);
    }

    public Document getDocumentWithHandle(String handle) throws ServiceException {
        Document document = MockDocumentService.HANDLE_DOCUMENT_MAP.get(handle);

        if (document != null) {
            return document;
        } else {
            throw new NotFoundException("no document found with handle " + handle, Document.class);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<GddDocument> getAttachedDocumentsToDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public String addMailInFolder(SentEmail mail, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {

        EmailDocument mailDocument = new EmailDocument(userId, tld);
        mailDocument.setSentEmail(mail);
        mail.setSentDate(new Date());
        mail.setReceivedDate(new Date());
        String handle = this.addDocumentInFolder(mailDocument, folderType, userId, tld);

        return handle;
    }

    /** {@inheritDoc} */
    @Override
    public void moveDocumentToFolder(String documentHandle, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.moveDocumentInFolder(this.getDocumentWithHandle(documentHandle), folderType, userId, tld);
    }

    /** {@inheritDoc} */
    @Override
    public String addGddDocumentInFolder(GddDocument document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (folderType == null) {
            throw new IllegalArgumentException("folderType cannot be null");
        }

        if (document == null) {
            throw new IllegalArgumentException("document cannot be null");
        }

        this.checkGddDocumentToAdd(document);

        return this.addDocumentInFolder(document, folderType, userId, tld);
    }

    /** {@inheritDoc} */
    @Override
    public String addSimpleDocumentInFolder(SimpleDocument document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.addDocumentInFolder(document, folderType, userId, tld);
    }

    /**
     * Ajoute le document dans les map du service mock et attribut un handle au document.
     * 
     * @param document
     * @param folderType
     * 
     * @return le handle du document
     * @throws ServiceException
     */
    private String addDocumentInFolder(Document document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (document.getReceptionDate() == null) {
            document.setReceptionDate(new Date());
        }

        String handle = MockDocumentService.getNextHandle(document);
        document.setHandle(handle);

        MockDocumentService.HANDLE_DOCUMENT_MAP.put(handle, document);
        this.documentFolderTypeMap.put(handle, folderType);
        Folder folder = this.getOrCreate(folderType, userId, tld);

        ArrayList<Document> documents = this.documentFolderMap.get(folder);
        if (documents == null) {
            documents = new ArrayList<Document>();
            this.documentFolderMap.put(folder, documents);
        }
        documents.add(document);
        return handle;

    }

    /** {@inheritDoc} */
    @Override
    public List<Element> getElementsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public List<Element> getElementsFromFolderHandle(String folderHandle, boolean isRecursive, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public List<Document> getDocumentsFromFolder(Document model, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDocumentsFromFolder(model, folderType, false, userId, tld);
    }

    /** {@inheritDoc} */
    @Override
    public List<Document> getDocumentsFromFolder(Document model, FolderType folderType, boolean isRecursive, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (model instanceof EmailDocument) {
            List<Document> documents = new ArrayList<Document>();

            EmailDocument modelMailDocument = (EmailDocument) model;
            for (Document document : this.getDocumentsFromFolder(folderType, userId, tld)) {
                if (document instanceof EmailDocument) {
                    EmailDocument mailDocument = (EmailDocument) document;

                    if (modelMailDocument.hasSentEmail()) {
                        if (mailDocument.getSentEmail().getMessageId().equals(modelMailDocument.getSentEmail().getMessageId())) {
                            documents.add(mailDocument);
                        }
                    } else {
                        throw new NotImplementedException();
                    }
                }

            }
            return documents;
        }
        throw new NotImplementedException("not implemented for " + model.getClass().getSimpleName());
    }

    /** {@inheritDoc} */
    @Override
    public List<GddDocument> getGddDocumentsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<GddDocument> gddDocuments = new ArrayList<GddDocument>();

        for (Document document : this.getDocumentsFromFolder(folderType, userId, tld)) {
            if (document instanceof GddDocument) {
                gddDocuments.add((GddDocument) document);
            }
        }
        return gddDocuments;

    }

    /** {@inheritDoc} */
    @Override
    public List<Document> getDocumentsFromFolder(FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {

        try {
            Folder folder = this.getFolder(folderType, userId, tld);

            // On retourne des clones pour que toute modification des objets retournée par cette méthode n'ait pas d'influence
            // sur les objets qui sont stockés par le service.
            List<Document> savedDocuments = this.documentFolderMap.get(folder);
            List<Document> copyDocuments = new ArrayList<Document>();
            if (savedDocuments != null) {
                for (Document document : savedDocuments) {
                    copyDocuments.add(document.copy());
                }
            }

            return copyDocuments;
        } catch (Exception e) {
            throw new ServiceException("getDocumentsFromFolder() failed", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public FolderType getFolderTypeForDocument(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.documentFolderTypeMap.get(documentHandle);
    }

    /** {@inheritDoc} */
    @Override
    public String addFaxAsGddDocumentInFolder(Fax fax, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (fax == null) {
            throw new IllegalArgumentException("fax cannot be null");
        }

        if (fax.getFile() == null) {
            throw new IllegalArgumentException("fax.file cannot be null");
        }

        if (folderType == null) {
            throw new IllegalArgumentException("folderType cannot be null");
        }

        String title = "Fax received from " + fax.getSender();
        GddDocument document = new GddDocument(userId, tld);
        document.setTitle(title);
        document.setSource(DocumentSource.Fax);
        document.setSender(fax.getSender());
        document.setReceptionDate(fax.getReceptionDate());

        try {
            document.setFileName(fax.getFile().getCanonicalPath());
        } catch (IOException e) {
            throw new ServiceException("cannot get fax.file.canonicalPath", e);
        }

        return this.addGddDocumentInFolder(document, FolderType.Inbox, userId, tld);
    }

    /** {@inheritDoc} */
    @Override
    public String getNormalizedTitle(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        return document.getDomain()
               + " " + document.getRequestOperation()
               + " " + DateUtils.toSecondFormat(document.getReceptionDate());

    }

    /**
     * Positionne un document dans un répertoire.<br/>
     * Le document doit déja avoir un handle.
     * 
     * @param document
     * @param folderType
     * @throws ServiceException
     */
    private void moveDocumentInFolder(Document document, FolderType folderType, UserId userId, TldServiceFacade tld) throws ServiceException {

        try {
            if (document.getHandle() == null) throw new IllegalArgumentException("document.handle must not be null");

            this.removeDocumentFromCurrentFolder(document, userId, tld);

            Folder folder = this.getFolder(folderType, userId, tld);
            Document copy = document.copy();

            ArrayList<Document> documents = this.documentFolderMap.get(folder);
            if (documents == null) {
                documents = new ArrayList<Document>();
                this.documentFolderMap.put(folder, documents);
            }

            documents.add(copy);

            MockDocumentService.HANDLE_DOCUMENT_MAP.put(copy.getHandle(), copy);
            this.documentFolderTypeMap.put(copy.getHandle(), folderType);
        } catch (Exception e) {
            throw new ServiceException("moveDocumentInFolder() failed", e);
        }

    }

    /**
     * Enleve un document d'un répertoire.<br/>
     * Le document doit déja avoir un handle.
     * 
     * @param document
     * @param folderType
     * @throws ServiceException
     */
    private void removeDocumentFromCurrentFolder(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            if (document.getFolderType() == null) {
                return;
            }

            Folder folder = this.getFolder(document.getFolderType(), userId, tld);

            List<Document> documents = this.documentFolderMap.get(folder);

            Document documentToRemove = null;
            for (Document doc : documents) {
                if (doc.getHandle().equals(document.getHandle())) {
                    documentToRemove = doc;
                    break;
                }
            }

            documents.remove(documentToRemove);
            this.documentFolderTypeMap.remove(document.getHandle());
        } catch (Exception e) {
            throw new ServiceException("removeDocumentInFolder() failed", e);
        }

    }

    private Folder getOrCreate(FolderType folderType, UserId userId, TldServiceFacade tld) {
        Preconditions.checkNotNull(folderType, "folderType");

        Folder folder = this.folderMap.get(folderType);
        if (folder == null) {
            folder = new Folder(folderType.toString(), userId, tld);
            this.folderMap.put(folderType, folder);
        }
        return folder;
    }

    /**
     * Vérifie qu'un document Gdd peut etre ajouté.<br/>
     * Si ce n'est pas le cas, une IllegalArgumentException est levée
     * 
     * @param document
     * @throws IllegalArgumentException
     */
    private void checkGddDocumentToAdd(SimpleDocument document) throws IllegalArgumentException {
        this.checkDocumentToAdd(document);
        if (document.getFileName() == null) throw new IllegalArgumentException("document.fileName cannot be null");

        File file = new File(document.getFileName());
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + document.getFileName() + " not found");
        }
    }

    /**
     * Vérifie qu'un document peut etre ajouté.<br/>
     * Si ce n'est pas le cas, une IllegalArgumentException est levée
     * 
     * @param document
     * @throws IllegalArgumentException
     */
    private void checkDocumentToAdd(Document document) throws IllegalArgumentException {

        if (document == null) throw new IllegalArgumentException("document cannot be null");
        if (document.getSource() == null) throw new IllegalArgumentException("document.source cannot be null");
        if (document.getTitle() == null) throw new IllegalArgumentException("document.title cannot be null");
    }

}
