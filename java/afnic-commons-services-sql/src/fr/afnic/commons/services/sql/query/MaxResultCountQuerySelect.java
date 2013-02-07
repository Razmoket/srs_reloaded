/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

public class MaxResultCountQuerySelect extends Select<CustomSelectElement> {
    /**
     * 
     */
    private static final long serialVersionUID = 5263132570271901813L;

    public MaxResultCountQuerySelect() {
        this.add(new CustomSelectElement("c.*, rownum"));
    }
}
