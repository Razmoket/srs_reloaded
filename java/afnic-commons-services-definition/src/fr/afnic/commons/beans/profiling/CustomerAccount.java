/**
 * 
 */
package fr.afnic.commons.beans.profiling;

/**
 * Objet representant un compte de manière générique. <br>
 * <a href="http://intrawiki.nic.fr/twiki/pub/Cedev/ProjetInformatiqueAfnicCommons/BOA_Afnic-commons_ModeleUML.png" >Voir dans modèle UML de Boa.</a>
 * 
 * @author alaphilippe
 */
public class CustomerAccount extends GenericAccount {

    private static final long serialVersionUID = 1L;

    private String product;
    private String env;

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getEnv() {
        return this.env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

}
