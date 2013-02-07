package fr.afnic.commons.beans.operations;

import fr.afnic.commons.beans.profiling.users.UserId;

/**
 * Objet constuit comme un builder permettant de ne pas passer à longueur de temps les 
 * 5 attributs de la configuration d'une operation.
 * 
 * 
 * @author ginguene
 *
 */
public class OperationConfiguration {

    protected OperationType type;
    protected OperationId parentId;
    protected UserId createUserId;
    protected boolean isBlocking;
    protected OperationId previousOperationId;
    protected String comment;

    private static final OperationConfiguration INSTANCE = new OperationConfiguration();

    protected OperationConfiguration() {
    }

    public static final OperationConfiguration create() {
        return INSTANCE;
    }

    protected OperationConfiguration(OperationConfiguration conf) {
        this.type = conf.type;
        this.parentId = conf.parentId;
        this.isBlocking = conf.isBlocking;
        this.createUserId = conf.createUserId;
        this.comment = conf.getComment();

    }

    public OperationType getType() {
        return this.type;
    }

    public OperationConfiguration setType(OperationType type) {
        OperationConfiguration conf = new OperationConfiguration(this);
        conf.type = type;
        return conf;
    }

    public String getComment() {
        return this.comment;
    }

    public OperationConfiguration setComment(String comment) {
        OperationConfiguration conf = new OperationConfiguration(this);
        conf.comment = comment;
        return conf;
    }

    public OperationId getParentId() {
        return this.parentId;
    }

    public OperationConfiguration setParentId(OperationId parentId) {
        OperationConfiguration conf = new OperationConfiguration(this);
        conf.parentId = parentId;
        return conf;
    }

    public UserId getCreateUserId() {
        return this.createUserId;
    }

    public OperationConfiguration setCreateUserId(UserId createUserId) {
        OperationConfiguration conf = new OperationConfiguration(this);
        conf.createUserId = createUserId;
        return conf;
    }

    public boolean isBlocking() {
        return this.isBlocking;
    }

    public OperationConfiguration setBlocking(boolean isBlocking) {
        OperationConfiguration conf = new OperationConfiguration(this);
        conf.isBlocking = isBlocking;
        return conf;
    }

    public OperationId getPreviousOperationId() {
        return this.previousOperationId;
    }

    public OperationConfiguration setPreviousOperationId(OperationId previousId) {
        OperationConfiguration conf = new OperationConfiguration(this);
        conf.previousOperationId = previousId;
        return conf;
    }

}
