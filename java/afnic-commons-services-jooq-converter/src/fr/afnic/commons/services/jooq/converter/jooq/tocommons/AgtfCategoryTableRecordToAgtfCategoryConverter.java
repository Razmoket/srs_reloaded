package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.agtf.AgtfCategory;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfCategoryTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AgtfCategoryTableRecordToAgtfCategoryConverter extends AbstractConverter<AgtfCategoryTableRecord, AgtfCategory> {

    public AgtfCategoryTableRecordToAgtfCategoryConverter() {
        super(AgtfCategoryTableRecord.class, AgtfCategory.class);

    }

    @Override
    public AgtfCategory convert(AgtfCategoryTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        AgtfCategory ret = new AgtfCategory(userId, tld);

        ret.setId(toConvert.getIdCategory().intValue());
        ret.setTextFr(toConvert.getValueFr());
        ret.setTextEn(toConvert.getValueEn());
        ret.setEndDate(toConvert.getEndDate());
        ret.setCanonicalCheck(toConvert.getCanonicalCheck().equals(new Byte((byte) 0)) ? false : true);

        return ret;
    }
}
