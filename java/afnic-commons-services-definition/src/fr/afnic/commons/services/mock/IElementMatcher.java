package fr.afnic.commons.services.mock;

public interface IElementMatcher<T> {

    public boolean match(T element) throws Exception;

}
