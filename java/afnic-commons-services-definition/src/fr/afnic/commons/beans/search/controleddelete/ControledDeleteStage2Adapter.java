package fr.afnic.commons.beans.search.controleddelete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.validatable.IValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataException;

public class ControledDeleteStage2Adapter implements Serializable, IValidatable {

    private static final long serialVersionUID = 1L;
    private final List<String[]> selectLineCheckboxGroup = new ArrayList<String[]>();

    private String selectUser;

    @Override
    public void validate() throws InvalidDataException {
    }

    public List<String[]> getSelectLineCheckboxGroup() {
        return this.selectLineCheckboxGroup;
    }

    public String getSelectUser() {
        return this.selectUser;
    }

    public void setSelectUser(String selectUser) {
        this.selectUser = selectUser;
    }
}
