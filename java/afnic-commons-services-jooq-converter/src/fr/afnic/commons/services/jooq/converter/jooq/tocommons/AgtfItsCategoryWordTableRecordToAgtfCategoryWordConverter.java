package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.agtf.AgtfCategoryWord;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfItsCategoryWordTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AgtfItsCategoryWordTableRecordToAgtfCategoryWordConverter extends AbstractConverter<AgtfItsCategoryWordTableRecord, AgtfCategoryWord> {

    public AgtfItsCategoryWordTableRecordToAgtfCategoryWordConverter() {
        super(AgtfItsCategoryWordTableRecord.class, AgtfCategoryWord.class);
    }

    @Override
    public AgtfCategoryWord convert(AgtfItsCategoryWordTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        AgtfCategoryWord ret = new AgtfCategoryWord(userId, tld);
        ret.setCategoryWordId(toConvert.getIdCategoryWord());
        ret.setCreateDate(toConvert.getCreateDate());
        ret.setDeleteDate(toConvert.getDeleteDate());
        ret.setCategoryId(toConvert.getIdCategory());
        ret.setWordId(toConvert.getIdWord());
        ret.setListHisto(AppServiceFacade.getAgtfService().getHistoWithIdCategoryWord(toConvert.getIdCategoryWord().intValue(), userId, tld));

        return ret;
    }
}
