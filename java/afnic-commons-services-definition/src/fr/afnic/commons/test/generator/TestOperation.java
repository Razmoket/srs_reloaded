package fr.afnic.commons.test.generator;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class TestOperation extends SimpleOperation {
    private final OperationStatus status;

    public TestOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.Test), userId, tld);
        this.status = OperationStatus.Pending;
    }

    public TestOperation(UserId userId, TldServiceFacade tld) {
        this(OperationStatus.Pending, false, userId, tld);
    }

    public TestOperation(OperationStatus status, UserId userId, TldServiceFacade tld) {
        this(status, false, userId, tld);
    }

    public TestOperation(OperationStatus status, boolean isBlocking, UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.Test)
                                    .setBlocking(isBlocking), userId, tld);
        this.status = status;

        try {
            this.setCreateUserId(UserGenerator.getRootUserId());
        } catch (Exception e) {
            throw new RuntimeException("new TestOperation failed", e);
        }
    }

    @Override
    public OperationStatus executeSimpleImpl() throws Exception {
        return this.status;
    }
}
