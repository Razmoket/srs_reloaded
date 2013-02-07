package fr.afnic.commons.beans.profiling.users;

import java.util.List;

import fr.afnic.commons.beans.list.Column;
import fr.afnic.commons.beans.list.Line;
import fr.afnic.commons.beans.list.SimpleResultList;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UserResultList extends SimpleResultList {

    private final static Column USER_ID = new Column("Id", "user_id", true);
    private final static Column LOGIN = new Column("Identifiant", "user_login");
    private final static Column FIRSTNAME = new Column("Prénom", "user_firstname");
    private final static Column LASTNAME = new Column("Nom", "user_lastname");
    private final static Column STATUS = new Column("Statut", "user_status");
    private final static Column PROFIL = new Column("Profile", "user_profile");

    public UserResultList(List<User> users, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        try {

            this.addColumn(USER_ID);
            this.addColumn(LOGIN);
            this.addColumn(FIRSTNAME);
            this.addColumn(LASTNAME);
            this.addColumn(STATUS);
            this.addColumn(PROFIL);

            for (User user : users) {
                Line line = new Line();
                line.addValue(USER_ID, user.getIdAsString());
                line.addValue(LOGIN, user.getLogin());
                line.addValue(FIRSTNAME, user.getFirstName());
                line.addValue(LASTNAME, user.getLastName());
                line.addValue(STATUS, user.getStatus().getDescription());
                line.addValue(PROFIL, user.getProfileName());
                this.addLine(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("UserResultList() failed", e);
        }
    }

}
