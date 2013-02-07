package fr.afnic.utils;

import java.security.Security;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Classe permettant de gerer les accès à une boite IMAP. Assez basique, elle ne demande qu'à évoluer :)
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class ImapManager {

    private final String login;
    private final String password;
    private final String protocole;
    private final String host;
    private final int port;
    private Session session;
    private Store store;
    private Folder folder;
    private Message[] mailBox;

    private boolean isInitialized = false;

    public ImapManager(final String login, final String password, final String host, final int port) {
        this.host = host;
        this.login = login;
        this.password = password;
        this.protocole = "imap";
        this.port = port;
    }

    /**
     * initialize la session
     * 
     * @return
     */
    private boolean initialize() {

        final Properties props = System.getProperties();

        // création d'une session
        props.put("mail.imap.port", Integer.toString(this.port));
        props.put("mail.imap.user", this.login);
        props.put("mail.imap.host", this.host);
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.pwd", this.password);
        props.put("mail.imap.starttls.enable", "true");
        props.put("mail.imap.starttls.required", "true");
        props.put("mail.imap.auth", "true");

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        java.security.Security.setProperty("ssl.SocketFactory.provider", "DummySSLSocketFactory");

        // IMAP provider
        this.session = Session.getDefaultInstance(props, null);
        this.session.setDebug(true);

        // création d'un objet d'enregistrement de message
        this.store = null;
        try {
            this.store = this.session.getStore(this.protocole);
        } catch (final Exception e) {
            return false;
        }

        // Connection
        try {
            this.store.connect(this.host, this.port, this.login, this.password);
        } catch (final MessagingException e) {
            return false;
        }

        this.isInitialized = true;
        this.openMailBox(null);
        return true;
    }

    /**
     * ferme la session
     * 
     * @return
     */
    public boolean close() {

        if (this.isInitialized) {
            this.isInitialized = false;
            try {
                // fermeture du store
                if (this.folder != null) {
                    this.folder.expunge();
                }

                if (this.store != null) {
                    this.store.close();
                }
            } catch (final Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * ouverture d'un répertoire
     * 
     * 
     * @param mailbox
     * @return
     */
    private boolean openMailBox(final String mailbox) {

        // ouverture du répertoire courant INBOX
        this.folder = null;
        try {
            this.folder = this.store.getDefaultFolder();

            if (this.folder == null) {
                return false;
            }

            // par défaut on récupère les messages dans INBOX
            this.folder = this.folder.getFolder("INBOX");
            if (this.folder == null) {
                return false;
            }

            try {
                this.folder.open(Folder.READ_WRITE);
            } catch (final MessagingException e) {

            }
        } catch (final MessagingException e) {
            return false;
        } catch (final IllegalStateException e) {
            return false;
        }

        try {
            this.mailBox = new Message[this.folder.getMessageCount()];
            this.mailBox = this.folder.getMessages();
        } catch (final MessagingException e) {

        }

        return true;
    }

    /**
     * Return number of message received. If an error occured, return -1
     * 
     * @return
     */
    public int getNbMessage() {

        if (!this.isInitialized) {
            if (!this.initialize()) {
                return -1;
            }
        }

        int nbMessage;
        try {
            nbMessage = this.folder.getMessageCount();
        } catch (final MessagingException e) {
            return -1;
        }
        this.close();
        return nbMessage;
    }

    /**
     * Return the number of unread message in the mailbox. If an error occured, return -1
     * 
     * @return
     */
    public int getNbMessageUnread() {
        if (!this.isInitialized) {
            if (!this.initialize()) {
                return -1;
            }
        }

        int nbMessage;
        try {
            nbMessage = this.folder.getUnreadMessageCount();
        } catch (final MessagingException e) {
            return -1;
        }
        this.close();
        return nbMessage;
    }

    /**
     * Return the number of new message in the mailbox. If an error occured, return -1
     * 
     * @return
     */
    public int getNbNewMessage() {
        if (!this.isInitialized) {
            if (!this.initialize()) {
                return -1;
            }
        }

        int nbMessage;
        try {
            nbMessage = this.folder.getNewMessageCount();
        } catch (final MessagingException e) {
            return -1;
        }

        this.close();
        return nbMessage;
    }

    /**
     * Return the number of read message in the mailbox.
     * 
     * @return
     */
    public int nbMessageRead() {
        return (this.getNbMessage() - this.getNbMessageUnread());
    }

    /**
     * Return the number of old message in the mailbox.
     * 
     * @return
     */
    public int nbOldMessage() {
        return (this.getNbMessage() - this.getNbNewMessage());
    }

    /**
     * Efface un message dont on precise le numero
     * 
     * 
     * @param num
     * @return
     */
    public boolean deleteMessage(final int num) {
        try {
            this.mailBox[num].setFlag(Flags.Flag.DELETED, true);
            return true;
        } catch (final MessagingException e) {
            return false;
        }
    }

    /**
     * Retourne le sujet d'un message dont on précise le numero
     * 
     * @param num
     * @return
     */
    public Message getMail(final int num) {
        return this.mailBox[num];
    }

    /**
     * Retourne la liste des nouveaux mails recues
     * 
     * @return
     */
    public Message[] getNewMail() {
        this.close();
        if (!this.isInitialized) {
            if (!this.initialize()) {
                return null;
            }
        }
        Message[] messages = null;

        try {
            messages = this.folder.getMessages();

        } catch (final MessagingException e) {
        }
        return messages;
    }

    /**
     * Sera peut etre utilisé pour se connecter en TLS
     * 
     * @author ginguene
     * 
     */
    /*
     * private static class PasswordAuthenticator extends Authenticator {
     * 
     * private final String password; private final String login;
     * 
     * public PasswordAuthenticator(String login, String password) { this.login = login; this.password = password; }
     * 
     * @Override public PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(this.login, this.password); } }
     */
}
