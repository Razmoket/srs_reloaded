package fr.afnic.commons.beans.profiling.users;

import java.util.List;

import fr.afnic.commons.beans.list.Column;
import fr.afnic.commons.beans.list.Line;
import fr.afnic.commons.beans.list.SimpleResultList;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UserRightResultList extends SimpleResultList {

    private final static Column NAME = new Column("Nom", "user_right_name", true);
    private final static Column DESCRIPTION = new Column("Description", "user_right_desc");

    public UserRightResultList(List<UserRight> rights, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        try {

            this.addColumn(NAME);
            this.addColumn(DESCRIPTION);

            for (UserRight right : rights) {
                Line line = new Line();
                line.addValue(NAME, right.getDictionaryKey());
                line.addValue(DESCRIPTION, right.getDescription(userId, tld));
                this.addLine(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("UserRightResultList() failed", e);
        }
    }

}
