package fr.afnic.commons.beans.operations.qualification;

import java.io.Serializable;

public class AutoMailReachability implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private QualificationId idQualification;
    private boolean isRelance;
    private boolean isValid;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public QualificationId getIdQualification() {
        return this.idQualification;
    }

    public void setIdQualification(QualificationId idQualification) {
        this.idQualification = idQualification;
    }

    public boolean isRelance() {
        return this.isRelance;
    }

    public void setRelance(boolean isRelance) {
        this.isRelance = isRelance;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }
}
