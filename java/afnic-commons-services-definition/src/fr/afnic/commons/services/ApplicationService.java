/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services;

import fr.afnic.commons.beans.application.Version;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Implementation unique de IApplicationService.<br/>
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class ApplicationService implements IApplicationService {

    private final Version version;

    public ApplicationService(Version version) {
        this.version = version;
    }

    @Override
    public Version getCurrentVersion() {
        return this.version;
    }

    @Override
    public boolean isEnv(Environnement env) {
        return AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == env;
    }

}
