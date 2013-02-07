package fr.afnic.commons.beans.operations.qualification;

public enum QualificationStep {
    Start("qualificationProcessStart"),
    Finished("qualificationProcessFinished"),
    Problem("qualificationProcessProblem");

    private String eppType;

    private QualificationStep(String eppType) {
        this.eppType = eppType;
    }

    public String getEppType() {
        return this.eppType;
    }
}
