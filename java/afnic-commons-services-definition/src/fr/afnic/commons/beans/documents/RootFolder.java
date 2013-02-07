package fr.afnic.commons.beans.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class RootFolder extends Folder {

    private final static Map<TldServiceFacade, RootFolder> ROOT = new HashMap<TldServiceFacade, RootFolder>();//new RootFolder();

    private List<Element> elements = null;

    private RootFolder(UserId userId, TldServiceFacade tld) {
        super("/", userId, tld);

    }

    public static RootFolder getRootFolder(UserId userId, TldServiceFacade tld) {
        if (ROOT.get(tld) == null) {
            ROOT.put(tld, new RootFolder(userId, tld));
        }
        return ROOT.get(tld);
    }

    @Override
    public List<Element> getElements() throws ServiceException {
        if (this.elements == null) {
            this.populateElements();
        }
        return this.elements;
    }

    private void populateElements() throws ServiceException {
        this.elements = new ArrayList<Element>();

        String mainLegalHandle = AppServiceFacade.getDocumentService().getTree(this.userIdCaller, this.tldCaller).getMainLegal().getHandle();
        this.elements.add(AppServiceFacade.getDocumentService().getElementWithHandle(mainLegalHandle, Folder.class, this.userIdCaller, this.tldCaller));

        String mainOperationHandle = AppServiceFacade.getDocumentService().getTree(this.userIdCaller, this.tldCaller).getMainOperation().getHandle();
        this.elements.add(AppServiceFacade.getDocumentService().getElementWithHandle(mainOperationHandle, Folder.class, this.userIdCaller, this.tldCaller));

    }

}
