package fr.afnic.commons.beans.documents;

import java.io.Serializable;

public enum TreeEnum implements Serializable {
    inbox("/inbox"),
    archive("/archive"),
    operation("/operation"),
    request("/request"),
    unknown("/unknown"),
    tradeWithoutTicket("/tradeWithoutTicket"),
    unitTest("/unitTest"),
    reporting("/reporting"),
    plaint("/plaint"),
    qualificationInbox("/qualificationInbox"),
    invalidPlaint("/invalidPlaint"),
    mainLegal("/mainLegal"),
    mainOperation("/mainOperation"),
    trash("/trash"),
    unclassifiable("/unclassifiable"), ;

    private String path;

    private TreeEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
