package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ElementsSearcher {

    public static <T> List<T> search(Collection<T> elements, IElementMatcher<T> matcher) throws Exception {
        List<T> matchingElements = new ArrayList<T>();

        for (T element : elements) {
            if (matcher.match(element)) {
                matchingElements.add(element);
            }
        }
        return matchingElements;
    }

    private ElementsSearcher() {

    }

}
