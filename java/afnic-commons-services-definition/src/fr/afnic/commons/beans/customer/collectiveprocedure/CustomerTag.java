package fr.afnic.commons.beans.customer.collectiveprocedure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.afnic.commons.beans.customer.collectiveprocedure.status.CollectiveProcedureStatus;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.CompulsoryLiquidation;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.Receivership;
import fr.afnic.commons.beans.customer.collectiveprocedure.status.SavingProcedure;
import fr.afnic.commons.beans.history.HistoryEvent;
import fr.afnic.utils.ToStringHelper;

/**
 * Objet de représentation d'une procédure collective d'un customer.
 * 
 * @author alaphilippe
 * 
 */
public class CustomerTag extends HistoryEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CollectiveProcedureStatus> history = new ArrayList<CollectiveProcedureStatus>();

    public boolean hasNotFinalStatus() {
        return !hasFinalStatus();
    }

    public boolean hasFinalStatus() {
        return this.getStatus().isFinalStatus();
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends CollectiveProcedureStatus>> getNextAllowedStatus() {
        if (this.getStatus() != null) {
            return this.getStatus().getNextAllowedStatus();
        } else {
            return Arrays.asList(SavingProcedure.class, Receivership.class, CompulsoryLiquidation.class);
        }
    }

    public CollectiveProcedureStatus getStatus() {
        List<CollectiveProcedureStatus> history = this.getHistory();
        if (history.isEmpty()) {
            return null;
        } else {
            return history.get(history.size() - 1);
        }
    }

    public List<CollectiveProcedureStatus> getHistory() {
        Collections.sort(this.history, new CollectiveProcedureStatusComparator());
        return this.history;
    }

    public void setHistory(List<CollectiveProcedureStatus> history) {
        this.history = history;
    }

    public void addHistoryEvent(CollectiveProcedureStatus status) {
        history.add(status);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }
}
