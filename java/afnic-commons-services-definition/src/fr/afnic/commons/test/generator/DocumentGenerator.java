package fr.afnic.commons.test.generator;

import java.io.File;
import java.io.IOException;

import fr.afnic.commons.beans.documents.DocumentSource;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class DocumentGenerator {

    private DocumentGenerator() {

    }

    public static SimpleDocument generateSimpleDocument() throws ServiceException {
        SimpleDocument doc = new SimpleDocument(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        doc.setComment("Commentaire de test unitaire.");
        doc.setSource(DocumentSource.Fax);
        doc.setTitle("Document de test unitaire");
        doc.setSender("boa+docsender@afnic.fr");
        doc.setFileName(getFileName());

        return doc;
    }

    public static SimpleDocument createSimpleDocument() throws ServiceException {
        SimpleDocument simpleDocument = generateSimpleDocument();

        Tree tree = AppServiceFacade.getDocumentService().getTree(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        String docHandle = AppServiceFacade.getDocumentService().createDocument(simpleDocument, tree.getUnitTest(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        simpleDocument.setHandle(docHandle);
        return simpleDocument;

    }

    private static String getFileName() throws ServiceException {
        File file = new File("./build/tmp/unitaire.test");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
