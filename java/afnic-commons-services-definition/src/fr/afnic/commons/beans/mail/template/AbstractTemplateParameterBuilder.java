/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.mail.template;

import java.util.List;

/**
 * un builder de parametre de template permet de générer une liste de parametres (clé + valeur) destiné à templir les variables dans un template de
 * mail.
 * 
 * @author ginguene
 * 
 * @param <TEMPLATE_PARAMETERS>
 */
public abstract class AbstractTemplateParameterBuilder<TEMPLATE_PARAMETERS extends ITemplateParameters> {

    public abstract List<EmailTemplateParameter> build();

    protected List<EmailTemplateParameter> parameters;

    protected void addParameter(TEMPLATE_PARAMETERS param, String value) {
        parameters.add(new EmailTemplateParameter(param.getKey(), value));
    }

    protected void addParameter(TEMPLATE_PARAMETERS param, int value) {
        parameters.add(new EmailTemplateParameter(param.getKey(), Integer.toString(value)));
    }
}
