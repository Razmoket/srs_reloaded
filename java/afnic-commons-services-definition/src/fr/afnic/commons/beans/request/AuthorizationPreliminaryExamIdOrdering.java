/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.request;

import java.util.List;

import com.google.common.collect.Ordering;

/**
 * 
 * Classe permettant de trier les AuthorizationPreliminaryExamOrdering en positionnant les id les moins elevé en première position.
 * 
 * 
 */
public final class AuthorizationPreliminaryExamIdOrdering extends Ordering<AuthorizationPreliminaryExam> {

    private static final AuthorizationPreliminaryExamIdOrdering INSTANCE = new AuthorizationPreliminaryExamIdOrdering();

    private AuthorizationPreliminaryExamIdOrdering() {

    }

    public static List<AuthorizationPreliminaryExam> sortListById(List<AuthorizationPreliminaryExam> list) {
        return AuthorizationPreliminaryExamIdOrdering.INSTANCE.nullsLast().sortedCopy(list);
    }

    @Override
    public int compare(AuthorizationPreliminaryExam arg0, AuthorizationPreliminaryExam arg1) {
        return arg0.getId() - arg1.getId();
    }

}
