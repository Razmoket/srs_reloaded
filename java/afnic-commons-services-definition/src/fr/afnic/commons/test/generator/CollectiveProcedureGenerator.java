package fr.afnic.commons.test.generator;

import java.util.Date;
import java.util.Random;

import fr.afnic.commons.beans.customer.collectiveprocedure.CustomerTag;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;

public final class CollectiveProcedureGenerator {

    /** Definition du Logger de la classe CollectiveProcedureGenerator */
    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(CollectiveProcedureGenerator.class);

    private CollectiveProcedureGenerator() {
    }

    public static CustomerTag createRandomCollectiveProcedure() {
        CustomerTag collectiveProcedure = new CustomerTag();

        collectiveProcedure.setDate(new Date());

        Random random = new Random(System.currentTimeMillis());

        int idxProc = random.nextInt(15) + 1;
        if (CollectiveProcedureGenerator.LOGGER.isDebugEnabled()) {
            CollectiveProcedureGenerator.LOGGER.debug("idxProc=" + idxProc);
        }

        return collectiveProcedure;
    }

}
