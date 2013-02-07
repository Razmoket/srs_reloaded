/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package fr.afnic.commons.beans.mail.template;

import java.io.Serializable;
import java.util.List;

public class EmailTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    public int idTemplate;

    public EmailTemplateType type;
    public List<EmailTemplateParameter> parameters;
    public String content;
    public String name;
    public String object;

    public int getIdTemplate() {
        return this.idTemplate;
    }

    public void setIdTemplate(int idTemplate) {
        this.idTemplate = idTemplate;
    }

    public EmailTemplateType getType() {
        return this.type;
    }

    public void setType(EmailTemplateType type) {
        this.type = type;
    }

    public List<EmailTemplateParameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<EmailTemplateParameter> parameters) {
        this.parameters = parameters;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }

}
