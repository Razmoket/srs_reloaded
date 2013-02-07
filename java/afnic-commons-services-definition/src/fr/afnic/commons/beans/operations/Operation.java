package fr.afnic.commons.beans.operations;

import java.io.Serializable;
import java.util.List;

import fr.afnic.commons.beans.BusinessObject;
import fr.afnic.commons.beans.boarequest.TopLevelOperation;
import fr.afnic.commons.beans.cache.BusinessObjectCache;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

/**
 *
 * @param <STATUS> Enumeration listant les différents status de l'opération
 * @param <OBJECT> Objet sur lequel s'applique l'opération.
 */
public abstract class Operation extends BusinessObject<OperationId> implements Serializable, Cloneable {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(Operation.class);
    protected OperationStatus status = OperationStatus.Pending;
    private boolean isBlocking = true;

    private String details;

    private final BusinessObjectCache<OperationId, Operation> parentCache = new BusinessObjectCache<OperationId, Operation>();

    private OperationId previousOperationId;

    private OperationType type;

    public Operation(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public Operation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        if (conf.getType() == null) {
            throw new IllegalArgumentException("conf.type is null in " + this.getClass().getSimpleName());
        }

        this.type = conf.getType();
        this.parentCache.setId(conf.getParentId());
        this.isBlocking = conf.isBlocking();
        this.createUserId = conf.getCreateUserId();
        this.previousOperationId = conf.getPreviousOperationId();
        this.comments = conf.getComment();
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isNotBlocking() {
        return !this.isBlocking();
    }

    public boolean isBlocking() {
        return this.isBlocking;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public OperationStatus getStatus() {
        return this.status;
    }

    public boolean hasParent() {
        return this.parentCache.isSet();
    }

    public void setParent(Operation parent) {
        Preconditions.checkNotNull(parent, "parent");
        this.parentCache.setValue(parent);
    }

    public Operation getParent() throws ServiceException {
        return this.parentCache.getValue(this.userIdCaller, this.tldCaller);
    }

    public OperationId getParentId() {
        return this.parentCache.getId();
    }

    public void setParentId(OperationId parentId) {
        this.parentCache.setId(parentId);
    }

    public abstract List<Document> getDocuments() throws ServiceException;

    public boolean isAllowed(UserId userId) {
        return true;
    }

    public OperationType getType() {
        return this.type;
    }

    /**
     * Indique si l'état actuelle de l'opération permet l'opération de type passé en parametre
     * 
     * @param operationType
     * @return
     */
    public boolean isStateAllowingOperation(OperationType operationType) {
        return true;
    }

    public TopLevelOperation getTopLevelOperation() throws ServiceException {
        if (this.hasParent()) {
            if (this.getParent() instanceof TopLevelOperation) {
                return (TopLevelOperation) this.getParent();
            }
            return this.getParent().getTopLevelOperation();
        } else {
            return null;
        }
    }

    /**
     * Retourne un parent de la classe spécifié en paramettre ou retourne une exception si il n'y en a pas
     * @throws ServiceException 
     */
    protected <OBJECT extends Object> OBJECT getParentOrThrowException(Class<OBJECT> parentClass) throws ServiceException {
        OBJECT parent = this.getParent(parentClass);
        if (parent == null) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " must be used under a " + parentClass.getSimpleName());
        } else {
            return parent;
        }
    }

    /**
     * Cherche dans les parents de l'opération une opération de class parentClass.
     * C'est le parent le plus proche correspondant au critère qui est retournée.
     * Si aucun parent correspondant n'est trouvé, retourne null.
     * @throws ServiceException 
     */
    @SuppressWarnings("unchecked")
    public <OPERATION extends Object> OPERATION getParent(Class<OPERATION> parentClass) throws ServiceException {
        Preconditions.checkNotNull(parentClass, "parentClass");
        if (this.hasParent()) {
            if (parentClass.isInstance(this.getParent())) {
                return (OPERATION) this.getParent();
            } else {
                return this.getParent().getParent(parentClass);
            }
        } else {
            return null;
        }
    }

    /**
     * A appeler en cas de changement sur le topLevelOperation.
     * Met à jour la base et change les informations dans le modèle objet pour que les sous-opérations 
     * de ce topLevelOperation aient les référence vers la version updaté de cette objet.
     * 
     * @param topLevelOperaton
     * @throws ServiceException 
     */
    public void updateTopLevelOperation() throws ServiceException {
        if (this.hasParent()) {
            this.getParent().updateTopLevelOperation();
        } else {
            return;
        }
    }

    public boolean hasNoMacthingCreateUserId() throws ServiceException {
        return !this.hasMatchingCreateUserId();
    }

    public boolean hasMatchingCreateUserId() throws ServiceException {
        if (this.hasNoCreateUserId()) {
            return false;
        }
        return this.createUserId.isExisting(this.userIdCaller, this.tldCaller);
    }

    public boolean hasNoCreateUserId() {
        return !this.hasCreateUserId();

    }

    public boolean hasCreateUserId() {
        return this.createUserId != null;

    }

    @Override
    public void setUpdateUserId(UserId updateUserId) {
        this.updateUserId = updateUserId;
    }

    protected OperationStatus computeAndUpdateStatus() {

        try {
            this.computeStatus();

            AppServiceFacade.getOperationService().updateStatus(this, this.userIdCaller, this.tldCaller);

            if (this.hasParent()) {
                this.getParent().computeAndUpdateStatus();
            }

            return this.status;
        } catch (ServiceException e) {
            //A priori la base de donnée est out.
            //On retourne failed pour arreter tout traitement.
            LOGGER.error("computeAndUpdateStatus failed for operationId " + this.getId(), e);
            return OperationStatus.Failed;
        }
    }

    public Operation updateStatus() throws ServiceException {
        AppServiceFacade.getOperationService().updateStatus(this, this.userIdCaller, this.tldCaller);
        return this.id.getObjectOwner(this.userIdCaller, this.tldCaller);
    }

    public final OperationStatus execute() throws ServiceException {
        if (this.getId() == null) {
            throw new IllegalArgumentException("Cannot execute unregistrered operation " + this.getClass().getSimpleName() + "[id is null]");
        }

        if (this.userIdCaller.isNotExisting(this.userIdCaller, this.tldCaller)) {
            throw new IllegalArgumentException("user with id " + this.userIdCaller + " does not exist");
        }

        if (!this.isAllowed(this.userIdCaller)) {
            throw new IllegalArgumentException("operation is not allowed");
        }

        return this.executeImpl();

    }

    public final OperationStatus redoExecute() throws ServiceException {
        if (this.getId() == null) {
            throw new IllegalArgumentException("Cannot execute unregistrered operation " + this.getClass().getSimpleName() + "[id is null]");
        }

        if (this.userIdCaller.isNotExisting(this.userIdCaller, this.tldCaller)) {
            throw new IllegalArgumentException("user with id " + this.userIdCaller + " does not exist");
        }

        if (!this.isAllowed(this.userIdCaller)) {
            throw new IllegalArgumentException("operation is not allowed");
        }

        return this.redoExecuteImpl();

    }

    public OperationId getPreviousOperationId() {
        return this.previousOperationId;
    }

    public boolean hasPrevious() {
        return this.previousOperationId != null;
    }

    public void setPreviousOperationId(OperationId previousOperationId) {
        this.previousOperationId = previousOperationId;
    }

    public void setBlocking(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }

    /**
     *  Indique si l'operation peut etre ajouter dans l'operation passée en parametre
     */
    protected final boolean canBeSubOperationForOperation(Operation operation) throws ServiceException {
        boolean found = true;
        for (Class<?> parentClass : this.getRequiredParentClass()) {
            found = found && (operation.getParent(parentClass) != null);
        }
        return found;
    }

    /**
     * Permet d'indiquer si une opération a  
     */
    protected Class<?>[] getRequiredParentClass() {
        return new Class<?>[] {};
    }

    /**
     * Implémentation de l'execution de l'opération déléguée aux implémentations de Operation.     
     * @throws ServiceException 
     */
    protected abstract OperationStatus executeImpl() throws ServiceException;

    protected abstract OperationStatus redoExecuteImpl() throws ServiceException;

    protected abstract void computeStatus() throws ServiceException;

    protected void setType(OperationType type) {
        this.type = type;
    }

    public Operation copy() throws ServiceException {
        try {
            return (Operation) super.clone();
        } catch (Exception e) {
            throw new ServiceException("copy() failed", e);
        }
    }

    /**
     * Fonction permettant de remplir les données liées à une opération particulière.
     * Par exemple un type d'opération comme les Status update contiennent des informaitons dans la table opération mais aussi dans la table
     * status update.
     * Dans le corps de cette fonction on se charge de remplir les champs de l'opérations stockés dans des tables spécifiques.
     * 
     */
    public void populate() throws ServiceException {

    }

    public String getDisplayName() {
        return this.getClass().getSimpleName() + " " + this.getIdAsString();
    }

}
