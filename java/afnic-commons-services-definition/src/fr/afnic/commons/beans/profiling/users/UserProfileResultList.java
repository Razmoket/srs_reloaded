package fr.afnic.commons.beans.profiling.users;

import java.util.List;

import fr.afnic.commons.beans.list.Column;
import fr.afnic.commons.beans.list.Line;
import fr.afnic.commons.beans.list.SimpleResultList;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UserProfileResultList extends SimpleResultList {

    private final static Column PROFILE_ID = new Column("Id", "profile_id", true);
    private final static Column PROFILE_NAME = new Column("Nom", "profile_name");
    private final static Column PROFILE_DESC = new Column("Description", "profile_desc");

    public UserProfileResultList(List<UserProfile> profiles, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        try {

            this.addColumn(PROFILE_ID);
            this.addColumn(PROFILE_NAME);
            this.addColumn(PROFILE_DESC);

            for (UserProfile profile : profiles) {
                Line line = new Line();
                line.addValue(PROFILE_ID, profile.getIdAsString());
                line.addValue(PROFILE_NAME, profile.getName());
                line.addValue(PROFILE_DESC, profile.getDescription());
                this.addLine(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("UserResultList() failed", e);
        }
    }

}
