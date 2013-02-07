package fr.afnic.commons.services.jooq;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.DomainContactType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.whoiscontact.WhoisContactSearchCriteria;
import fr.afnic.commons.services.IWhoisContactService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.boa.packages.PkgUtils;
import fr.afnic.commons.services.jooq.stub.boa.tables.VDomainView;
import fr.afnic.commons.services.jooq.stub.boa.tables.VNichandlesToSurveyView;
import fr.afnic.commons.services.jooq.stub.boa.tables.VWhoisContactView;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VNichandlesToSurveyViewRecord;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VWhoisContactViewRecord;
import fr.afnic.commons.services.jooq.stub.whois.tables.ObjectContactRTable;
import fr.afnic.commons.services.proxy.ProxyWhoisContactService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public class JooqWhoisContactService extends ProxyWhoisContactService {

    private ISqlConnectionFactory sqlConnectionFactory = null;

    public JooqWhoisContactService(final ISqlConnectionFactory sqlConnectionFactory) {

        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqWhoisContactService(final ISqlConnectionFactory sqlConnectionFactory, IWhoisContactService delegationService) {
        super(delegationService);
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    @Override
    public List<String> getDomainContactHandles(String domainName, DomainContactType contactType, UserId userId, TldServiceFacade tld) throws ServiceException {

        Factory factory = this.createFactory();
        try {
            Result<VWhoisContactViewRecord> result = factory.selectFrom(VWhoisContactView.V_WHOIS_CONTACT)
                                                            .whereExists(factory.selectOne()
                                                                                .from(ObjectContactRTable.OBJECT_CONTACT_R)
                                                                                .join(VDomainView.V_DOMAIN)
                                                                                .on(VDomainView.V_DOMAIN.ID_DOMAIN_WHOIS.equal(ObjectContactRTable.OBJECT_CONTACT_R.OBJECT_ID))
                                                                                .where(VWhoisContactView.V_WHOIS_CONTACT.ID_CONTACT.equal(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_ID))
                                                                                .and(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_TYPE.equal(contactType.toString()))
                                                                                .and(VDomainView.V_DOMAIN.DOMAIN_NAME.equal(domainName))
                                                            )
                                                            .fetch();

            List<String> ret = new ArrayList<String>();
            for (VWhoisContactViewRecord record : result) {
                // ret.add(record.getHandle());
            }
            return ret;
        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<String> getNicHandlesToSurvey(UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            Result<VNichandlesToSurveyViewRecord> result = factory.selectFrom(VNichandlesToSurveyView.V_NICHANDLES_TO_SURVEY)
                                                                  .fetch();

            return JooqUtils.asStringList(result, VNichandlesToSurveyView.V_NICHANDLES_TO_SURVEY.HANDLE);
        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public boolean isExistingNicHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            int nbFoundNicHandle = PkgUtils.isexistingnichandle(factory, nicHandle).intValue();
            return nbFoundNicHandle > 0;
        } catch (Exception e) {
            throw new ServiceException("isExistingNicHandle(" + nicHandle + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<WhoisContact> getHoldersWithRegistrarCode(final String registrarCode, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(registrarCode, "registrarCode");

        Factory factory = this.createFactory();
        try {
            Result<VWhoisContactViewRecord> result = factory.selectFrom(VWhoisContactView.V_WHOIS_CONTACT)
                                                            .where(VWhoisContactView.V_WHOIS_CONTACT.REGISTRAR_CODE.equal(registrarCode))
                                                            .andExists(factory.selectOne()
                                                                              .from(ObjectContactRTable.OBJECT_CONTACT_R)
                                                                              .where(VWhoisContactView.V_WHOIS_CONTACT.ID_CONTACT.equal(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_ID))
                                                                              .and(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_TYPE.equal("HOLDER")))
                                                            .fetch();
            return JooqConverterFacade.convertIterator(result.iterator(), WhoisContact.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public List<WhoisContact> searchContact(WhoisContactSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(criteria, "criteria");

        Factory factory = this.createFactory();
        try {
            SelectJoinStep step = factory.select()
                                         .from(VWhoisContactView.V_WHOIS_CONTACT);

            List<Condition> conditions = new ArrayList<Condition>();

            if (criteria.getDomainName() != null && !StringUtils.isBlank(criteria.getDomainName())) {

                SelectConditionStep existsStep = factory.selectOne()
                                                        .from(ObjectContactRTable.OBJECT_CONTACT_R)
                                                        .join(VDomainView.V_DOMAIN)
                                                        .on(VDomainView.V_DOMAIN.ID_DOMAIN_WHOIS.equal(ObjectContactRTable.OBJECT_CONTACT_R.OBJECT_ID))
                                                        .where(VWhoisContactView.V_WHOIS_CONTACT.ID_CONTACT.equal(ObjectContactRTable.OBJECT_CONTACT_R.CONTACT_ID));

                if (!criteria.getDomainNameLike()) {
                    existsStep.and(VDomainView.V_DOMAIN.DOMAIN_NAME.like("%" + criteria.getDomainName() + "%"));
                } else {
                    existsStep.and(VDomainView.V_DOMAIN.DOMAIN_NAME.equal(criteria.getDomainName()));
                }
                conditions.add(Factory.exists(existsStep));

            }

            if (criteria.getWhoisContactName() != null && !StringUtils.isBlank(criteria.getWhoisContactName())) {
                //vaut vrai si recherche exacte
                if (!criteria.getWhoisContactNameLike()) {
                    conditions.add(VWhoisContactView.V_WHOIS_CONTACT.LASTNAME.like("%" + criteria.getWhoisContactName() + "%"));
                } else {
                    conditions.add(VWhoisContactView.V_WHOIS_CONTACT.LASTNAME.equal(criteria.getWhoisContactName()));
                }
            }

            if (criteria.getWhoisContactNicHandle() != null && !StringUtils.isBlank(criteria.getWhoisContactNicHandle())) {
                conditions.add(VWhoisContactView.V_WHOIS_CONTACT.HANDLE.equal(criteria.getWhoisContactNicHandle()));
            }

            if (criteria.getWhoisContactIdentifier() != null && !StringUtils.isBlank(criteria.getWhoisContactIdentifier())) {
                //TODO multi registre: voir si ce critère est toujours utilisé
                //si c'est le cas, il faudra le ramener dans la vue
                //builder.appendWhere(" AND whois.identification2.contact_id=whois.contact.id AND whois.identification2.data = ?");
                //builder.appendFrom(" , whois.identification2");
                //builder.addParameters(criteria.getWhoisContactIdentifier());
            }

            Result<VWhoisContactViewRecord> result = step.where(conditions)
                                                         .fetchInto(VWhoisContactView.V_WHOIS_CONTACT);

            return JooqConverterFacade.convertIterator(result.iterator(), WhoisContact.class, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }

    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

}
