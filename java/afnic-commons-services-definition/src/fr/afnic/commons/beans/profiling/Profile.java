package fr.afnic.commons.beans.profiling;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.utils.ToStringHelper;

/**
 * Objet contenant et représentant un profile qui regourpe un ensemble de droit applicatif.
 * 
 * @author alaphilippe
 * @see ApplicationRight
 */
public class Profile {

    /**
     * Code de référence du profil.
     */
    private String code;

    /**
     * Description du profil.
     */
    private String description;

    /**
     * liste des droits composant le profil
     */
    private List<ApplicationRight> applicationRights = new ArrayList<ApplicationRight>();

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ApplicationRight> getApplicationRights() {
        return applicationRights;
    }

    public void setApplicationRights(List<ApplicationRight> applicationRights) {
        this.applicationRights = applicationRights;
    }
}
