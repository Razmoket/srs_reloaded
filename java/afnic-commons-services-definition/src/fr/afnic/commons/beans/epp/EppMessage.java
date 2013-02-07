package fr.afnic.commons.beans.epp;

public class EppMessage {
    private int id;
    private String idAdherent;
    private int numFo;
    private String opaque;
    private String enqueueTime;
    private EppMessageStatus status;
    private String message;
    private String type;
    private String domainName;
    private String qualificationId;
    private String contactHandle;
    private String contactSnapshotId;
    private String domainSnapshotId;
    private String qualificationSnapshotId;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdAdherent() {
        return this.idAdherent;
    }

    public void setIdAdherent(String idAdherent) {
        this.idAdherent = idAdherent;
    }

    public int getNumFo() {
        return this.numFo;
    }

    public void setNumFo(int numFo) {
        this.numFo = numFo;
    }

    public String getOpaque() {
        return this.opaque;
    }

    public void setOpaque(String opaque) {
        this.opaque = opaque;
    }

    public String getEnqueueTime() {
        return this.enqueueTime;
    }

    public void setEnqueueTime(String enqueueTime) {
        this.enqueueTime = enqueueTime;
    }

    public EppMessageStatus getStatus() {
        return this.status;
    }

    public void setStatus(EppMessageStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getQualificationId() {
        return this.qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getContactHandle() {
        return this.contactHandle;
    }

    public void setContactHandle(String contactHandle) {
        this.contactHandle = contactHandle;
    }

    public String getContactSnapshotId() {
        return this.contactSnapshotId;
    }

    public void setContactSnapshotId(String contactSnapshotId) {
        this.contactSnapshotId = contactSnapshotId;
    }

    public String getDomainSnapshotId() {
        return this.domainSnapshotId;
    }

    public void setDomainSnapshotId(String domainSnapshotId) {
        this.domainSnapshotId = domainSnapshotId;
    }

    public String getQualificationSnapshotId() {
        return this.qualificationSnapshotId;
    }

    public void setQualificationSnapshotId(String qualificationSnapshotId) {
        this.qualificationSnapshotId = qualificationSnapshotId;
    }

}
