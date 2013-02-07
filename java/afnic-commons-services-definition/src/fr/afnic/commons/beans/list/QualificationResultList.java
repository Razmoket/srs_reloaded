package fr.afnic.commons.beans.list;

import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.utils.Preconditions;

public class QualificationResultList extends ResultList<OperationView> {

    public QualificationResultList(OperationView view) {
        this.view = Preconditions.checkNotNull(view, "view");
    }

    public QualificationResultList() {
        this.view = OperationView.None;
    }

    @Override
    public CharSequence createStream() throws ServiceException {
        throw new NotImplementedException();
    }

}
