package fr.afnic.commons.beans.list;

import org.apache.commons.lang.NotImplementedException;

import fr.afnic.commons.beans.statistics.StatisticsView;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.utils.Preconditions;

public class QualificationStatResultList extends ResultList<StatisticsView> {

    public QualificationStatResultList(StatisticsView view) {
        this.view = Preconditions.checkNotNull(view, "view");
    }

    public QualificationStatResultList() {
    }

    @Override
    public CharSequence createStream() throws ServiceException {
        throw new NotImplementedException();
    }

}
