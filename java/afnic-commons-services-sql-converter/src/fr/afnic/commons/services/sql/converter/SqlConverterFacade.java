package fr.afnic.commons.services.sql.converter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AlgoHashMapping;
import fr.afnic.commons.services.converter.ConverterFacade;
import fr.afnic.commons.services.converter.DigestHashMapping;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.AskedActionTypeMapping;
import fr.afnic.commons.services.sql.converter.mapping.CustomerContactTypeMapping;
import fr.afnic.commons.services.sql.converter.mapping.EligibilityStatusMapping;
import fr.afnic.commons.services.sql.converter.mapping.IncoherentStatusMapping;
import fr.afnic.commons.services.sql.converter.mapping.OperationStatusMapping;
import fr.afnic.commons.services.sql.converter.mapping.OperationTypeMapping;
import fr.afnic.commons.services.sql.converter.mapping.PortfolioStatusMapping;
import fr.afnic.commons.services.sql.converter.mapping.ReachStatusMapping;
import fr.afnic.commons.services.sql.converter.mapping.SourceTypeMapping;
import fr.afnic.commons.services.sql.converter.mapping.TopLevelOperationStatusMapping;
import fr.afnic.commons.services.sql.converter.mapping.UserRightMapping;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToAutoMailReachabilityConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsCustomerContactIdConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsDocumentConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsExportViewConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsOperationConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsOperationFormConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsOperationIdConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsPostalAddressIdConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsProfileConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsQualificationConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsQualificationIdConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsQualificationSnapshotConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsResultListConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsTicketConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsUserConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToCommonsWhoisContactConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToDateConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToRawStatConverter;
import fr.afnic.commons.services.sql.converter.sql.tocommons.SqlToSyreliLegalDataConverter;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class SqlConverterFacade extends ConverterFacade {

    private static final SqlConverterFacade INSTANCE = new SqlConverterFacade();

    protected SqlConverterFacade() {
        this.registerConverterImpl(new SqlToCommonsOperationConverter());
        this.registerConverterImpl(new SqlToCommonsUserConverter());
        this.registerConverterImpl(new SqlToCommonsProfileConverter());
        this.registerConverterImpl(new SqlToCommonsTicketConverter());
        this.registerConverterImpl(new SqlToCommonsOperationFormConverter());
        this.registerConverterImpl(new SqlToCommonsOperationIdConverter());
        this.registerConverterImpl(new SqlToCommonsDocumentConverter());
        this.registerConverterImpl(new SqlToCommonsQualificationSnapshotConverter());
        this.registerConverterImpl(new SqlToCommonsQualificationConverter());
        this.registerConverterImpl(new SqlToSyreliLegalDataConverter());
        this.registerConverterImpl(new SqlToAutoMailReachabilityConverter());
        this.registerConverterImpl(new SqlToCommonsWhoisContactConverter());
        this.registerConverterImpl(new SqlToCommonsQualificationIdConverter());
        this.registerConverterImpl(new SqlToDateConverter());
        this.registerConverterImpl(new SqlToCommonsPostalAddressIdConverter());

        this.registerConverterImpl(new SqlToCommonsCustomerContactIdConverter());

        this.registerConverterImpl(new SqlToRawStatConverter());

        this.registerConverterImpl(new SqlToCommonsExportViewConverter());

        this.registerConverterImpl(new SqlToCommonsResultListConverter());

        this.registerMappingImpl(new EligibilityStatusMapping());
        this.registerMappingImpl(new OperationStatusMapping());
        this.registerMappingImpl(new OperationTypeMapping());
        this.registerMappingImpl(new AskedActionTypeMapping());
        this.registerMappingImpl(new PortfolioStatusMapping());
        this.registerMappingImpl(new ReachStatusMapping());
        this.registerMappingImpl(new SourceTypeMapping());
        this.registerMappingImpl(new TopLevelOperationStatusMapping());

        this.registerMappingImpl(new IncoherentStatusMapping());

        this.registerMappingImpl(new UserRightMapping());

        this.registerMappingImpl(new CustomerContactTypeMapping());

        //Mapping utilisés par les DNSSEC
        this.registerMappingImpl(new AlgoHashMapping());
        this.registerMappingImpl(new DigestHashMapping());

    }

    public static <IN, OUT> OUT convert(IN instanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return SqlConverterFacade.INSTANCE.convertImpl(instanceObject, outClass, userId, tld);
    }

    public static <IN, OUT> List<OUT> convertList(List<IN> listInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return SqlConverterFacade.INSTANCE.convertListImpl(listInstanceObject, outClass, userId, tld);
    }

    public static <IN, OUT> Set<OUT> convertSet(Set<IN> listInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return SqlConverterFacade.INSTANCE.convertSetImpl(listInstanceObject, outClass, userId, tld);
    }

    public static <IN, OUT> List<OUT> convertIterator(Iterator<IN> iteratorInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return SqlConverterFacade.INSTANCE.convertIteratorImpl(iteratorInstanceObject, outClass, userId, tld);
    }

}
