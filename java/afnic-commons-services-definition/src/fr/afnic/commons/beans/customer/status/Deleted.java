package fr.afnic.commons.beans.customer.status;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;

public class Deleted extends CustomerStatus {

    @Override
    public <S extends CustomerStatus> void populateNextAllowedStatus(List<Class<S>> list, Customer customer) {

    }

}
