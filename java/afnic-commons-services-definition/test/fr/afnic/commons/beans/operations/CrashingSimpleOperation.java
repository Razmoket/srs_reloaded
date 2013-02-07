package fr.afnic.commons.beans.operations;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class CrashingSimpleOperation extends SimpleOperation {

    public CrashingSimpleOperation(boolean isBlocking) {
        super(new OperationConfiguration().setType(OperationType.Test)
                                          .setBlocking(isBlocking), new UserId(22), TldServiceFacade.Fr);

        try {
            this.setCreateUserId(UserGenerator.getRootUserId());
        } catch (Exception e) {
            throw new RuntimeException("new TestOperation failed", e);
        }
    }

    @Override
    public OperationStatus executeSimpleImpl() throws Exception {
        throw new Exception("crashed");
    }

}
