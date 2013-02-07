package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.corporateentity.IntracommunityVat;
import fr.afnic.commons.beans.corporateentity.Siren;
import fr.afnic.commons.beans.corporateentity.Siret;
import fr.afnic.commons.beans.corporateentity.TradeMark;
import fr.afnic.commons.beans.corporateentity.Waldec;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.CorporateEntityTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CorporateEntityTableRecordToCorporateEntityIdentityConverter extends AbstractConverter<CorporateEntityTableRecord, CorporateEntityIdentity> {

    public CorporateEntityTableRecordToCorporateEntityIdentityConverter() {
        super(CorporateEntityTableRecord.class, CorporateEntityIdentity.class);

    }

    @Override
    public CorporateEntityIdentity convert(CorporateEntityTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        CorporateEntityIdentity ret = new CorporateEntityIdentity();

        ret.setOrganizationName(toConvert.getOrganizationName());
        ret.setSiren(new Siren(toConvert.getSirenId()));
        ret.setSiret(new Siret(toConvert.getSiretId()));
        ret.setIntracommunityVat(new IntracommunityVat(toConvert.getIntracommunityVatId()));
        ret.setTradeMark(new TradeMark(toConvert.getTrademarkId()));
        ret.setWaldec(new Waldec(toConvert.getWaldecId()));
        ret.setId(toConvert.getIdCorporateentity());
        return ret;
    }
}
