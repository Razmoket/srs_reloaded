package fr.afnic.commons.services.mock;

import fr.afnic.commons.beans.application.Application;
import fr.afnic.commons.beans.application.Version;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.services.IApplicationService;
import fr.afnic.commons.services.facade.AppServiceFacade;

public class MockApplicationService implements IApplicationService {

    private Version currentMockVersion = null;

    public MockApplicationService() {
        this.currentMockVersion = new Version(1, 2, 3, 666, new Application("Boa"), Environnement.Dev);
    }

    @Override
    public Version getCurrentVersion() {
        return this.currentMockVersion;
    }

    @Override
    public boolean isEnv(Environnement env) {
        return AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == env;
    }

}
