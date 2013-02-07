/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services;

import fr.afnic.commons.beans.application.Version;
import fr.afnic.commons.beans.application.env.Environnement;

/**
 * Service permettant de récupérer les informations sur l'application courante.
 * 
 * @author ginguene
 * 
 */
public interface IApplicationService {

    public Version getCurrentVersion();

    public boolean isEnv(Environnement env);

}
