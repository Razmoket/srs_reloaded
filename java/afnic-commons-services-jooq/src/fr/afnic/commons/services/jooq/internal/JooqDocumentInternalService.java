package fr.afnic.commons.services.jooq.internal;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.SelectConditionStep;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;
import org.jooq.impl.SQLDataType;

import fr.afnic.commons.beans.boarequest.TopLevelOperation;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.LegalDocument;
import fr.afnic.commons.beans.documents.Path;
import fr.afnic.commons.beans.documents.RootFolder;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.JooqDocumentService;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.Sequences;
import fr.afnic.commons.services.jooq.stub.grc.tables.DataFileTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.DirectoryTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.DocTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.FileAuthRequestTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.FileQualificationTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.MailTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.VDirectoryView;
import fr.afnic.commons.services.jooq.stub.grc.tables.VDocAuthRequestView;
import fr.afnic.commons.services.jooq.stub.grc.tables.VDocumentQualificationView;
import fr.afnic.commons.services.jooq.stub.grc.tables.VDocumentTypeView;
import fr.afnic.commons.services.jooq.stub.grc.tables.VMailView;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.DocTableRecord;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.VDirectoryViewRecord;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.VDocumentTypeViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class JooqDocumentInternalService {

    private static VDocumentTypeView V_DOCUMENT_TYPE = VDocumentTypeView.V_DOCUMENT_TYPE;

    private static VDocumentQualificationView V_DOCUMENT_QUALIFICATION = VDocumentQualificationView.V_DOCUMENT_QUALIFICATION;

    private static VDocAuthRequestView V_DOC_AUTH_REQUEST = VDocAuthRequestView.V_DOC_AUTH_REQUEST;

    private static VDirectoryView V_DIRECTORY = VDirectoryView.V_DIRECTORY;

    private static VMailView V_MAIL = VMailView.V_MAIL;

    public static List<Element> getDocumentWithConditions(JooqDocumentService service, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Factory factory = service.createFactory();
        try {
            SimpleSelectConditionStep<VDocumentTypeViewRecord> select = factory.selectFrom(V_DOCUMENT_TYPE)
                                                                               .where(conditions);

            org.jooq.Result<VDocumentTypeViewRecord> result = select.fetch();

            return JooqConverterFacade.convertList(result, Element.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getDocumentWithConditions(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static List<Folder> getFolderWithConditions(JooqDocumentService service, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Factory factory = service.createFactory();
        try {
            SimpleSelectConditionStep<VDirectoryViewRecord> select = factory.selectFrom(V_DIRECTORY)
                                                                            .where(conditions);

            org.jooq.Result<VDirectoryViewRecord> result = select.fetch();

            return JooqConverterFacade.convertList(result, Folder.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getDocumentWithConditions(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    protected static int getDocumentType(Element element) {
        if (element instanceof Folder)
            return 1;
        if (element instanceof EmailDocument)
            return 2;
        if (element instanceof SimpleDocument)
            return 3;
        if (element instanceof GddDocument)
            return 4;
        if (element instanceof LegalDocument)
            return 5;
        return -1;
    }

    protected static Element createElementFromType(int type, UserId userId, TldServiceFacade tld) {
        if (type == 1)
            return new Folder((Path) null, userId, tld);
        if (type == 2)
            return new EmailDocument(userId, tld);
        if (type == 3)
            return new SimpleDocument(userId, tld);
        if (type == 4)
            return new GddDocument(userId, tld);
        if (type == 5)
            return new LegalDocument(userId, tld);
        return null;
    }

    protected static String createDoc(JooqDocumentService service, Element element, Folder parent, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = service.createFactory();
        DocTable table = DocTable.DOC;
        try {
            DocTableRecord record = factory.insertInto(table, table.ID_DOCUMENT, table.ID_DOCUMENT_TYPE, table.IS_ACTIVE, table.ID_DIRECTORY, table.COMMENT_TEXT)
                                           .values(factory.nextval(Sequences.SEQ_DOC_ID), getDocumentType(element), 1, parent.getHandle() + "", element.getComment())
                                           .returning(table.ID_DOCUMENT).fetchOne();

            return record.getIdDocument() + "";
        } catch (Exception e) {
            throw new ServiceException("createDoc(" + element + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    protected static void createFolder(JooqDocumentService service, Folder folder, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = service.createFactory();
        DirectoryTable table = DirectoryTable.DIRECTORY;
        try {
            factory.insertInto(table, table.ID_DIRECTORY, table.LIB_DIRECTORY, table.FULL_PATH)
                   .values(folder.getHandle() + "", folder.getTitle(), folder.getPath().getValue());
        } catch (Exception e) {
            throw new ServiceException("createFolder(" + folder + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    protected static void createDataFile(JooqDocumentService service, Document document, Integer mailId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = service.createFactory();
        DataFileTable table = DataFileTable.DATA_FILE;
        try {
            if (mailId == null) {
                factory.insertInto(table, table.ID_FILE, table.FILE_PATH, table.TITLE, table.ID_MAIL, table.FILE_TYPE, table.SOURCE)
                       .values(document.getHandleAsInt(), document.getFolder().getPath().toString(), document.getTitle(), null, null, document.getSource());
            } else {
                factory.insertInto(table, table.ID_FILE, table.FILE_PATH, table.TITLE, table.ID_MAIL, table.FILE_TYPE, table.SOURCE)
                       .values(document.getHandleAsInt(), document.getFolder().getPath().toString(), document.getTitle(), mailId.intValue(), null, document.getSource());
            }
        } catch (Exception e) {
            throw new ServiceException("createDoc(" + document + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static void createMail(JooqDocumentService service, EmailDocument email, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = service.createFactory();
        MailTable table = MailTable.MAIL;
        SentEmail sentMail = email.getSentEmail();
        try {
            createDoc(service, email, email.getFolder().getPath().getParentFolder(userId, tld), userId, tld);
            factory.insertInto(table, table.ID_MAIL, table.TITLE, table.FROM_METADATA, table.TO_METADATA, table.CC, table.BCC,
                               table.REPLY_TO, table.DATE_SENT, table.CONTENT, table.CONTENT_FORMAT, table.MSG_ID, table.STATUS)
                   .values(email.getHandle() + "", sentMail.getSubject(), sentMail.getFromEmailAddress(), sentMail.getToEmailAddressesAsString(), sentMail.getCcEmailAddressesAsString(), null,
                           sentMail.getReplyTo(), sentMail.getSentDate(), Factory.val(sentMail.getContent(), SQLDataType.CLOB), sentMail.getFormat(), null, null);
        } catch (Exception e) {
            throw new ServiceException("createMail(" + email + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static Folder createFolder(JooqDocumentService service, Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(path, "path");

        if (path.isRoot()) {
            return RootFolder.getRootFolder(userId, tld);
        }
        Factory factory = service.createFactory();

        try {
            Folder f = new Folder(path, userId, tld);
            String dirHandle = createDoc(service, f, path.getParentFolder(userId, tld), userId, tld);
            f.setHandle(dirHandle);
            f.setTitle(path.getTitle());
            createFolder(service, f, userId, tld);
            return f;
        } catch (final Exception e) {
            throw new ServiceException("createFolder( " + path + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static String createGddDocument(JooqDocumentService service, GddDocument document, Integer mailId, UserId userId, TldServiceFacade tld) throws ServiceException {

        Factory factory = service.createFactory();
        FileAuthRequestTable table = FileAuthRequestTable.FILE_AUTH_REQUEST;

        try {
            String dirHandle = createDoc(service, document, document.getFolder().getPath().getParentFolder(userId, tld), userId, tld);
            createDataFile(service, document, mailId, userId, tld);

            factory.insertInto(table, table.ID_FILE, table.DOMAIN_NAME, table.ID_OPERATION, table.REGISTRAR_CODE, table.CONTACT_HANDLE)
                   .values(document.getHandleAsInt(), document.getDomain(), document.getOperationId(userId, tld).getIntValue(),
                           document.getRegistrarCode(), document.getContactHandle());
            return dirHandle;
        } catch (final Exception e) {
            throw new ServiceException("createGddDocument( " + document + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static String createLegalDocument(JooqDocumentService service, LegalDocument document, Integer mailId, UserId userId, TldServiceFacade tld) throws ServiceException {

        Factory factory = service.createFactory();
        FileQualificationTable table = FileQualificationTable.FILE_QUALIFICATION;
        try {
            String dirHandle = createDoc(service, document, document.getFolder().getPath().getParentFolder(userId, tld), userId, tld);
            createDataFile(service, document, mailId, userId, tld);

            factory.insertInto(table, table.ID_FILE, table.DOMAIN_NAME, table.HOLDER_HANDLE, table.INITIATOR_EMAIL, table.QUALIFICATION_SOURCE)
                   .values(document.getHandleAsInt(), document.getDomainName(), document.getHolderHandle(), document.getInitiatorEmail(),
                           document.getQualificationSource());
            return dirHandle;
        } catch (final Exception e) {
            throw new ServiceException("createLegalDocument( " + document + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static String createSimpleDocument(JooqDocumentService service, SimpleDocument document, Integer mailId, UserId userId, TldServiceFacade tld) throws ServiceException {

        Factory factory = service.createFactory();

        try {
            String dirHandle = createDoc(service, document, document.getFolder().getPath().getParentFolder(userId, tld), userId, tld);
            createDataFile(service, document, mailId, userId, tld);

            return dirHandle;
        } catch (final Exception e) {
            throw new ServiceException("createSimpleDocument( " + document + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static Folder getOrCreateTopLevelOperationFolder(final TopLevelOperation topLevelOperation, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (topLevelOperation.isFinished()) {
            return getOrCreateTopLevelOperationArchiveFolder(topLevelOperation, userId, tld);
        } else {
            return getOrCreateTopLevelOperationRunningFolder(topLevelOperation, userId, tld);
        }

    }

    protected static Folder getOrCreateTopLevelOperationArchiveFolder(final TopLevelOperation topLevelOperation, UserId userId, TldServiceFacade tld) throws ServiceException {

        final Date date = new Date();
        final String year = new SimpleDateFormat("yyyy").format(date);
        final String month = new SimpleDateFormat("MM").format(date) + " " + new SimpleDateFormat("MMMM").format(date);

        Folder archiveFolder = Tree.get(userId, tld).getArchive();
        //archiveFolder.

        archiveFolder = AppServiceFacade.getDocumentService().getElementWithHandle(archiveFolder.getHandle(), Folder.class, userId, tld);
        Folder folder = AppServiceFacade.getDocumentService().getOrCreateFolder(archiveFolder.getPath().getValue() + "/" + year + "/" + month, userId, tld);

        return folder;
    }

    protected static Folder getOrCreateTopLevelOperationRunningFolder(final TopLevelOperation topLevelOperation, UserId userId, TldServiceFacade tld) throws ServiceException {

        Folder operationFolder = Tree.get(userId, tld).getOperation();
        operationFolder = AppServiceFacade.getDocumentService().getElementWithHandle(operationFolder.getHandle(), Folder.class, userId, tld);
        Folder folder = AppServiceFacade.getDocumentService().getOrCreateFolder(operationFolder.getPath().getValue() + "/" + topLevelOperation.getIdAsString(), userId, tld);

        return folder;
    }

    public static Folder searchFolder(JooqDocumentService service, Path path, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (path.isRoot()) {
            return RootFolder.getRootFolder(userId, tld);
        }

        Folder parentFolder = AppServiceFacade.getDocumentService().getOrCreateFolder(path.getParent(), userId, tld);
        for (Element element : parentFolder.getElements()) {
            if (element instanceof Folder && StringUtils.equals(element.getTitle(), path.getName())) {
                return (Folder) element;
            }
        }

        return null;
    }

    public static int getNbElementWithConditions(JooqDocumentService service, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Factory factory = service.createFactory();
        try {
            SelectConditionStep select = factory.selectCount().from(V_DOCUMENT_TYPE)
                                                .where(conditions);

            org.jooq.Result<?> result = select.fetch();

            return result.getValueAsInteger(0, 0).intValue();
        } catch (Exception e) {
            throw new ServiceException("getNbElementWithConditions(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }

    public static Integer getElementParent(JooqDocumentService service, UserId userId, TldServiceFacade tld, String handle) throws ServiceException {
        Factory factory = service.createFactory();
        try {
            SimpleSelectConditionStep<VDocumentTypeViewRecord> select = factory.selectFrom(V_DOCUMENT_TYPE)
                                                                               .where(V_DOCUMENT_TYPE.ID_DOCUMENT.equal(Integer.parseInt(handle)));

            VDocumentTypeViewRecord result = select.fetch().get(0);

            return result.getIdDirectory();
        } catch (Exception e) {
            throw new ServiceException("getElementLazy(" + handle + ") failed", e);
        } finally {
            service.closeFactory(factory);
        }
    }
}
