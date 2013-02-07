package fr.afnic.commons.beans.customer.status;

import java.io.Serializable;

import fr.afnic.commons.beans.customer.termination.TerminationType;
import fr.afnic.commons.beans.history.HistoryEvent;

public class Termination extends HistoryEvent implements Serializable {

    private static final long serialVersionUID = 2411506184512269355L;

    private TerminationType type;

    public TerminationType getType() {
        return type;
    }

    public void setType(TerminationType type) {
        this.type = type;
    }

}
