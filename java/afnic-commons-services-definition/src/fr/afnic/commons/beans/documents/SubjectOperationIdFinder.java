package fr.afnic.commons.beans.documents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Classe chargéé de trouver un operationId en parsant le sujet d'un mail d'un emailDocument
 * 
 * @author ginguene
 *
 */
public class SubjectOperationIdFinder {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SubjectOperationIdFinder.class);

    private final EmailDocument emailDocument;
    private final String subject;

    private static final Pattern SUBJECT_PATTERN = Pattern.compile(".*\\[(\\d*)\\]*.*", Pattern.CASE_INSENSITIVE);

    public SubjectOperationIdFinder(EmailDocument emailDocument) {
        this.emailDocument = emailDocument;

        if (emailDocument.hasSentEmail()) {
            this.subject = this.emailDocument.getSentEmail().getSubject();
        } else {
            this.subject = null;
        }

    }

    protected OperationId findOperationId(UserId userId, TldServiceFacade tld) throws ServiceException {

        if (this.hasNotSubject()) {
            return null;
        }

        Matcher matcher = this.SUBJECT_PATTERN.matcher(this.subject);

        // si recherche fructueuse
        if (matcher.matches()) {
            if (matcher.groupCount() == 1) {
                String operationIdAsString = matcher.group(1);
                try {
                    int operationIdAsInt = Integer.parseInt(operationIdAsString);
                    OperationId operationId = new OperationId(operationIdAsInt);

                    if (operationId.isExisting(userId, tld)) {
                        return operationId;
                    } else {
                        LOGGER.warn("Found operationId " + operationIdAsString + "  in subject  EmailDocument " + this.emailDocument.getHandle()
                                    + " but it does not correspond to an operation [subject:" + this.subject + "]");
                    }

                } catch (NumberFormatException e) {
                    LOGGER.warn("Found operationId " + operationIdAsString + "  in subject  EmailDocument " + this.emailDocument.getHandle()
                                + " but it is not a long [subject:" + this.subject + "]");
                }
            }
        } else {
            LOGGER.warn("No operationId found  in subject  EmailDocument " + this.emailDocument.getHandle() + " but it is not a long [subject:" + this.subject + "]");
        }

        return null;
    }

    private boolean hasNotSubject() {
        return StringUtils.isNotBlank(this.subject);
    }

}
