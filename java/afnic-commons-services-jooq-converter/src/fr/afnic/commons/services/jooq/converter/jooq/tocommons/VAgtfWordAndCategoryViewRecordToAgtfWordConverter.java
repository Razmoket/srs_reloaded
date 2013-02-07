package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import java.util.ArrayList;

import fr.afnic.commons.beans.agtf.AgtfCategoryWord;
import fr.afnic.commons.beans.agtf.AgtfWord;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VAgtfWordAndCategoryViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class VAgtfWordAndCategoryViewRecordToAgtfWordConverter extends AbstractConverter<VAgtfWordAndCategoryViewRecord, AgtfWord> {

    public VAgtfWordAndCategoryViewRecordToAgtfWordConverter() {
        super(VAgtfWordAndCategoryViewRecord.class, AgtfWord.class);
    }

    @Override
    public AgtfWord convert(VAgtfWordAndCategoryViewRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        AgtfWord ret = new AgtfWord(userId, tld);
        ret.setId(toConvert.getIdWord().intValue());
        ret.setWord(toConvert.getWord());
        ret.setWordTld(toConvert.getWordTld());
        ret.setStatus(toConvert.getStatus());
        AgtfCategoryWord acw = new AgtfCategoryWord(userId, tld);
        acw.setCreateDate(toConvert.getCreateDate());
        acw.setDeleteDate(toConvert.getDeleteDate());
        acw.setCategoryWordId(toConvert.getIdCategoryWord());
        acw.setCategoryId(toConvert.getIdCategory());
        ArrayList<AgtfCategoryWord> list = new ArrayList<AgtfCategoryWord>();
        list.add(acw);
        ret.setListCategoryWord(list);

        return ret;
    }
}
