package fr.afnic.commons.services.jooq;

/*
 * $Id: DocushareDocumentService.java,v 1.32 2010/09/24 14:37:50 ginguene Exp $ $Revision: 1.32 $ $Author: ginguene $
 */

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.boarequest.TopLevelOperation;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.DocumentSource;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.beans.documents.Fax;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.LegalDocument;
import fr.afnic.commons.beans.documents.Path;
import fr.afnic.commons.beans.documents.RootFolder;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.documents.TreeEnum;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IDocumentService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.internal.JooqDocumentInternalService;
import fr.afnic.commons.services.jooq.stub.grc.tables.DataFileTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.DocTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.FileAuthRequestTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.VDirectoryView;
import fr.afnic.commons.services.jooq.stub.grc.tables.VDocumentTypeView;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

/**
 * 
 * Classe permettant d'acceder à docushare. Tous les commentaires en anglais sont en fait les commentaires des exemples fournit par docushare qui ont
 * fortement inspirés ce code. Il n'y a pas grande utilité à traduire ces commentaire, mais ils peuvent s'avérer utiles.
 * 
 * <br/>
 * Fonctionne surtout avec les DOC du GDD. Pour une évolution il faudra faire quelques changement ou créer un nouveau type de doc. <br/>
 * <ul>
 * Attention: </u l> cet objet utilise un cache afin de limiter les echanges avec la ged, les données restent dans le cache min. (cf attribut
 * cacheTimeOut) processo
 * 
 * @author ginguene
 * 
 */
public class JooqDocumentService implements IDocumentService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(JooqDocumentService.class);

    private final Map<TldServiceFacade, Tree> mapTree;

    /*Sert de cache pour les répertoires, peut poser problème si l'on supprime des répertoires*/
    private final Map<Path, Folder> map = new HashMap<Path, Folder>();

    private ISqlConnectionFactory sqlConnectionFactory = null;

    private static VDocumentTypeView V_DOCUMENT_TYPE = VDocumentTypeView.V_DOCUMENT_TYPE;

    private static VDirectoryView V_DIRECTORY = VDirectoryView.V_DIRECTORY;

    public JooqDocumentService(final ISqlConnectionFactory sqlConnectionFactory) throws ServiceException {
        this.sqlConnectionFactory = sqlConnectionFactory;
        this.mapTree = new HashMap<TldServiceFacade, Tree>();
        this.loadTree();
    }

    public JooqDocumentService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
        this.mapTree = new HashMap<TldServiceFacade, Tree>();
        this.loadTree();
    }

    private void loadTree() throws ServiceException {
        for (TldServiceFacade tld : TldServiceFacade.values()) {
            Tree tree = new Tree();
            this.mapTree.put(tld, tree);
            for (TreeEnum treeEnum : TreeEnum.values()) {
                tree.setValue(treeEnum, this.getDir(treeEnum, tld));
            }
        }
    }

    public Folder getDir(TreeEnum treeEnum, TldServiceFacade tld) throws ServiceException {
        List<Folder> listFolder = JooqDocumentInternalService.getFolderWithConditions(this, new UserId(18), tld, V_DIRECTORY.FULL_PATH.equal("/" + tld.name() + treeEnum.getPath() + "/"));
        System.out.println("/" + tld.name() + treeEnum.getPath());
        if (listFolder.size() != 0) {
            return listFolder.get(0);
        }
        throw new ServiceException("can't find directory " + treeEnum.getPath() + " for Tld " + tld.name());
    }

    @Override
    public Tree getTree(UserId userId, TldServiceFacade tld) {
        return this.mapTree.get(tld);
    }

    public Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    public void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

    @Override
    public String download(String handle, String downloadFolder, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document getDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getElementWithHandle(handle, Document.class, userId, tld);
    }

    @Override
    public <ELEMENT extends Element> ELEMENT getElementWithHandle(String handle, Class<ELEMENT> elementClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return (ELEMENT) JooqDocumentInternalService.getDocumentWithConditions(this, userId, tld, V_DOCUMENT_TYPE.ID_DOCUMENT.equal(Integer.parseInt(handle))).get(0);
    }

    @Override
    public List<Element> getFolderElements(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return JooqDocumentInternalService.getDocumentWithConditions(this, userId, tld, V_DOCUMENT_TYPE.ID_DIRECTORY.equal(Integer.parseInt(folderHandle)));
    }

    @Override
    public void deleteElement(Element element, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            factory.update(DocTable.DOC)
                   .set(DocTable.DOC.IS_ACTIVE, (byte) 0)
                   .where(DocTable.DOC.ID_DOCUMENT.equal(Integer.parseInt(element.getHandle())));
        } catch (Exception e) {
            throw new ServiceException("deleteElement(" + element + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public void updateTitle(String documentHandle, String newTitle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            factory.update(DataFileTable.DATA_FILE)
                   .set(DataFileTable.DATA_FILE.TITLE, newTitle)
                   .where(DataFileTable.DATA_FILE.ID_FILE.equal(Integer.parseInt(documentHandle)));
        } catch (Exception e) {
            throw new ServiceException("updateComment(" + documentHandle + ", " + newTitle + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public void updateReceptionDate(String documentHandle, Date date, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            factory.update(DocTable.DOC)
                   .set(DocTable.DOC.DATE_RECEIVED, new java.sql.Date(date.getTime()))
                   .where(DocTable.DOC.ID_DOCUMENT.equal(Integer.parseInt(documentHandle)));
        } catch (Exception e) {
            throw new ServiceException("updateComment(" + documentHandle + ", " + date + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public void updateComment(String documentHandle, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            factory.update(DocTable.DOC)
                   .set(DocTable.DOC.COMMENT_TEXT, comment)
                   .where(DocTable.DOC.ID_DOCUMENT.equal(Integer.parseInt(documentHandle)));
        } catch (Exception e) {
            throw new ServiceException("updateComment(" + documentHandle + ", " + comment + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public void updateMetadata(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            factory.update(FileAuthRequestTable.FILE_AUTH_REQUEST)
                   .set(FileAuthRequestTable.FILE_AUTH_REQUEST.ID_OPERATION, Integer.parseInt(document.getRequestOperation().getTicketOperation().toString()))
                   .set(FileAuthRequestTable.FILE_AUTH_REQUEST.DOMAIN_NAME, document.getDomain())
                   .set(FileAuthRequestTable.FILE_AUTH_REQUEST.REGISTRAR_CODE, Integer.parseInt(document.getRegistrarCode()))
                   .set(FileAuthRequestTable.FILE_AUTH_REQUEST.CONTACT_HANDLE, document.getContactHandle())
                   .where(FileAuthRequestTable.FILE_AUTH_REQUEST.ID_FILE.equal(Integer.parseInt(document.getHandle())));

            factory.update(DocTable.DOC)
                   .set(DocTable.DOC.ID_DOCUMENT_TYPE, Integer.parseInt(document.getSource().toString()))
                   .where(DocTable.DOC.ID_DOCUMENT.equal(Integer.parseInt(document.getHandle())));
        } catch (Exception e) {
            throw new ServiceException("updateComment(" + document + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public void moveDocument(String documentHandle, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            factory.update(DocTable.DOC)
                   .set(DocTable.DOC.ID_DIRECTORY, Integer.parseInt(folder.getHandle()))
                   .where(DocTable.DOC.ID_DOCUMENT.equal(Integer.parseInt(documentHandle)));
        } catch (Exception e) {
            throw new ServiceException("updateComment(" + documentHandle + ", " + folder + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public String createDocument(Document document, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.createDocument(document, (Integer) null, userId, tld);
    }

    public String createDocument(Document document, Integer mailId, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (document instanceof GddDocument) {
            return JooqDocumentInternalService.createGddDocument(this, (GddDocument) document, mailId, userId, tld);
        }
        if (document instanceof LegalDocument) {
            return JooqDocumentInternalService.createLegalDocument(this, (LegalDocument) document, mailId, userId, tld);
        }

        if (document instanceof SimpleDocument) {
            return JooqDocumentInternalService.createSimpleDocument(this, (SimpleDocument) document, mailId, userId, tld);
        }

        throw new IllegalArgumentException("Cannot create document " + document.getClass().getSimpleName());
    }

    @Override
    public String createDocument(Document document, Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.createDocument(document, AppServiceFacade.getDocumentService().getOrCreateFolder(path, userId, tld), userId, tld);
    }

    @Override
    public String createDocument(Fax fax, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(fax, "fax");
        Preconditions.checkNotNull(fax.getFile(), "fax.file");
        Preconditions.checkNotNull(folder, "folder");
        try {
            return this.createDocument(fax.toGddDocument(userId, tld), (Integer) null, userId, tld);
        } catch (final Exception e) {
            throw new ServiceException("addFaxAsGddDocumentInFolder(" + fax + "," + folder.getHandle() + ") failed", e);
        }
    }

    @Override
    public String createDocument(SentEmail email, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        EmailDocument emailDocument = new EmailDocument(userId, tld);
        emailDocument.setFolder(folder);
        emailDocument.setCreateDate(email.getSentDate());
        final Date date = email.getSentDate();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
        emailDocument.setTitle(email.getSubject() + " " + dateFormat.format(date));
        emailDocument.setSentEmail(email);
        emailDocument.setFolder(folder);
        JooqDocumentInternalService.createMail(this, emailDocument, userId, tld);

        if (email.hasAttachements()) {
            for (File attachement : email.getAttachements()) {
                SimpleDocument doc = new SimpleDocument(userId, tld);
                doc.setReceptionDate(email.getReceivedDate());
                doc.setCreateDate(email.getReceivedDate());
                doc.setFileName(attachement.getName());
                doc.setSource(DocumentSource.Mail);
                doc.setTitle(email.getSubject());
                doc.setSender(email.getFromEmailAddress().getValue());
                doc.setComment("Attachement-" + emailDocument.getHandle());
                String docHandle = this.createDocument(doc, emailDocument.getHandleAsInt(), userId, tld);
                doc.setHandle(docHandle);
                doc.attachTo(emailDocument.getHandle());
            }
        }
        return emailDocument.getHandle();
    }

    @Override
    public void deleteDocumentsFromFolder(Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        LOGGER.debug("About to delete all documents from folder " + folder);
        final List<Element> elements = AppServiceFacade.getDocumentService().getFolderElements(folder.getHandle(), userId, tld);
        LOGGER.debug("About to delete all documents from folder " + folder);
        for (final Element element : elements) {
            this.deleteElement(element, userId, tld);
        }
    }

    @Override
    public void archive(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        final Date date = new Date();

        final String year = new SimpleDateFormat("yyyy").format(date);
        final String month = new SimpleDateFormat("MM").format(date) + " " + new SimpleDateFormat("MMMM").format(date);
        final String day = new SimpleDateFormat("dd").format(date);

        Folder archiveFolder = AppServiceFacade.getDocumentService().getElementWithHandle(this.mapTree.get(tld).getArchive().getHandle(), Folder.class, userId, tld);

        String pathStr = archiveFolder.getPath().getValue() + "/" + year + "/" + month + "/" + day;

        archiveFolder = JooqDocumentInternalService.createFolder(this, new Path(pathStr), userId, tld);
        this.moveDocument(document.getHandle(), archiveFolder, userId, tld);
    }

    @Override
    public void sort(Document document, UserId userId, TldServiceFacade tld) throws ServiceException {
        Operation operation = document.getOperation();
        if (operation != null) {
            TopLevelOperation topLevelOperation = null;
            if (operation instanceof TopLevelOperation) {
                topLevelOperation = (TopLevelOperation) operation;
            } else {
                topLevelOperation = operation.getTopLevelOperation();
            }
            Folder folder = JooqDocumentInternalService.getOrCreateTopLevelOperationFolder(topLevelOperation, userId, tld);
            this.moveDocument(document.getHandle(), folder, userId, tld);
        } else {
            throw new IllegalArgumentException("cannot sort document " + document.getHandle() + " because it is not attached to an operation");
        }
    }

    @Override
    public Folder getOrCreateFolder(Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(path, "path");

        if (this.map.containsKey(path)) {
            return this.map.get(path);
        } else {
            if (path.isRoot()) {
                return RootFolder.getRootFolder(userId, tld);
            } else {
                Folder folder = JooqDocumentInternalService.searchFolder(this, path, userId, tld);
                if (folder == null) {
                    folder = JooqDocumentInternalService.createFolder(this, path, userId, tld);
                }
                this.map.put(folder.getPath(), folder);
                return folder;
            }
        }
    }

    @Override
    public Folder getOrCreateFolder(String path, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getOrCreateFolder(new Path(path), userId, tld);
    }

    @Override
    public Folder getElementFolder(String elementHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Integer handleFolder = JooqDocumentInternalService.getElementParent(this, userId, tld, elementHandle);
        return this.getElementWithHandle(handleFolder + "", Folder.class, userId, tld);
    }

    @Override
    public int getFolderElementsCount(String folderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return JooqDocumentInternalService.getNbElementWithConditions(this, userId, tld, V_DOCUMENT_TYPE.ID_DIRECTORY.equal(Integer.parseInt(folderHandle)));
    }

}
