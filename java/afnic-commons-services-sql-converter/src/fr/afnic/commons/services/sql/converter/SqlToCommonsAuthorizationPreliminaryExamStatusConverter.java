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

public class SqlToCommonsAuthorizationPreliminaryExamStatusConverter extends AbstractConverter<Integer, AuthorizationPreliminaryExamStatus> {

    public SqlToCommonsAuthorizationPreliminaryExamStatusConverter() {
        super(Integer.class, AuthorizationPreliminaryExamStatus.class);
    }

    @Override
    public AuthorizationPreliminaryExamStatus convert(Integer toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        switch (toConvert) {
        case 1:
            return AuthorizationPreliminaryExamStatus.Pending;

        case 2:
        case 3:
        case 4:
        case 5:
            return AuthorizationPreliminaryExamStatus.Running;

        case 6:
        case 7:
        case 8:
            return AuthorizationPreliminaryExamStatus.Aborted;

        case 9:
            return AuthorizationPreliminaryExamStatus.Accepted;

        case 10:
            return AuthorizationPreliminaryExamStatus.Rejected;

        case 11:
            return AuthorizationPreliminaryExamStatus.Used;

        default:
            throw new RuntimeException("Cannot convert " + toConvert);
        }

    }

}
