/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search;

import java.io.Serializable;

public enum SearchCriterionOperator implements Serializable {
    Superior,
    Inferior,
    Contains,
    NotContains,
    StartWith,
    EndWith,
    Equals;

}
