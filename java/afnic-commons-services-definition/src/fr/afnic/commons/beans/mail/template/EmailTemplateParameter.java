/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package fr.afnic.commons.beans.mail.template;

import java.io.Serializable;

import fr.afnic.utils.ToStringHelper;

/**
 * The EmailTemplateParameter Structure
 */
public class EmailTemplateParameter implements Serializable {

    private String key;
    private String value;

    public EmailTemplateParameter() {

    }

    public EmailTemplateParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
