package fr.afnic.commons.beans.boarequest;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationStatus;
import fr.afnic.commons.services.exception.ServiceException;

public enum TopLevelOperationStatus implements IDescribedInternalObject {

    Initializing("En cours d'initialisation", PublicQualificationStatus.Start), //Phase d'initialisation par un robot
    Pending("À traiter", PublicQualificationStatus.Start), //A traiter -> rien n'a été fait manuellement
    Running("En cours", PublicQualificationStatus.Start), //En cours -> des propriétés ont été modifiés
    PendingResponse("En attente de réponse", PublicQualificationStatus.Problem), //Attente réponse -> un message a été envoyé
    ReceivedResponse("Répondu", PublicQualificationStatus.Problem), //Répondu -> une réponse a été reçue
    Finished("Fini", PublicQualificationStatus.Finished); //Le dossier est clot

    private PublicQualificationStatus publicQualificationStatus;
    private String description;

    private TopLevelOperationStatus(String description, PublicQualificationStatus publicQualificationStatus) {
        this.publicQualificationStatus = publicQualificationStatus;
        this.description = description;
    }

    public PublicQualificationStatus getPublicQualificationStatus() {
        return this.publicQualificationStatus;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        return this.getDescription();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {

    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

}
