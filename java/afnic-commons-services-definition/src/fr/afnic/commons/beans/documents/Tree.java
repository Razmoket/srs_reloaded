package fr.afnic.commons.beans.documents;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class Tree implements Serializable {

    private final Map<TreeEnum, Folder> mapFolder = new HashMap<TreeEnum, Folder>();

    public static Tree get(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDocumentService().getTree(userId, tld);
    }

    public Folder getQualificationInbox() {
        return this.getFolder(this.mapFolder.get(TreeEnum.qualificationInbox), "qualificationInbox");
    }

    public Folder getMainLegal() {
        return this.getFolder(this.mapFolder.get(TreeEnum.mainLegal), "mainLegal");
    }

    public void setMainLegal(Folder mainLegal) {
        this.mapFolder.put(TreeEnum.mainLegal, mainLegal);
    }

    public Folder getMainOperation() {
        return this.getFolder(this.mapFolder.get(TreeEnum.mainOperation), "mainOperation");
    }

    public void setMainOperation(Folder mainOperation) {
        this.mapFolder.put(TreeEnum.mainOperation, mainOperation);
    }

    public void setQualificationInbox(Folder qualificationInbox) {
        this.mapFolder.put(TreeEnum.qualificationInbox, qualificationInbox);
    }

    public Folder getInvalidPlaint() {
        return this.getFolder(this.mapFolder.get(TreeEnum.invalidPlaint), "invalidPlaint");
    }

    public void setInvalidPlaint(Folder invalidPlaint) {
        this.mapFolder.put(TreeEnum.invalidPlaint, invalidPlaint);
    }

    public Folder getInbox() {
        return this.getFolder(this.mapFolder.get(TreeEnum.inbox), "inbox");
    }

    public void setInbox(Folder inbox) {
        this.mapFolder.put(TreeEnum.inbox, inbox);
    }

    public Folder getArchive() {
        return this.getFolder(this.mapFolder.get(TreeEnum.archive), "archive");
    }

    public void setArchive(Folder archive) {
        this.mapFolder.put(TreeEnum.archive, archive);
    }

    public Folder getOperation() {
        return this.getFolder(this.mapFolder.get(TreeEnum.operation), "operation");
    }

    public void setOperation(Folder operation) {
        this.mapFolder.put(TreeEnum.operation, operation);
    }

    public Folder getRequest() {
        return this.getFolder(this.mapFolder.get(TreeEnum.request), "request");
    }

    public void setRequest(Folder request) {
        this.mapFolder.put(TreeEnum.request, request);
    }

    public Folder getUnclassifiable() {
        return this.getFolder(this.mapFolder.get(TreeEnum.unclassifiable), "unclassifiable");
    }

    public void setUnclassifiable(Folder unclassifiable) {
        this.mapFolder.put(TreeEnum.unclassifiable, unclassifiable);
    }

    public Folder getUnknown() {
        return this.getFolder(this.mapFolder.get(TreeEnum.unknown), "unknown");
    }

    public void setUnknown(Folder unknown) {
        this.mapFolder.put(TreeEnum.unknown, unknown);
    }

    public Folder getTradeWithoutTicket() {
        return this.getFolder(this.mapFolder.get(TreeEnum.tradeWithoutTicket), "tradeWithoutTicket");
    }

    public void setTradeWithoutTicket(Folder tradeWithoutTicket) {
        this.mapFolder.put(TreeEnum.tradeWithoutTicket, tradeWithoutTicket);
    }

    public Folder getUnitTest() {
        return this.getFolder(this.mapFolder.get(TreeEnum.unitTest), "unitTest");
    }

    public void setUnitTest(Folder unitTest) {
        this.mapFolder.put(TreeEnum.unitTest, unitTest);
    }

    public Folder getReporting() {
        return this.getFolder(this.mapFolder.get(TreeEnum.reporting), "reporting");
    }

    public void setReporting(Folder reporting) {
        this.mapFolder.put(TreeEnum.reporting, reporting);
    }

    public Folder getPlaint() {
        return this.getFolder(this.mapFolder.get(TreeEnum.plaint), "plaint");
    }

    public void setPlaint(Folder plaint) {
        this.mapFolder.put(TreeEnum.plaint, plaint);
    }

    public Folder getTrash() {
        return this.getFolder(this.mapFolder.get(TreeEnum.trash), "trash");
    }

    public void setTrash(Folder trash) {
        this.mapFolder.put(TreeEnum.trash, trash);
    }

    public void setValue(TreeEnum key, Folder folder) {
        this.mapFolder.put(key, folder);
    }

    private Folder getFolder(Folder folder, String folderName) {
        if (folder == null) {
            throw new IllegalArgumentException("Folder " + folderName + " is not setted.");
        } else {
            return folder;
        }

    }
}
