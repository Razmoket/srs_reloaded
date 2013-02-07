package fr.afnic.commons.beans.profiling;

import fr.afnic.utils.ToStringHelper;

/**
 * Représentation d'un droit utilisateur
 * 
 * @author alaphilippe
 * 
 */
public class ApplicationRight {

    /**
     * Code de référence du droit applicatif.
     */
    private String code;

    /**
     * Description du droit.
     */
    private String description;

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

}
