/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.converter;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExamStatus;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CommonsToSqlAuthorizationPreliminaryExamStatusConverter extends AbstractConverter<AuthorizationPreliminaryExamStatus, Integer> {

    public CommonsToSqlAuthorizationPreliminaryExamStatusConverter() {
        super(AuthorizationPreliminaryExamStatus.class, Integer.class);
    }

    @Override
    public Integer convert(AuthorizationPreliminaryExamStatus toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        switch (toConvert) {
        case Pending:
            return 1;

        case Running:
            return 2;

        case Aborted:
            return 6;

        case Accepted:
            return 9;

        case Rejected:
            return 10;

        case Used:
            return 11;

        default:
            throw new RuntimeException("Cannot convert " + toConvert);
        }

    }

}
