package fr.afnic.commons.beans.export;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * ResultList qui n'est pas lié à une vue.
 * Utilisée pour éviter de charger les données des exports avant que l'utilisateur ne clique sur le bouton d'export.
 * Non testé pour laffichage wicket
 * 
 *
 */
public class LazyExportResultList extends ExportResultList {

    private static final long serialVersionUID = 1L;

    private transient ExportResultList resultList;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public LazyExportResultList(ExportView view, UserId userId, TldServiceFacade tld) {
        super(view);
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    @Override
    public CharSequence createStream() throws ServiceException {
        if (this.resultList == null) {
            this.resultList = (ExportResultList) AppServiceFacade.getResultListService().getResultList(this.view, this.userIdCaller, this.tldCaller);
        }
        return this.resultList.createStream();
    }

    @Override
    public String getViewComments() {
        return this.view.getComments();
    }

    @Override
    public String getViewName() {
        return this.view.getName();
    }
}
