package fr.afnic.commons.services.jooq.converter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.ConverterFacade;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.ContractOffreMapping;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.ContractStatusMapping;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.CustomerContactStatusMapping;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.CustomerStatusMapping;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.StrDomainStatusMapping;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.StrTicketOperationMapping;
import fr.afnic.commons.services.jooq.converter.jooq.mapping.StrTicketStatusMapping;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.AgtfCategoryTableRecordToAgtfCategoryConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.AgtfCommentItsCategWordTableRecordToAgtfHistoConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.AgtfItsCategoryWordTableRecordToAgtfCategoryWordConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.AgtfWordTableRecordToAgtfWordConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.ContactDetailsTableRecordToContactDetailsConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.ContractTableRecordToContract;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.CorporateEntityTableRecordToCorporateEntityIdentityConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.CustomerContactTableRecordToCustomerContactConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.CustomerTableRecordToCustomerConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.EppMessageTableRecordToEppMessageConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.IndividualTableRecordToIndividualIdentityConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.PostalAddressTableRecordToPostalAddressConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.RecordToContractTypeOnTld;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.TldTableRecordToTldServiceFacade;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.VAgtfWordAndCategoryViewRecordToAgtfWordConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.VBillableTicketViewRecordToBillableTicketInfoConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.VDocumentTypeViewRecordToDocumentConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.VDomainViewRecordToDomainConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.VTicketViewRecordToTicketConverter;
import fr.afnic.commons.services.jooq.converter.jooq.tocommons.VWhoisContactViewRecordToWhoisContactConverter;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class JooqConverterFacade extends ConverterFacade {

    private static final JooqConverterFacade INSTANCE = new JooqConverterFacade();

    protected JooqConverterFacade() {
        this.registerConverterImpl(new PostalAddressTableRecordToPostalAddressConverter());
        this.registerConverterImpl(new EppMessageTableRecordToEppMessageConverter());
        this.registerConverterImpl(new VDomainViewRecordToDomainConverter());
        this.registerConverterImpl(new VWhoisContactViewRecordToWhoisContactConverter());
        this.registerConverterImpl(new VTicketViewRecordToTicketConverter());
        this.registerConverterImpl(new VBillableTicketViewRecordToBillableTicketInfoConverter());

        this.registerConverterImpl(new AgtfCategoryTableRecordToAgtfCategoryConverter());
        this.registerConverterImpl(new AgtfWordTableRecordToAgtfWordConverter());
        this.registerConverterImpl(new AgtfItsCategoryWordTableRecordToAgtfCategoryWordConverter());
        this.registerConverterImpl(new AgtfCommentItsCategWordTableRecordToAgtfHistoConverter());
        this.registerConverterImpl(new VAgtfWordAndCategoryViewRecordToAgtfWordConverter());
        this.registerConverterImpl(new RecordToContractTypeOnTld());
        this.registerConverterImpl(new ContractTableRecordToContract());
        this.registerConverterImpl(new CustomerContactTableRecordToCustomerContactConverter());
        this.registerConverterImpl(new CustomerTableRecordToCustomerConverter());
        this.registerConverterImpl(new ContactDetailsTableRecordToContactDetailsConverter());
        this.registerConverterImpl(new VDocumentTypeViewRecordToDocumentConverter());

        this.registerConverterImpl(new IndividualTableRecordToIndividualIdentityConverter());
        this.registerConverterImpl(new CorporateEntityTableRecordToCorporateEntityIdentityConverter());
        this.registerConverterImpl(new TldTableRecordToTldServiceFacade());

        this.registerMappingImpl(new StrDomainStatusMapping());
        this.registerMappingImpl(new StrTicketStatusMapping());
        this.registerMappingImpl(new StrTicketOperationMapping());
        this.registerMappingImpl(new CustomerContactStatusMapping());
        this.registerMappingImpl(new CustomerStatusMapping());
        this.registerMappingImpl(new ContractStatusMapping());
        this.registerMappingImpl(new ContractOffreMapping());

    }

    public static <IN, OUT> OUT convert(IN instanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return JooqConverterFacade.INSTANCE.convertImpl(instanceObject, outClass, userId, tld);
    }

    public static <IN, OUT> List<OUT> convertList(List<IN> listInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return JooqConverterFacade.INSTANCE.convertListImpl(listInstanceObject, outClass, userId, tld);
    }

    public static <IN, OUT> Set<OUT> convertSet(Set<IN> listInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return JooqConverterFacade.INSTANCE.convertSetImpl(listInstanceObject, outClass, userId, tld);
    }

    public static <IN, OUT> List<OUT> convertIterator(Iterator<IN> iteratorInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return JooqConverterFacade.INSTANCE.convertIteratorImpl(iteratorInstanceObject, outClass, userId, tld);
    }

}
