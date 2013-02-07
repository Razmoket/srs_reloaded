package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.agtf.AgtfWord;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfWordTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AgtfWordTableRecordToAgtfWordConverter extends AbstractConverter<AgtfWordTableRecord, AgtfWord> {

    public AgtfWordTableRecordToAgtfWordConverter() {
        super(AgtfWordTableRecord.class, AgtfWord.class);

    }

    @Override
    public AgtfWord convert(AgtfWordTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        AgtfWord ret = new AgtfWord(userId, tld);

        ret.setId(toConvert.getIdWord().intValue());
        ret.setWord(toConvert.getWord());
        ret.setWordTld(toConvert.getWordTld());
        ret.setStatus(toConvert.getStatus());
        ret.setListCategoryWord(AppServiceFacade.getAgtfService().getActiveCategoriesWordFromWord(toConvert.getIdWord().intValue(), userId, tld));

        return ret;
    }
}
