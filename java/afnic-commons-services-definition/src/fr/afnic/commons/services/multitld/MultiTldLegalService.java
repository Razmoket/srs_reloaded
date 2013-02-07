package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.legal.LegalData;
import fr.afnic.commons.legal.SyreliLegalData;
import fr.afnic.commons.services.ILegalService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldLegalService implements ILegalService {

    protected MultiTldLegalService() {
        super();
    }

    @Override
    public SyreliLegalData getSyreliComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().getSyreliComplaintData(pNdd, pNumDossier, userId, tld);
    }

    @Override
    public boolean isOkAgtf(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().isOkAgtf(pNdd, userId, tld);
    }

    @Override
    public boolean fillBOAStatus(LegalData pLegalData, String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().fillBOAStatus(pLegalData, pNdd, userId, tld);
    }

    @Override
    public LegalData getComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().getComplaintData(pNdd, pNumDossier, userId, tld);
    }

    @Override
    public void updateComplaintState(String pNumDossier, int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getLegalService().updateComplaintState(pNumDossier, pState, userId, tld);
    }

    @Override
    public void createOrUpdateAgtfState(String pNdd, int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getLegalService().createOrUpdateAgtfState(pNdd, pState, userId, tld);
    }

    @Override
    public boolean checkExistingLegalState(int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().checkExistingLegalState(pState, userId, tld);
    }

    @Override
    public boolean checkExistingNumDossier(String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().checkExistingNumDossier(pNumDossier, userId, tld);
    }

    @Override
    public List<SyreliLegalData> getListSyreliForDomainName(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().getListSyreliForDomainName(pNdd, userId, tld);
    }

    @Override
    public boolean checkExistingSyreliForDomainName(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().checkExistingSyreliForDomainName(pNdd, pNumDossier, userId, tld);
    }

    @Override
    public List<Domain> getDomainAgtfFrozenList(WhoisContact pWhoisContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getLegalService().getDomainAgtfFrozenList(pWhoisContact, userId, tld);
    }
}
