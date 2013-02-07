/**
 * 
 */
package fr.afnic.commons.beans.profiling;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.validatable.AbstractValidatable;
import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;

/**
 * Objet representant un compte de manière générique. <br>
 * <a href="http://intrawiki.nic.fr/twiki/pub/Cedev/ProjetInformatiqueAfnicCommons/BOA_Afnic-commons_ModeleUML.png" >Voir dans modèle UML de Boa.</a>
 * 
 * @author alaphilippe
 */
public abstract class GenericAccount extends AbstractValidatable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * L'identifiant de connexion du compte.
     */
    private String login;

    /**
     * Le mot de passe lié à l'identifiant.
     */
    private String password;

    @Override
    public InvalidDataDescription checkInvalidData() {
        CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.checkNotNullableField(this.login, "login");
        builder.checkNotNullableField(this.password, "password");
        return builder.build();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasNotEmptyLogin() {
        return !hasEmptyLogin();
    }

    public boolean hasEmptyLogin() {
        return StringUtils.isEmpty(this.login);
    }

    public boolean hasEmptyPassword() {
        return StringUtils.isEmpty(this.password);
    }
}
