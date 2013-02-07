package fr.afnic.commons.beans.mail.reception;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import fr.afnic.commons.beans.Authentification;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.smtp.SentEmailFactory;

/**
 * Boite mail utilisant le protocole Imap.
 * 
 * 
 * @author ginguene
 * 
 */
public class ImapEmailBox implements IEmailBox {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(ImapEmailBox.class);
    private static final String DEFAULT_FOLDER_NAME = "INBOX";

    private final String protocole = "imap";
    private final Authentification authentification;

    private final String host;
    private final int port;
    private Session session;
    private Store store;
    private Folder folder;

    private String folderName = null;

    public ImapEmailBox(Authentification authentification, String host, int port) throws EmailBoxException {
        this.authentification = authentification;
        this.host = host;
        this.port = port;
    }

    /**
     * ouvre la connection ainsi que le repertoire principale des mails
     * 
     * @return
     * @throws EmailBoxException
     */
    private void connect() throws EmailBoxException {
        this.createSession();
        this.createStore();
        this.connectStore();
        this.openMailBox();
    }

    private void createSession() {
        LOGGER.debug("Connect to " + this.host + ":" + this.port + " with user " + this.authentification.getUserName());

        this.session = Session.getDefaultInstance(this.getProperties(), null);
        // this.session.setDebug(LOGGER.isDebugEnabled());

    }

    private void createStore() throws EmailBoxException {
        this.store = null;
        try {
            this.store = this.session.getStore(this.protocole);
        } catch (NoSuchProviderException e) {
            throw new EmailBoxException("Cannot create store", e);
        }
    }

    private void connectStore() throws EmailBoxException {
        try {
            this.store = this.session.getStore(this.protocole);
            this.store.connect(this.host, this.port, this.authentification.getUserName(), this.authentification.getPassword());
        } catch (MessagingException e) {
            throw new EmailBoxException("Connection failed", e);
        }
    }

    private Properties getProperties() {
        Properties props = System.getProperties();

        props.setProperty("mail.store.protocol", this.protocole);
        props.setProperty("mail.imap.host", this.host);
        props.setProperty("mail.imap.auth", "true");
        props.setProperty("mail.store.protocol", this.protocole);

        return props;
    }

    /**
     * ferme la session
     * 
     * @return
     */
    public void disconnect() throws EmailBoxException {
        try {
            // fermeture du store
            if (this.folder != null) {
                this.folder.expunge();
            }

            if (this.store != null) {
                this.store.close();
            }
        } catch (Exception e) {
            throw new EmailBoxException("Disconnection failed", e);
        }
    }

    /**
     * ouverture d'un répertoire
     * 
     * 
     * @param mailbox
     * @return
     * @throws EmailBoxException
     */
    private void openMailBox() throws EmailBoxException {

        this.createFolder();
        this.openFolder();
    }

    private void createFolder() throws EmailBoxException {
        try {
            if (this.folderName == null) {
                this.folderName = ImapEmailBox.DEFAULT_FOLDER_NAME;
            }

            Folder storeDefaultFolder = this.store.getDefaultFolder();
            if (storeDefaultFolder == null) {
                throw new EmailBoxException("No default mailbox defined");
            }

            this.folder = storeDefaultFolder.getFolder(this.folderName);

            if (this.folder == null) {
                throw new EmailBoxException("Folder " + this.folderName + " not found");
            }
        } catch (MessagingException e) {
            throw new EmailBoxException("Cannot create folder " + this.folderName + " not found", e);
        }
    }

    private void openFolder() throws EmailBoxException {
        try {
            this.folder.open(Folder.READ_WRITE);
            LOGGER.debug("Folder " + this.folder.getName() + " opened");
        } catch (MessagingException e) {
            throw new EmailBoxException("[process_folder]: cannot open folder "
                                        + this.folderName, e);
        }
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public int getUnreadEmailCount() throws EmailBoxException {

        this.connect();
        int nbMessage;
        try {
            nbMessage = this.folder.getUnreadMessageCount();
        } catch (MessagingException e) {
            throw new EmailBoxException("getUnreadMailCount() failed", e);
        }
        this.disconnect();
        return nbMessage;
    }

    @Override
    public int getEmailCount() throws EmailBoxException {

        this.connect();
        int nbMessage;
        try {
            nbMessage = this.folder.getMessageCount();
        } catch (MessagingException e) {
            throw new EmailBoxException("getMailCount() failed", e);
        }
        this.disconnect();
        return nbMessage;
    }

    @Override
    public List<SentEmail> getUnreadEmails() throws EmailBoxException {
        return this.getMailsCorrespondingToSearchTerm(new NotSeenEmailSearchTerm());
    }

    @Override
    public List<SentEmail> getEmails() throws EmailBoxException {
        return this.getMailsCorrespondingToSearchTerm(null);
    }

    @Override
    public void deleteEmail(SentEmail mail) throws EmailBoxException {
        this.deleteEmail(mail.getMessageId());
    }

    @Override
    public void deleteEmail(String messageId) throws EmailBoxException {
        try {
            this.connect();
            Message message = this.getMessageWithMessageId(messageId);
            if (message == null) {
                LOGGER.warn("delete mail failed : le mail n'existe pas.");
                return;
            }
            message.setFlag(Flag.DELETED, true);
            this.folder.expunge();
            this.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailBoxException("deleteMail(" + messageId + ") failed", e);

        }
    }

    @Override
    public void setAsReadMail(SentEmail mail) throws EmailBoxException {
        this.setAsReadMail(mail.getMessageId());
    }

    @Override
    public void setAsReadMail(String messageId) throws EmailBoxException {
        try {
            this.connect();
            Message message = this.getMessageWithMessageId(messageId);
            message.setFlag(Flag.SEEN, true);
            this.disconnect();
        } catch (Exception e) {
            throw new EmailBoxException("setAsReadMail(" + messageId + ") failed", e);
        }
    }

    private Message getMessageWithMessageId(String messageId) throws EmailBoxException, MessagingException {
        Message[] messages = this.getMessagesFromFolderCorrespondingToSearchTerm(new EmailIdSearchTerm(messageId));
        if (messages != null && messages.length == 1) {
            return messages[0];
        } else {
            return null;
        }

    }

    private List<SentEmail> getMailsCorrespondingToSearchTerm(SearchTerm searchTerm) throws EmailBoxException {
        this.connect();
        List<SentEmail> result = this.getUnreadMailsFromFolderCorrespondingToSearchTerm(searchTerm);
        this.disconnect();
        return result;
    }

    /***
     * Retourne tous les message du repertoire principale correspondant au critere de recherche. Si le critere de recherche de recherche est null,
     * retourne tous les mails du répertoire.
     * 
     * @param searchTerm
     * @return
     * @throws EmailBoxException
     */
    private List<SentEmail> getUnreadMailsFromFolderCorrespondingToSearchTerm(SearchTerm searchTerm) throws EmailBoxException {
        try {
            List<SentEmail> mails = new ArrayList<SentEmail>();

            Message[] messages = this.getMessagesFromFolderCorrespondingToSearchTerm(searchTerm);
            for (Message message : messages) {
                SentEmail mail = SentEmailFactory.createSentMail(message);
                mails.add(mail);

            }

            return mails;
        } catch (Exception e) {
            throw new EmailBoxException("Error while getNewMailsFromMessagesFolder()", e);
        }
    }

    private Message[] getMessagesFromFolderCorrespondingToSearchTerm(SearchTerm searchTerm) throws EmailBoxException, MessagingException {

        if (searchTerm != null) {
            return this.folder.search(searchTerm);
        } else {
            return this.folder.getMessages();
        }
    }

}
