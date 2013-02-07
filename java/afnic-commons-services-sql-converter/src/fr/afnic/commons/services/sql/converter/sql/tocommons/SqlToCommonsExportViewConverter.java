package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.export.ExportView;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnExportViewMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsExportViewConverter extends AbstractConverter<ResultSet, ExportView> {

    public SqlToCommonsExportViewConverter() {
        super(ResultSet.class, ExportView.class);
    }

    @Override
    public ExportView convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        ExportView view = null;
        try {
            String name = toConvert.getString(SqlColumnExportViewMapping.name.toString());
            String comments = toConvert.getString(SqlColumnExportViewMapping.comments.toString());
            view = new ExportView(name, comments);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return view;
    }
}
