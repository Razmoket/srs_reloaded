package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.AutoMailReachability;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationId;
import fr.afnic.commons.beans.operations.qualification.QualificationSnapshot;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IQualificationService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.Delay;

public class MockQualificationService implements IQualificationService {

    private static int lastSnapshotId = 1;

    private final Map<OperationId, Qualification> qualificationMap = new HashMap<OperationId, Qualification>();
    private final Map<Integer, QualificationSnapshot> snapshotMap = new HashMap<Integer, QualificationSnapshot>();

    @Override
    public Qualification getQualification(OperationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.qualificationMap.containsKey(qualificationId)) {
            return this.qualificationMap.get(qualificationId).copy();
        } else {
            throw new NotFoundException("not found");
        }
    }

    @Override
    public OperationId create(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkNotNull(qualification.getSource(), "qualification.source");
        Preconditions.checkIsExistingNicHandle(qualification.getHolderNicHandle(), "qualification.holderNicHandle", userId, tld);

        if (qualification.getSource() == QualificationSource.Reporting
            || qualification.getSource() == QualificationSource.Plaint) {
            Preconditions.checkParameter(qualification.getInitiatorEmailAddress(), "qualification.initiatorEmail");
        }

        qualification = AppServiceFacade.getOperationService().createAndGet(qualification, userId, tld);
        //On s'assure d'avoir le même objet dans la map de MockOperationService que dans la map de MockQualificationService
        if (AppServiceFacade.getOperationService() instanceof MockOperationService) {

            MockOperationService mos = (MockOperationService) AppServiceFacade.getOperationService();
            qualification = (Qualification) mos.getStoredOperation(qualification.getId());
        } else {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " can only be used with " + MockOperationService.class.getSimpleName());
        }

        qualification.setPortfolioStatus(PortfolioStatus.Active);
        qualification.setEligibilityStatus(EligibilityStatus.NotIdentified);
        qualification.setReachStatus(ReachStatus.NotIdentified);
        qualification.setTopLevelStatus(TopLevelOperationStatus.Pending);

        this.qualificationMap.put(qualification.getId(), qualification);
        qualification.setHolderSnapshotId("TEST");

        return qualification.getId();
    }

    @Override
    public Qualification createAndGet(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getQualification(this.create(qualification, userId, tld), userId, tld);
    }

    @Override
    public List<Qualification> getQualifications(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return Collections.emptyList();
    }

    @Override
    public void updateTopLevelStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.checkUpdateParameters(qualification, updateUserId, tld);

        Qualification savedQualification = this.qualificationMap.get(qualification.getId());
        savedQualification.setTopLevelStatus(qualification.getTopLevelStatus());

        this.populateUpdateField(savedQualification, updateUserId);
    }

    @Override
    public void updateReachStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.checkUpdateParameters(qualification, updateUserId, tld);

        Qualification savedQualification = this.qualificationMap.get(qualification.getId());
        savedQualification.setReachStatus(qualification.getReachStatus());

        this.populateUpdateField(savedQualification, updateUserId);
    }

    @Override
    public void updateEligibilityStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.checkUpdateParameters(qualification, updateUserId, tld);

        Qualification savedQualification = this.qualificationMap.get(qualification.getId());
        savedQualification.setEligibilityStatus(qualification.getEligibilityStatus());

        this.populateUpdateField(savedQualification, updateUserId);
    }

    @Override
    public void updatePortfolioStatus(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.checkUpdateParameters(qualification, updateUserId, tld);

        Qualification savedQualification = this.qualificationMap.get(qualification.getId());
        savedQualification.setPortfolioStatus(qualification.getPortfolioStatus());
        this.populateUpdateField(savedQualification, updateUserId);
    }

    @Override
    public void setIncoherent(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        this.checkUpdateParameters(qualification, updateUserId, tld);

        Qualification savedQualification = this.qualificationMap.get(qualification.getId());
        savedQualification.setIncoherent(true);

        this.populateUpdateField(savedQualification, updateUserId);
    }

    private void checkUpdateParameters(Qualification qualification, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkIsExistingIdentifier(qualification.getId(), "qualification.operationId", updateUserId, tld);
    }

    private void populateUpdateField(Qualification qualification, UserId updateUserId) {
        qualification.setUpdateUserId(updateUserId);
        qualification.setObjectVersion(qualification.getObjectVersion() + 1);
        qualification.setUpdateDate(new Date());
    }

    @Override
    public int getQualificationInProgressCount(UserId userId, TldServiceFacade tld) throws ServiceException {
        int count = 0;

        for (Qualification qualification : this.qualificationMap.values()) {
            if (qualification.getTopLevelStatus() != TopLevelOperationStatus.Finished) {
                count++;
            }

        }
        return count;
    }

    @Override
    public int createSnapshot(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {

        Preconditions.checkNotNull(qualification, "qualification");
        Preconditions.checkIsExistingIdentifier(qualification.getId(), "qualification.id", userId, tld);

        QualificationSnapshot snap = new QualificationSnapshot();
        int id = lastSnapshotId++;

        snap.put("ID_QUALIFICATION_SNAPSHOT", id);

        if (qualification.getReachStatus() != null) {
            snap.put("ID_REACH_STATUS", qualification.getReachStatus());
        }

        if (qualification.getPortfolioStatus() != null) {
            snap.put("ID_PORTFOLIO_STATUS", qualification.getPortfolioStatus());
        }

        if (qualification.getEligibilityStatus() != null) {
            snap.put("ID_ELIGIBILITY_STATUS", qualification.getEligibilityStatus());
        }

        if (qualification.getTopLevelStatus() != null) {
            snap.put("ID_QUALIFICATION_STATUS", qualification.getTopLevelStatus());
        }

        WhoisContact holder = qualification.getHolder();

        String name = holder.getName();
        String orgType = "";
        String siren = "";
        String siret = "";
        String trademark = "";
        String duns = "";
        String waldec = "";

        if (holder instanceof CorporateEntityWhoisContact) {
            CorporateEntityWhoisContact corp = (CorporateEntityWhoisContact) holder;
            if (corp.hasLegalStructure()) {
                CorporateEntity legalStructure = corp.getLegalStructure();

                orgType = legalStructure.getOrganizationTypeAsString();
                waldec = legalStructure.getWaldecAsString();
                siren = legalStructure.getSirenAsString();
                siret = legalStructure.getSiretAsString();
                trademark = legalStructure.getTradeMarkAsString();
            }
        }

        snap.put("contactName", name);
        snap.put("ORG_TYPE", orgType);
        snap.put("SIREN", siren);
        snap.put("SIRET", siret);
        snap.put("TRADEMARK", trademark);
        snap.put("WALDEC", waldec);
        snap.put("DUNS", duns);

        snap.put("NAME", holder.getName());

        this.snapshotMap.put(id, snap);

        return id;

    }

    @Override
    public QualificationSnapshot getQualificationSnapshot(int snapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.snapshotMap.get(snapshotId);
    }

    @Override
    public List<Qualification> getQualifications(String nicHanlde, UserId userId, TldServiceFacade tld) throws ServiceException {

        return null;
    }

    @Override
    public List<Qualification> getQualificationsToUpdate(OperationView view, Delay delay, UserId userId, TldServiceFacade tld) throws ServiceException {

        PortfolioStatus portfolioStatus = this.getRequiredPortfolioStatus(view);
        List<Qualification> qualifications = new ArrayList<Qualification>();

        for (Qualification qualification : this.qualificationMap.values()) {
            if (qualification.getPortfolioStatus() == portfolioStatus) {
                Date date = new Date(System.currentTimeMillis() - delay.toMillis());
                if (qualification.getUpdateDate().before(date)) {
                    qualifications.add(qualification.copy());
                }
            }
        }

        return qualifications;

    }

    private PortfolioStatus getRequiredPortfolioStatus(OperationView view) {
        return OperationViewToPortfolioStatusConverter.convert(view);
    }

    @Override
    public void populateQualification(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public Qualification getQualificationInProgress(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingNicHandle(nicHandle, "nicHandle", userId, tld);

        for (Qualification qualification : this.qualificationMap.values()) {
            if (qualification.getTopLevelStatus() != TopLevelOperationStatus.Finished
                && nicHandle.equals(qualification.getHolderNicHandle())) {
                return qualification;
            }
        }

        throw new NotFoundException("No qualification in progress found with nichandle[" + nicHandle + "].");
    }

    @Override
    public int getQualificationsCount(OperationView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getQualifications(view, userId, tld).size();
    }

    @Override
    public Qualification getQualification(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isExistingQualificationId(QualificationId qualificationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AutoMailReachability checkKey(String cle, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<QualificationId> getListQualificationWaitingCheckAutoReachability(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<QualificationId> getListQualificationWaitingCheckAutoReminderReachability(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createQualificationCheckAutoReachability(Qualification qualification, boolean reminder, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<QualificationId> getListQualificationAutoReminderTimeout(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateAutoMail(QualificationId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getQualificationCheckAutoReachability(Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateComment(QualificationId qualificationId, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateEnQualif(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNbQualifLaunchedToday(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

}
