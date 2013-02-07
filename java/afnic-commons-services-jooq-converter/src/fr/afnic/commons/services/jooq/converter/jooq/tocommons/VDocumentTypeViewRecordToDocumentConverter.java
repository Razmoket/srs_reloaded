package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Element;
import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.beans.documents.Folder;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.documents.LegalDocument;
import fr.afnic.commons.beans.documents.Path;
import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.VDocumentTypeViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class VDocumentTypeViewRecordToDocumentConverter extends AbstractConverter<VDocumentTypeViewRecord, Document> {

    public VDocumentTypeViewRecordToDocumentConverter() {
        super(VDocumentTypeViewRecord.class, Document.class);
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

    @Override
    public Document convert(VDocumentTypeViewRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        Element back = createElementFromType(toConvert.getIdDocumentType(), userId, tld);
        back.setHandle(toConvert.getIdDocument() + "");
        back.populate();

        return (Document) back;
    }
}
