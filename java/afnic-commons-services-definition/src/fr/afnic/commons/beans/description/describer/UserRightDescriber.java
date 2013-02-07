/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.description.describer;

import java.util.HashMap;
import java.util.Locale;

import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UserRightDescriber implements IDescriber<UserRight> {

    private static final HashMap<UserRight, String> MAP = UserRightDescriber.createMap();

    private static final HashMap<UserRight, String> createMap() {
        HashMap<UserRight, String> map = new HashMap<UserRight, String>();
        map.put(UserRight.AccountManager, "chargé de clientèle");
        map.put(UserRight.TicketWrite, "visualisation des tickets");
        map.put(UserRight.TicketRead, "modification des tickets");
        map.put(UserRight.EmailView, "visualisation des tickets");
        map.put(UserRight.EmailSend, "envoi des mails");
        map.put(UserRight.Viewer, "visualisation");
        map.put(UserRight.AuthorizationRead, "visualisation des requetes d'autorisation");
        map.put(UserRight.AuthorizationWrite, "modification des requetes d'autorisation");
        map.put(UserRight.AuthorizationUnlock, "déverouillage des requetes d'autorisation");
        map.put(UserRight.AuthorizationSuppression, "suppression des requetes d'autorisation");

        map.put(UserRight.TradeRead, "visualisation des requetes de trade");
        map.put(UserRight.TradeWrite, "modification des requetes de trade");
        map.put(UserRight.TradeUnlock, "déverouillage des requetes de trade");
        map.put(UserRight.Testing, "testing");
        map.put(UserRight.StagedDelete, "suppression controlée");
        map.put(UserRight.CustomerCreate, "creation des clients");
        map.put(UserRight.CustomerUpdate, "modification des clients");
        map.put(UserRight.CustomerRead, "visualisation des clients");
        map.put(UserRight.DomainRead, "visualisation des domaines");
        map.put(UserRight.DomainBlock, "blocage des domaines");

        map.put(UserRight.WhoisContactWrite, "modification des contacts");
        map.put(UserRight.WhoisContactRead, "visualisation des contacts");
        map.put(UserRight.WhoisContactBlockportfolio, "blocage du portefeuilles des titulaires");

        map.put(UserRight.QualificationRead, "visualisation des qualifications");
        map.put(UserRight.QualificationWrite, "modification des qualifications");
        map.put(UserRight.QualificationCreate, "Créaion de qualifications");
        map.put(UserRight.QualificationStat, "Consultation des stats de qualifications");

        map.put(UserRight.All, "tous les droits");

        map.put(UserRight.OperationRelaunch, "relancer des opérations");

        map.put(UserRight.Admin, "Administration du site");
        map.put(UserRight.StatistictRead, "Consultation des statistiques");
        map.put(UserRight.Admin, "Administration du site");
        map.put(UserRight.ContractCreate, "Création de contrat");
        map.put(UserRight.ContractUpdate, "Modification de contrat");

        return map;
    }

    @Override
    public String getDescription(UserRight userRight, Locale locale, UserId userId, TldServiceFacade tld) {
        if (UserRightDescriber.MAP.containsKey(userRight)) {
            return UserRightDescriber.MAP.get(userRight);
        } else {
            return userRight + "(complete UserRightDescriber for a better description)";
        }
    }

}
