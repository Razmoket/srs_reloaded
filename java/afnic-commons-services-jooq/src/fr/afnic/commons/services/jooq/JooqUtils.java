package fr.afnic.commons.services.jooq;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.TableField;

public class JooqUtils {

    public static <RECORD extends Record> List<String> asStringList(Result<RECORD> results, TableField<RECORD, ?> field) {
        List<String> ret = new ArrayList<String>();
        for (RECORD record : results) {
            ret.add(record.getValueAsString(field));
        }
        return ret;

    }

}
