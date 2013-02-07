package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnQualificationMapping {

    // table qualification
    idQualification("ID_QUALIFICATION"),
    idSourceType("ID_SOURCE_TYPE"),
    idOperation("ID_OPERATION"),
    idTopLevelOperationStatus("ID_TOP_LEVEL_OPERATION_STATUS"),
    idReachStatus("ID_REACH_STATUS"),
    idEligibilityStatus("ID_ELIGIBILITY_STATUS"),
    idPortfolioStatus("ID_PORTFOLIO_STATUS"),
    idContactSnapshot("ID_CONTACT_SNAPSHOT"),
    idClient("ID_CLIENT"),
    contactHolderHandle("CONTACT_HOLDER_HANDLE"),
    initiatorEmail("INITIATOR_EMAIL"),
    isIncoherent("IS_INCOHERENT"),
    objectVersion("OBJECT_VERSION"),
    domainNameFrom("DOMAIN_NAME_FROM"),
    comments("COMMENTS");

    private final String sqlColumnName;

    private SqlColumnQualificationMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
