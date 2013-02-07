package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnQualificationSnapshotMapping {

    idQualificationSnapshot("ID_QUALIFICATION_SNAPSHOT"),
    idReachStatus("ID_REACH_STATUS"),
    idEligibilityStatus("ID_ELIGIBILITY_STATUS"),
    idPortfolioStatus("ID_PORTFOLIO_STATUS"),
    idQualificationStatus("ID_QUALIFICATION_STATUS"),
    orgType("ORG_TYPE"),
    siren("SIREN"),
    siret("SIRET"),
    trademark("TRADEMARK"),
    waldec("WALDEC"),
    duns("DUNS"),
    name("NAME"), ;

    private final String sqlColumnName;

    private SqlColumnQualificationSnapshotMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
