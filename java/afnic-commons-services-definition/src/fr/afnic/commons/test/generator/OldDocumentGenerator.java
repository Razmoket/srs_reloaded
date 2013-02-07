/*
 * $Id: DocumentGenerator.java,v 1.2 2010/07/08 09:54:47 alaphil Exp $
 * $Revision: 1.2 $
 * $Author: alaphil $
 */

package fr.afnic.commons.test.generator;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import fr.afnic.commons.beans.IRequestOperation;
import fr.afnic.commons.beans.documents.DocumentSource;
import fr.afnic.commons.beans.documents.FolderType;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;

/**
 * Crée des documents de test.
 * 
 * @author ginguene
 * 
 */
public final class OldDocumentGenerator {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(OldDocumentGenerator.class);

    /** Répertoire temporaire utilisé pour créer les fichiers correspondant aux documents créés */
    public static final String TMP_DIR_PATH = "./build/tmp";

    public static final String INVALID_DOCUMENT_HANDLE = "invalidDocumentHandle";

    /**
     * Empeche l'instanciation de cette classe utilitaire.
     * 
     */
    private OldDocumentGenerator() {
    }

    public static String getInvalidDocumentHandle() {
        return OldDocumentGenerator.INVALID_DOCUMENT_HANDLE;
    }

    /**
     * Création d'un document GDD dans le répertoire 'En cours'.
     * 
     * @param operation
     *            Operation utilisé pour les meta-données du document.
     * 
     * @param domain
     *            Domaine utilisé pour les meta-données du document.
     * 
     * @param handle
     *            Handle utilisé pour les meta-données du document.
     * 
     * @param registrarCode
     *            Code du bureau d'enregistrement utilisé pour les meta-données du document.
     * 
     * @return Le document créé dans la GED.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static GddDocument createDocumentInRunningFolder(IRequestOperation operation, String domain, String handle, String registrarCode, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                 throws GeneratorException {
        return OldDocumentGenerator.createGddDocumentInFolder(operation, domain, handle, registrarCode, FolderType.Running, userId, tld);
    }

    /**
     * Création d'un document GDD dans la boite de récéption.
     * 
     * @param operation
     *            Operation utilisé pour les meta-données du document.
     * 
     * @param domain
     *            Domaine utilisé pour les meta-données du document.
     * 
     * @param handle
     *            Handle utilisé pour les meta-données du document.
     * 
     * @param registrarCode
     *            Code du bureau d'enregistrement utilisé pour les meta-données du document.
     * 
     * @return Le document créé dans la GED.
     * 
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static GddDocument createDocumentInInboxFolder(IRequestOperation operation, String domain, String handle, String registrarCode, UserId userId, TldServiceFacade tld)
                                                                                                                                                                               throws GeneratorException {
        return OldDocumentGenerator.createGddDocumentInFolder(operation, domain, handle, registrarCode, FolderType.Inbox, userId, tld);
    }

    /**
     * Création d'un document GDD dans un répertoire particulier.
     * 
     * @param operation
     *            Operation utilisé pour les meta-données du document.
     * 
     * @param domain
     *            Domaine utilisé pour les meta-données du document.
     * 
     * @param handle
     *            Handle utilisé pour les meta-données du document.
     * 
     * @param registrarCode
     *            Code du bureau d'enregistrement utilisé pour les meta-données du document.
     * 
     * @param folder
     *            Répertoire dans lequel on souhaite créer le document.
     * 
     * @return Le document créé dans la GED.
     * 
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static GddDocument createGddDocumentInFolder(IRequestOperation operation, String domain, String handle, String registrarCode, FolderType folder, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                throws GeneratorException {

        try {
            GddDocument document = new GddDocument(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            document.setRequestOperation(operation);
            document.setDomain(domain);
            document.setRegistrarCode(registrarCode);
            document.setContactHandle(handle);
            document.setReceptionDate(new Date());
            document.setSource(DocumentSource.Fax);
            File createNewTmpFile = OldDocumentGenerator.createNewTmpFile();
            document.setFileName(createNewTmpFile.getCanonicalPath());

            String title = AppServiceFacade.getOldDocumentService().getNormalizedTitle(document, userId, tld);
            document.setTitle(title);

            AppServiceFacade.getOldDocumentService().addGddDocumentInFolder(document, folder, userId, tld);
            if (createNewTmpFile.delete()) {
                OldDocumentGenerator.LOGGER.debug("delete " + createNewTmpFile.getName());
            } else {
                OldDocumentGenerator.LOGGER.warn("failed to delete " + createNewTmpFile.getName());
            }
            return document;
        } catch (Exception e) {
            throw new GeneratorException("createGddDocumentInFolder() failed with document ", e);
        }
    }

    /**
     * Crée un document non typé dans la GED dans le répertoire passé en parametre.
     * 
     * @param folder
     *            Répertoire dans lequel on souhaite créer le document.
     * 
     * @return Le document créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     * @throws IOException
     */
    public static SimpleDocument createSimpleDocumentInFolder(FolderType folder, UserId userId, TldServiceFacade tld) throws GeneratorException {

        try {
            SimpleDocument document = new SimpleDocument(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            document.setReceptionDate(new Date());
            File createNewTmpFile = OldDocumentGenerator.createNewTmpFile();
            document.setFileName(createNewTmpFile.getCanonicalPath());
            document.setTitle("my title");
            document.setSource(DocumentSource.Fax);

            AppServiceFacade.getOldDocumentService().addSimpleDocumentInFolder(document, folder, userId, tld);

            if (createNewTmpFile.delete()) {
                OldDocumentGenerator.LOGGER.debug("Delete " + createNewTmpFile.getCanonicalPath() + " succed");
            } else {
                OldDocumentGenerator.LOGGER.debug("Delete " + createNewTmpFile.getCanonicalPath() + " failed");
            }

            return document;
        } catch (Exception e) {
            throw new GeneratorException("createGddDocumentInFolder() failed with document ", e);
        }
    }

    /**
     * Crée un document Gdd correspondant à un fax mais ne le stocke pas dans la ged, c'est juste un simple bean qui est créé.
     * @throws ServiceException 
     * 
     */
    public static GddDocument createBeanFaxGddDocument() throws ServiceException {
        GddDocument document = new GddDocument(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        document.setTitle("title " + System.currentTimeMillis());

        try {
            document.setFileName(OldDocumentGenerator.createNewTmpFile().getAbsolutePath());
        } catch (GeneratorException e) {
            e.printStackTrace();
        }

        document.setSource(DocumentSource.Fax);
        return document;
    }

    /**
     * Cree un nouveau fichier temporaire et retourne son nom complet.
     * 
     * @return Le nom complet du fichier créé.
     * 
     * @throws GeneratorException
     *             Si la génération du jeu de test échoue.
     */
    public static File createNewTmpFile() throws GeneratorException {
        try {
            if (new File(OldDocumentGenerator.TMP_DIR_PATH).mkdir()) {
                OldDocumentGenerator.LOGGER.debug("mkdir " + OldDocumentGenerator.TMP_DIR_PATH + " succed");
            } else {
                OldDocumentGenerator.LOGGER.debug("mkdir " + OldDocumentGenerator.TMP_DIR_PATH + " already existed");
            }

            File file = new File(OldDocumentGenerator.TMP_DIR_PATH + File.separator + "requestMaker_test_file" + System.currentTimeMillis());

            if (file.createNewFile()) {
                OldDocumentGenerator.LOGGER.debug("create" + file.getName() + " succed");
            } else {
                OldDocumentGenerator.LOGGER.warn("create" + file.getName() + " failed");
            }

            return file;
        } catch (Exception e) {
            throw new GeneratorException("createNewTmpFile() failed", e);
        }
    }
}
