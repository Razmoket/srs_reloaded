/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.collectiveprocedure.status;

import java.util.Arrays;
import java.util.List;

/**
 * Procedure de redressement judiciaire
 * 
 * @author ginguene
 * 
 */
public class Receivership extends CollectiveProcedureStatus {

    private static final long serialVersionUID = 1L;

    public Receivership() {
        super("Redressement judiciaire");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Class<? extends CollectiveProcedureStatus>> getNextAllowedStatus() {
        return Arrays.asList(Closed.class,
                             CompulsoryLiquidation.class);
    }

}
