/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.collectiveprocedure;

import java.io.Serializable;
import java.util.Comparator;

import fr.afnic.commons.beans.customer.collectiveprocedure.status.Closed;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.CollectiveProcedureStatus;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.CompulsoryLiquidation;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.Receivership;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.SavingProcedure;

public class CollectiveProcedureStatusComparator implements Comparator<CollectiveProcedureStatus>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(CollectiveProcedureStatus arg0, CollectiveProcedureStatus arg1) {
        return getStatusValue(arg1) - getStatusValue(arg0);
    }

    private int getStatusValue(CollectiveProcedureStatus status) {
        if (status instanceof Closed) return 1;
        if (status instanceof CompulsoryLiquidation) return 2;
        if (status instanceof Receivership) return 3;
        if (status instanceof SavingProcedure) return 4;
        return 0;

    }

}
