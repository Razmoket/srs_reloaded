package fr.afnic.commons.services.jooq;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;
import org.jooq.util.oracle.OraclePackageDefinition;
import org.jooq.util.oracle.OracleRoutineDefinition;

public class CustomStrategy extends DefaultGeneratorStrategy {

    public CustomStrategy() {
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {

        String type = "Table";
        if (definition.getInputName().startsWith("V_")) {
            type = "View";
        }

        if (definition instanceof OracleRoutineDefinition || definition instanceof OraclePackageDefinition) {
            type = "";
        }

        String ret = super.getJavaClassName(definition, mode);

        if (!ret.endsWith("Record")) {
            ret += type;
        } else {
            ret = ret.replaceAll("Record$", type + "Record");
        }
        return ret;
    }

}
