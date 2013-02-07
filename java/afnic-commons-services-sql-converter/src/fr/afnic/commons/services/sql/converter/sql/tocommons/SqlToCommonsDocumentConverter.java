package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.documents.DeletedDocument;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnDocumentMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsDocumentConverter extends AbstractConverter<ResultSet, Document> {

    public SqlToCommonsDocumentConverter() {
        super(ResultSet.class, Document.class);
    }

    @Override
    public Document convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {

            //int idOperation = toConvert.getInt(SqlColumnDocumentMapping.idOperation.toString());
            //int idDocument = toConvert.getInt(SqlColumnDocumentMapping.idDocument.toString());
            //int objectVersion = toConvert.getInt(SqlColumnDocumentMapping.objectVersion.toString());

            String handle = toConvert.getString(SqlColumnDocumentMapping.docuShareHandle.toString());
            Document document = null;
            try {
                document = AppServiceFacade.getDocumentService().getDocumentWithHandle(handle, userId, tld);
            } catch (NotFoundException e) {
                document = new DeletedDocument(handle, userId, tld);
            }
            //document.setOperationId(new OperationId(idOperation));

            return document;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

}
