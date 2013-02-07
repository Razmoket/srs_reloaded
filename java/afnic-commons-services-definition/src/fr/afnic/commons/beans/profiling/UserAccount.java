package fr.afnic.commons.beans.profiling;

import java.util.ArrayList;
import java.util.List;

/**
 * Objet de la représentation d'un compte de connexion utilisateur.
 * 
 * @author alaphilippe
 * 
 */
public class UserAccount extends GenericAccount {

    private static final long serialVersionUID = 1L;

    /**
     * liste des droits applicatif du compte utilisateur
     */
    private List<ApplicationRight> applicationRights = new ArrayList<ApplicationRight>();

    /**
     * liste des profil au quel appartient le compte.
     */
    private List<Profile> profiles = new ArrayList<Profile>();

    private int id;

    public List<ApplicationRight> getApplicationRights() {
        return this.applicationRights;
    }

    public void setApplicationRights(List<ApplicationRight> applicationRights) {
        this.applicationRights = applicationRights;
    }

    public List<Profile> getProfiles() {
        return this.profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
