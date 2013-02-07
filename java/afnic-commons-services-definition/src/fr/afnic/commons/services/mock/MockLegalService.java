package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contact.IndividualAndLegalStructure;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.legal.AskedAction;
import fr.afnic.commons.legal.LegalData;
import fr.afnic.commons.legal.SyreliLegalData;
import fr.afnic.commons.services.ILegalService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class MockLegalService implements ILegalService {

    @Override
    public LegalData getComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        LegalData vLegalData = new LegalData();
        vLegalData.setSyreliLegalData(AppServiceFacade.getLegalService().getSyreliComplaintData(pNdd, pNumDossier, userId, tld));
        vLegalData.setOkAGTF(AppServiceFacade.getLegalService().isOkAgtf(pNdd, userId, tld));
        vLegalData.setOkBOA(AppServiceFacade.getLegalService().fillBOAStatus(vLegalData, pNdd, userId, tld));
        return vLegalData;
    }

    @Override
    public void updateComplaintState(String pNumDossier, int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNumDossier, "pNumDossier");
        Preconditions.checkNotEmpty(pNumDossier, "pNumDossier");
        if (!this.checkExistingLegalState(pState, userId, tld))
            throw new IllegalArgumentException("pState must be a defined value in legal database.");
    }

    @Override
    public SyreliLegalData getSyreliComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotNull(pNumDossier, "pNumDossier");
        Preconditions.checkNotEmpty(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNumDossier, "pNumDossier");

        SyreliLegalData vSyreliLegalData = new SyreliLegalData();
        PostalAddress vPostalAddrReq = new PostalAddress(userId, tld);
        PostalAddress vPostalAddrRep = new PostalAddress(userId, tld);

        IndividualIdentity vIdReq = new IndividualIdentity();
        IndividualIdentity vIdRep = new IndividualIdentity();

        vIdReq.setFirstName("Robert");
        vIdReq.setLastName("Redford");
        IndividualAndLegalStructure vIndividuReq = new IndividualAndLegalStructure(vIdReq, userId, tld);

        vIdRep.setFirstName("Jean-François");
        vIdRep.setLastName("Copé");
        IndividualAndLegalStructure vIndividuRep = new IndividualAndLegalStructure(vIdRep, userId, tld);

        vPostalAddrReq.setCity("Los Angeles");
        vPostalAddrReq.setPostCode("32521");
        vPostalAddrReq.setCountryCode("US");
        vPostalAddrReq.setStreet("36251 51th avenue", null, null);

        vPostalAddrRep.setCity("Paris");
        vPostalAddrRep.setPostCode("75016");
        vPostalAddrRep.setCountryCode("FR");
        vPostalAddrRep.setStreet("5 allée des lilas", "batiment gauche", null);

        vIndividuReq.setOrganization(null);
        vIndividuReq.setVatnumber(null);
        vIndividuReq.setCivility("M");
        vIndividuReq.getPhoneNumbers().add(new PhoneNumber("+316666666666"));
        vIndividuReq.getFaxNumbers().add(new PhoneNumber("+316666666667"));
        vIndividuReq.addEmailAddress(new EmailAddress("robert.redfort@robert.com"));
        vIndividuReq.setPostalAddress(vPostalAddrReq);

        vIndividuRep.setOrganization("avocats & cie");
        vIndividuRep.setVatnumber("FR12345678910");
        vIndividuRep.setCivility(null);
        vIndividuRep.getPhoneNumbers().add(new PhoneNumber("+33123222402"));
        vIndividuRep.getFaxNumbers().add(new PhoneNumber("0123222457"));
        vIndividuRep.addEmailAddress(new EmailAddress("robert.cope@reobertcope.fr"));
        vIndividuRep.setPostalAddress(vPostalAddrRep);

        vSyreliLegalData.setRepresentant(vIndividuRep);
        vSyreliLegalData.setRequerant(vIndividuReq);

        vSyreliLegalData.setAskedAction(AskedAction.Transmission);
        vSyreliLegalData.setDateCreation(new Date());
        vSyreliLegalData.setNumDossier(pNumDossier);

        return vSyreliLegalData;
    }

    @Override
    public boolean isOkAgtf(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");

        return true;
    }

    @Override
    public boolean fillBOAStatus(LegalData pLegalData, String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pLegalData, "pLegalData");
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");

        return true;
    }

    @Override
    public boolean checkExistingLegalState(int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        return true;
    }

    @Override
    public boolean checkExistingNumDossier(String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNumDossier, "pNumDossier");
        Preconditions.checkNotEmpty(pNumDossier, "pNumDossier");
        return true;
    }

    private static String[] getGraphieAndExtensionForNdd(String pNdd) {
        if (pNdd.lastIndexOf(".") == -1)
            throw new IllegalArgumentException("pNdd must be a well formed domain name.");
        String vGraphTerme = pNdd.substring(0, pNdd.lastIndexOf("."));
        String vExtensionTerme = pNdd.substring(pNdd.lastIndexOf("."), pNdd.length());
        return new String[] { vGraphTerme, vExtensionTerme };
    }

    @Override
    public void createOrUpdateAgtfState(String pNdd, int pState, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");
        String[] vNddSplit = getGraphieAndExtensionForNdd(pNdd);
        if ((pState < 0) || (pState > 3))
            throw new IllegalArgumentException("pState must be a defined value in legal database.");
    }

    @Override
    public List<SyreliLegalData> getListSyreliForDomainName(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");
        String[] vNddSplit = getGraphieAndExtensionForNdd(pNdd);
        return new ArrayList<SyreliLegalData>();
    }

    @Override
    public boolean checkExistingSyreliForDomainName(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pNdd, "pNdd");
        Preconditions.checkNotEmpty(pNdd, "pNdd");
        return false;
    }

    @Override
    public List<Domain> getDomainAgtfFrozenList(WhoisContact pWhoisContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(pWhoisContact, "pWhoisContact");
        if ((pWhoisContact.getHandle() == null) || ("".equals(pWhoisContact.getHandle())))
            throw new IllegalArgumentException("pWhoisContact must have a valid handle.");
        return new ArrayList<Domain>();
    }

}
