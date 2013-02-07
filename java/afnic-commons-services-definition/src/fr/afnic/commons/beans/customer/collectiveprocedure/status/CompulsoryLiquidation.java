/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.collectiveprocedure.status;

import java.util.Collections;
import java.util.List;

/**
 * Liquidation judiciaire.
 * 
 * @author ginguene
 * 
 */
public class CompulsoryLiquidation extends CollectiveProcedureStatus {

    private static final long serialVersionUID = 1L;

    public CompulsoryLiquidation() {
        super("Liquidation judiciaire");
    }

    @Override
    public List<Class<? extends CollectiveProcedureStatus>> getNextAllowedStatus() {
        return Collections.emptyList();
    }

}
