/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.collectiveprocedure.status;

import java.util.Arrays;
import java.util.List;

public class SavingProcedure extends CollectiveProcedureStatus {

    private static final long serialVersionUID = 1L;

    public SavingProcedure() {
        super("Procédure de sauvegarde");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Class<? extends CollectiveProcedureStatus>> getNextAllowedStatus() {
        return Arrays.asList(Closed.class,
                             Receivership.class,
                             CompulsoryLiquidation.class);
    }

}
