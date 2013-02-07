package fr.afnic.commons.services.jooq.converter.jooq.tocommons;

import fr.afnic.commons.beans.agtf.AgtfHisto;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfCommentItsCategWordTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class AgtfCommentItsCategWordTableRecordToAgtfHistoConverter extends AbstractConverter<AgtfCommentItsCategWordTableRecord, AgtfHisto> {

    public AgtfCommentItsCategWordTableRecordToAgtfHistoConverter() {
        super(AgtfCommentItsCategWordTableRecord.class, AgtfHisto.class);
    }

    @Override
    public AgtfHisto convert(AgtfCommentItsCategWordTableRecord toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {

        AgtfHisto ret = new AgtfHisto(userId, tld);
        ret.setUserId(new UserId(toConvert.getIdUser()));
        ret.setDateComment(toConvert.getDateComment());
        ret.setComment(toConvert.getRemark());
        ret.setVersion(toConvert.getRVersion().intValue());
        ret.setCategoryWordId(toConvert.getIdCategoryWord());

        return ret;
    }
}
