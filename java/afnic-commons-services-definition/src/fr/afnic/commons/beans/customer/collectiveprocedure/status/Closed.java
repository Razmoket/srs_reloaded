/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.collectiveprocedure.status;

import java.util.Collections;
import java.util.List;

public class Closed extends CollectiveProcedureStatus {

    private static final long serialVersionUID = 1L;

    public Closed() {
        super("Fermé");
    }

    @Override
    public List<Class<? extends CollectiveProcedureStatus>> getNextAllowedStatus() {
        return Collections.emptyList();
    }

}
