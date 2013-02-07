/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.application;

import java.io.Serializable;

import fr.afnic.commons.utils.Preconditions;

public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public Application(String name) {
        this.name = Preconditions.checkNotEmpty(name, "name");
    }

    public String getName() {
        return this.name;
    }

}
