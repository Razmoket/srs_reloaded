package fr.afnic.commons.services.filter;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.services.exception.NotFoundException;

public class ResultFilter<T> {

    private IObjectIdentifier<T> identifier;
    private List<String> availableIdentifiers = new ArrayList<String>();

    public ResultFilter(IObjectIdentifier<T> identifier) {
        this.identifier = identifier;
    }

    public T filterSingleResult(T result) throws NotFoundException {
        for (String id : availableIdentifiers) {
            if (id.equals(identifier.getId(result))) {
                return result;
            }
        }
        throw new NotFoundException("not found");
    }

    public List<T> filterResults(List<T> results) throws NotFoundException {
        List<T> returnedResults = new ArrayList<T>();

        for (T result : results) {
            for (String id : availableIdentifiers) {
                if (id.equals(identifier.getId(result))) {
                    returnedResults.add(result);
                    break;
                }
            }
        }
        return returnedResults;
    }

    public List<String> filterIdentifierResults(List<String> identifierResults) throws NotFoundException {
        List<String> returnedResults = new ArrayList<String>();

        for (String result : identifierResults) {
            for (String id : availableIdentifiers) {
                if (id.equals(result)) {
                    returnedResults.add(result);
                    break;
                }
            }
        }
        return returnedResults;
    }

    public void addAvailableResult(T result) {
        availableIdentifiers.add(identifier.getId(result));
    }

    public void removeAvailableResult(T result) {
        availableIdentifiers.remove(identifier.getId(result));
    }
}
